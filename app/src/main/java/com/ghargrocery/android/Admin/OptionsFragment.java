package com.ghargrocery.android.Admin;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ghargrocery.android.R;
import com.ghargrocery.android.UploadActivity;


public class OptionsFragment extends Fragment {

    private TextView uploadProductButton;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_options, container, false);

        uploadProductButton = v.findViewById(R.id.uploadProductButton);

        uploadProductButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), UploadActivity.class));
            }
        });

        return v;
    }
}