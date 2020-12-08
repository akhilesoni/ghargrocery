package com.ghargrocery.android;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class UserAddressActivity extends AppCompatActivity {
    private TextView fullname, house, area, city, pincode, button;
    private String uid;
    private ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_address);

        fullname = (TextView) findViewById(R.id.fullname);
        house = (TextView) findViewById(R.id.House);
        area = (TextView) findViewById(R.id.area);
        city = (TextView) findViewById(R.id.city);
        pincode = (TextView) findViewById(R.id.pincode);
        button = (TextView) findViewById(R.id.changeaddressbutton);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        FirebaseDatabase.getInstance().getReference("users").child(uid).child("address").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Address mAddress = dataSnapshot.getValue(Address.class);
                fullname.setText(mAddress.getFull_name());
                fullname.setVisibility(View.VISIBLE);
                house.setText(mAddress.getHouse_no());
                house.setVisibility(View.VISIBLE);
                area.setText(mAddress.getArea());
                area.setVisibility(View.VISIBLE);
                city.setText(mAddress.getCity());
                city.setVisibility(View.VISIBLE);
                pincode.setText(mAddress.getPincode());
                pincode.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),AddressActivity.class));
            }
        });

    }
}