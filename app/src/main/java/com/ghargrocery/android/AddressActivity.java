package com.ghargrocery.android;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import org.w3c.dom.Text;

public class AddressActivity extends AppCompatActivity {
    private EditText fname,house_no,pincode,city,area;
    private TextView submit;
    private String uid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address);

        fname = (EditText) findViewById(R.id.fullname);
        house_no = (EditText) findViewById(R.id.house_no);
        pincode = (EditText) findViewById(R.id.pincode);
        city = (EditText) findViewById(R.id.city);
        area = (EditText) findViewById(R.id.area);
        submit = (TextView) findViewById(R.id.submit);
        uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Address address = new Address(fname.getText().toString(),house_no.getText().toString(),area.getText().toString(),city.getText().toString(),"chhatisgarh",pincode.getText().toString(),"India");
                FirebaseDatabase.getInstance().getReference("users").child(uid).child("address").setValue(address);
                startActivity(new Intent(getApplicationContext(),BillingActivity.class));
                finish();
            }
        });
    }
}