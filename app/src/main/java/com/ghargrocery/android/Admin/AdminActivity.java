package com.ghargrocery.android.Admin;

import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.ghargrocery.android.R;

public class AdminActivity extends AppCompatActivity{
    private BottomNavigationView bottomNavigationView;
    private FrameLayout frameLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        bottomNavigationView = (BottomNavigationView) findViewById(R.id.botton_navigation_view);
        frameLayout = (FrameLayout) findViewById(R.id.admin_frame);
        bottomNavigationView.setOnNavigationItemSelectedListener(navigationItemSelectedListener);


        if(savedInstanceState == null){
            getSupportFragmentManager().beginTransaction().replace(R.id.admin_frame,new OrdersFragment()).commit();
        }

    }
    private BottomNavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
            Fragment fragment = null;
            switch (menuItem.getItemId()){
                case R.id.orders:
                    menuItem.setCheckable(true);
                    fragment = new OrdersFragment();
                    break;

                case R.id.options:
                    menuItem.setCheckable(true);
                    fragment = new OptionsFragment();
                    break;
            }
            getSupportFragmentManager().beginTransaction().replace(R.id.admin_frame,fragment).commit();
            return false;
        }
    };


}