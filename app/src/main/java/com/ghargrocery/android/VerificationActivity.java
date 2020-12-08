package com.ghargrocery.android;

import android.content.Intent;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class VerificationActivity extends AppCompatActivity {
    private EditText otpCode;
    private TextView verify_button,notice_text;
    private CardView notice_card;
    private ProgressBar progressBar;
    private View view;
    private String systemCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verification);
        otpCode = (EditText) findViewById(R.id.otpCode);
        verify_button = (TextView) findViewById(R.id.verifyButton);
        notice_text = (TextView) findViewById(R.id.notice_text);
        notice_card = (CardView) findViewById(R.id.notice_card);
        view = (View) findViewById(R.id.tint);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        String phoneNo = getIntent().getStringExtra("phone_number");

        sendVerificationCode(phoneNo);
        progressBar.setVisibility(View.VISIBLE);

        verify_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String code = otpCode.getText().toString();

                if (code.isEmpty() || code.length() < 6) {
                    otpCode.setError("Wrong OTP...");
                    otpCode.requestFocus();
                    return;
                }

                verifyCode(code);
                notice_card.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.VISIBLE);
                view.setVisibility(View.VISIBLE);
            }
        });
    }


    private void sendVerificationCode(String phoneNo) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                "+91" + phoneNo,
                60,
                TimeUnit.SECONDS,
                TaskExecutors.MAIN_THREAD,
                mCallbacks);
    }

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            //super.onCodeSent(s, forceResendingToken);
            systemCode = s;
        }

        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
            String code = phoneAuthCredential.getSmsCode();
            if(code != null){
                verifyCode(code);
            }
        }

        @Override
        public void onVerificationFailed(FirebaseException e) {
            progressBar.setVisibility(View.INVISIBLE);
            notice_text.setText("Invalid code!");
            notice_text.setVisibility(View.VISIBLE);

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    notice_card.setVisibility(View.INVISIBLE);
                    notice_text.setVisibility(View.INVISIBLE);
                    view.setVisibility(View.INVISIBLE);
                }
            }, 2000);

        }
    };

    private void verifyCode(String code) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(systemCode,code);
        signInTheUserByCredential(credential);
    }

    private void signInTheUserByCredential(PhoneAuthCredential credential) {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        mAuth.signInWithCredential(credential).addOnCompleteListener(VerificationActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    notice_card.setVisibility(View.INVISIBLE);
                    progressBar.setVisibility(View.INVISIBLE);
                    view.setVisibility(View.INVISIBLE);
                    startActivity(new Intent(getApplicationContext(),RegisterationActivity.class));
                }else{
                    progressBar.setVisibility(View.INVISIBLE);
                    notice_text.setText("Invalid code!");
                    notice_text.setVisibility(View.VISIBLE);

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            notice_card.setVisibility(View.INVISIBLE);
                            notice_text.setVisibility(View.INVISIBLE);
                            view.setVisibility(View.INVISIBLE);
                        }
                    },2000);
                }
            }
        });
    }
}
