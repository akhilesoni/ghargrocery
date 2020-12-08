package com.ghargrocery.android;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class ProductDetailFragment extends Fragment {
    private TextView product_name,product_weight_1,product_weight_2,product_prize,product_category,address;
    private ImageView product_image_view;
    private String product_id;
    private TextView add_cart,buy_now;
    private ArrayAdapter<String> arrayAdapter;
    private Product product;
    private Spinner spinner;
    private String uid;
    private CardView notice_card;
    private TextView notice_text;
    private ProgressBar progressBar;
    private View view;
    DatabaseReference databaseReference;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_product_detail, container, false);
        String[] quantity = {"1","2","3","4","5","6","7","8","9","10","11","12","13","14","15","16"};
        Bundle bundle = getArguments();
        product_id = bundle.getString("ProductId");
        product_name = v.findViewById(R.id.product_name);
        product_weight_2 = v.findViewById(R.id.product_weight_1);
        product_weight_1 = v.findViewById(R.id.product_weight_2);
        product_prize = v.findViewById(R.id.product_prize);
        product_category = v.findViewById(R.id.product_category);
        spinner = v.findViewById(R.id.quantity_spin);
        add_cart = v.findViewById(R.id.add_cart_button);
        buy_now = v.findViewById(R.id.buy_now_button);
        address = v.findViewById(R.id.address);
        uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        notice_text = v.findViewById(R.id.notice_text);
        notice_card =  v.findViewById(R.id.notice_card);
        view = v.findViewById(R.id.tint);
        progressBar = v.findViewById(R.id.progressBar);

        arrayAdapter = new ArrayAdapter<String>(getContext(),android.R.layout.simple_spinner_item,quantity);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(arrayAdapter);
        product_image_view = v.findViewById(R.id.product_image_view);
        databaseReference = FirebaseDatabase.getInstance().getReference("products");
        databaseReference.child(product_id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                product = dataSnapshot.getValue(Product.class);
                product_name.setText(product.getProduct_name());
                product_category.setText(product.getProduct_category());
                product_prize.setText(product.getProduct_prize());
                product_weight_1.setText("("+product.getProduct_weight()+"kg)");
                product_weight_2.setText(product.getProduct_weight()+"kg");
                Glide.with(getContext()).load(product.getProduct_img_url()).into(product_image_view);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getContext(),"Failed to load",Toast.LENGTH_LONG).show();
            }
        });

        add_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CartProduct cartProduct = new CartProduct(product.getProduct_name(),product.getProduct_weight(),product.getProduct_prize(),spinner.getSelectedItem().toString(),product.getProduct_category(),product.getProduct_img_url(),product.getProduct_id());
                String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                FirebaseDatabase.getInstance().getReference("users").child(uid).child("cart").child(product.getProduct_id()).setValue(cartProduct);
                notice_card.setVisibility(View.VISIBLE);
                notice_text.setText("Added To Cart Successfully");
                notice_text.setVisibility(View.VISIBLE);
                view.setVisibility(View.VISIBLE);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        notice_card.setVisibility(View.INVISIBLE);
                        notice_text.setVisibility(View.INVISIBLE);
                        view.setVisibility(View.INVISIBLE);
                    }
                }, 2000);
            }
        });
        return v;
    }
}