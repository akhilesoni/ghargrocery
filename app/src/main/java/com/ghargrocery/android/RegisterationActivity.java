package com.ghargrocery.android;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class RegisterationActivity extends AppCompatActivity {
    private EditText name;
    private TextView register_button,terms_conditions_text;
    private CardView notice_card;
    private CheckBox accept;
    private ProgressBar progressBar;
    private View view;
    private FirebaseUser user;
    private DatabaseReference databaseReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registeration);

        name = (EditText) findViewById(R.id.name);
        register_button = (TextView) findViewById(R.id.register_button);
        terms_conditions_text = (TextView) findViewById(R.id.term_condition);
        accept = (CheckBox) findViewById(R.id.term_check_box);
        notice_card = (CardView) findViewById(R.id.notice_card);
        view = (View) findViewById(R.id.tint);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        user = FirebaseAuth.getInstance().getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference("users");

        databaseReference.child(user.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                if(user != null){
                    startActivity(new Intent(getApplicationContext(),MainActivity.class));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        register_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String fname = name.getText().toString().trim();

                if(fname.isEmpty()){
                    name.setError("Please enter name");
                    name.requestFocus();
                    return;
                }
                if(!accept.isChecked()){
                    accept.setError("please accept this !");
                    accept.requestFocus();
                    return;
                }

                progressBar.setVisibility(View.VISIBLE);
                notice_card.setVisibility(View.VISIBLE);
                view.setVisibility(View.VISIBLE);

                User fuser = new User(fname,user.getPhoneNumber().trim());
                databaseReference.child(user.getUid()).setValue(fuser);

                Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);

                progressBar.setVisibility(View.INVISIBLE);
                notice_card.setVisibility(View.INVISIBLE);
                view.setVisibility(View.INVISIBLE);

            }
        });


    }
}