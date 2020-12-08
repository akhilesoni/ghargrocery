package com.ghargrocery.android;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.ghargrocery.android.Admin.Order;
import com.ghargrocery.android.Notifications.Client;
import com.ghargrocery.android.Notifications.Data;
import com.ghargrocery.android.Notifications.MyResponse;
import com.ghargrocery.android.Notifications.Sender;
import com.ghargrocery.android.Notifications.Token;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Set;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class CartFragment extends Fragment {
    private RecyclerView cart_recycler_view;
    private TextView checkout;
    private List<CartProduct> cartProductList;
    private CartProductAdapter cartProductAdapter;
    private ImageView notice_image_view;
    private ProgressBar progressBar;
    private TextView cart_total;
    DatabaseReference cartDatabaseReference;
    DatabaseReference productDatabaseReference;
    private FloatingActionButton homefab;
    private TextView notice;
    private Integer total = 0;
    private Boolean cartEmpty = false;
    APIService apiService;
    private String uid;
    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_cart,container,false);
        progressBar = v.findViewById(R.id.progressBar);
        notice_image_view = v.findViewById(R.id.notice_image_view);
        checkout = v.findViewById(R.id.cart_checkout);
        notice = v.findViewById(R.id.notice);
        cart_total = v.findViewById(R.id.cart_total);
        cart_recycler_view = v.findViewById(R.id.cart_recycler_view);
        cart_recycler_view.hasFixedSize();
        cart_recycler_view.setLayoutManager(new LinearLayoutManager(getContext()));
        cartProductList = new ArrayList<CartProduct>();
        cartProductAdapter = new CartProductAdapter(getContext(),cartProductList);
        cart_recycler_view.setAdapter(cartProductAdapter);

        homefab = v.findViewById(R.id.home_fab);

        apiService = Client.getClient().create(APIService.class);

        cartDatabaseReference = FirebaseDatabase.getInstance().getReference("users");
        productDatabaseReference = FirebaseDatabase.getInstance().getReference("products");
        uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        cartDatabaseReference.child(uid).child("cart").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(!dataSnapshot.exists()){
                    notice.setText("Nothing in the cart..");
                    cartEmpty = true;
                    total = 0;
                    notice_image_view.setVisibility(View.VISIBLE);
                }
                else {
                    notice_image_view.setVisibility(View.INVISIBLE);
                }
                cartProductList.clear();
                total = 0;
                for(DataSnapshot p : dataSnapshot.getChildren()){
                    CartProduct product = p.getValue(CartProduct.class);
                    cartProductList.add(product);
                    total = total + Integer.parseInt(product.getQuantity())*Integer.parseInt(product.getPrize());
                }

                cart_total.setText(Integer.toString(total));
                cartProductAdapter.notifyDataSetChanged();
                progressBar.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                progressBar.setVisibility(View.INVISIBLE);
                Toast.makeText(getContext(),"inner value problem",Toast.LENGTH_LONG).show();

            }
        });
        final String message = "hello";
        checkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(cartEmpty){
                    Toast.makeText(getContext(), "Cart is Empty", Toast.LENGTH_SHORT).show();
                }else {
                    Random random = new Random();
                    String order_id = Integer.toString(random.nextInt(10000000));
                    String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                    Intent intent = new Intent(getContext(),BillingActivity.class);
                    intent.putExtra("date",new SimpleDateFormat("dd/MM/yy HH:mm:ss").format(new Date()).toString());
                    intent.putExtra("order_id",order_id);
                    intent.putExtra("total",Integer.toString(total));
                    startActivity(intent);
                }


            }
        });

        homefab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().beginTransaction().disallowAddToBackStack().replace(R.id.fragment_frame,new HomeFragment()).commit();
            }
        });

        return v;
    }

    private void sendNotification(String receiver, String username, String message ){
        FirebaseDatabase.getInstance().getReference("tokens").child(receiver).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Token token = dataSnapshot.getValue(Token.class);
                Data data = new Data(FirebaseAuth.getInstance().getCurrentUser().getUid(),"hey bud","order","WEk7CeqAYnh8s2IFppWAJxkSkWH2",R.mipmap.ic_launcher);
                Sender sender = new Sender(data,token.getToken());

                apiService.sendNotification(sender).enqueue(new Callback<MyResponse>() {
                    @Override
                    public void onResponse(Call<MyResponse> call, Response<MyResponse> response) {
                        if(response.code() == 200){
                            Log.d("hey", "working ");
                            if(response.body().success!= 1){
                                Log.d("here", "it is not worikin ");
                                Toast.makeText(getContext(), "fucked", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<MyResponse> call, Throwable t) {
                        Log.d("fucked", "notwrokin ");
                        Toast.makeText(getContext(), "fucked", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getContext(), "Failed", Toast.LENGTH_SHORT).show();
            }
        });
    }



}
