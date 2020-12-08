package com.ghargrocery.android;

import android.content.Intent;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.ghargrocery.android.Admin.Order;
import com.ghargrocery.android.Notifications.Client;
import com.ghargrocery.android.Notifications.Data;
import com.ghargrocery.android.Notifications.MyResponse;
import com.ghargrocery.android.Notifications.Sender;
import com.ghargrocery.android.Notifications.Token;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BillingActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private List<CartProduct> cartProductList;
    private BillAdapter billAdapter;
    private String uid;
    private TextView order_number,phone_number,date,address,grand_total,button_continue;
    private TextView notice_text;
    private CardView notice_card;
    private ProgressBar progressBar;
    private View view;

    APIService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_billing);

        apiService = Client.getClient().create(APIService.class);

        notice_text = (TextView) findViewById(R.id.notice_text);
        notice_card = (CardView) findViewById(R.id.notice_card);
        view = (View) findViewById(R.id.tint);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        recyclerView = (RecyclerView) findViewById(R.id.bill_recycler_view);
        recyclerView.hasFixedSize();
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        Bundle extras = getIntent().getExtras();
        button_continue = (TextView) findViewById(R.id.button_continue);
        order_number = (TextView) findViewById(R.id.order_number);
        phone_number = (TextView) findViewById(R.id.phone_no);
        grand_total = (TextView) findViewById(R.id.grand_total);
        date = (TextView) findViewById(R.id.date);
        address = (TextView) findViewById(R.id.address);
        order_number.setText(extras.getString("order_id"));
        date.setText(extras.getString("date"));
        grand_total.setText(extras.getString("total"));
        cartProductList = new ArrayList<CartProduct>();
        uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        FirebaseDatabase.getInstance().getReference("users").child(uid).child("address").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(!dataSnapshot.exists()){
                    startActivity(new Intent(getApplicationContext(),AddressActivity.class));
                }
                else {
                    Address address1 = dataSnapshot.getValue(Address.class);
                    address.setText(address1.full_name+", "+address1.house_no+", "+address1.area+", "+address1.city);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        FirebaseDatabase.getInstance().getReference("users").child(uid).child("cart").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                cartProductList.clear();
                for(DataSnapshot p : dataSnapshot.getChildren()){
                    CartProduct product = p.getValue(CartProduct.class);
                    cartProductList.add(product);
                }
                billAdapter = new BillAdapter(getApplicationContext(),cartProductList);
                recyclerView.setAdapter(billAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



        button_continue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                view.setVisibility(View.VISIBLE);
                notice_card.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.VISIBLE);
                FirebaseDatabase.getInstance().getReference("users").child(uid).child("cart").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        String order_id = FirebaseDatabase.getInstance().getReference("orders").push().getKey();
                        Order order = new Order(uid,order_id,new SimpleDateFormat("dd/MM/yy HH:mm:ss").format(new Date()).toString(),"pending");
                        FirebaseDatabase.getInstance().getReference("orders").child(order_id).setValue(order);
                        FirebaseDatabase.getInstance().getReference("orders").child(order_id).child("items").setValue(dataSnapshot.getValue());
                        progressBar.setVisibility(View.INVISIBLE);
                        notice_text.setText("Order Placed Successfully ");
                        notice_text.setVisibility(View.VISIBLE);
                        sendNotification("WEk7CeqAYnh8s2IFppWAJxkSkWH2", "Order Received", order_id);
                        sendNotification(uid,"Order Confirmed","please check order details...");
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                view.setVisibility(View.INVISIBLE);
                                notice_card.setVisibility(View.INVISIBLE);
                                progressBar.setVisibility(View.INVISIBLE);
                                notice_text.setVisibility(View.INVISIBLE);
                                startActivity(new Intent(getApplicationContext(),MainActivity.class));
                            }
                        }, 3000);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });

    }
    private void sendNotification(String receiver, final String title, final String message ){
        FirebaseDatabase.getInstance().getReference("tokens").child(receiver).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Token token = dataSnapshot.getValue(Token.class);
                Data data = new Data(FirebaseAuth.getInstance().getCurrentUser().getUid(),message,title,"WEk7CeqAYnh8s2IFppWAJxkSkWH2",R.mipmap.ic_launcher);
                Sender sender = new Sender(data,token.getToken());

                apiService.sendNotification(sender).enqueue(new Callback<MyResponse>() {
                    @Override
                    public void onResponse(Call<MyResponse> call, Response<MyResponse> response) {
                        if(response.code() == 200){
                            Log.d("hey", "working ");
                            if(response.body().success!= 1){
                                Log.d("here", "it is not worikin ");
                                Toast.makeText(getApplicationContext(), "fucked", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<MyResponse> call, Throwable t) {
                        Log.d("fucked", "notwrokin ");
                        Toast.makeText(getApplicationContext(), "fucked", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(), "Failed", Toast.LENGTH_SHORT).show();
            }
        });
    }
}