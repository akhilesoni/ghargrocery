package com.ghargrocery.android;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.w3c.dom.Text;

public class LoginActivity extends AppCompatActivity {
    private EditText phone_number;
    private TextView login_button,notice_text;
    private CardView notice_card;
    private ProgressBar progressBar;
    private View view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        phone_number = (EditText) findViewById(R.id.phoneEditText);
        login_button = (TextView) findViewById(R.id.login);
        notice_text = (TextView) findViewById(R.id.notice_text);
        notice_card = (CardView) findViewById(R.id.notice_card);
        view = (View) findViewById(R.id.tint);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phoneNo = phone_number.getText().toString().trim();
                if(phoneNo.isEmpty()){
                    phone_number.setError("Please Enter Phone Number First!");
                    phone_number.requestFocus();
                    return;
                }
                if(phoneNo.length()!= 10){
                    phone_number.setError("Please Enter Valid Phone Number!");
                    phone_number.requestFocus();
                    return;
                }
                notice_card.setVisibility(View.VISIBLE);
                view.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.VISIBLE);

                Intent intent = new Intent(getApplicationContext(),VerificationActivity.class);
                intent.putExtra("phone_number",phoneNo);
                startActivity(intent);

                notice_card.setVisibility(View.INVISIBLE);
                view.setVisibility(View.INVISIBLE);
                progressBar.setVisibility(View.INVISIBLE);
            }
        });
    }
}