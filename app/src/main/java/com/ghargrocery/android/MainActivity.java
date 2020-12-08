package com.ghargrocery.android;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.ghargrocery.android.Admin.AdminActivity;
import com.ghargrocery.android.Notifications.Token;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    DrawerLayout drawer;
    Toolbar toolbar;
    private String fname,fphone;
    private FirebaseUser mUser;
    private FirebaseDatabase database;
    private TextView name,phone;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mUser = FirebaseAuth.getInstance().getCurrentUser();
        database = FirebaseDatabase.getInstance();

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setTitle(" ");

        Token token = new Token(FirebaseInstanceId.getInstance().getToken());
        database.getReference("tokens").child(mUser.getUid()).setValue(token);

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,drawer,toolbar,R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        View header = navigationView.getHeaderView(0);

        name = (TextView)header.findViewById(R.id.user_name);
        phone = (TextView)header.findViewById(R.id.user_phone);



        database.getReference("users").child(mUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    fname = dataSnapshot.child("fname").getValue(String.class);
                    fphone = dataSnapshot.child("phone").getValue(String.class);
                    name.setText(fname);
                    phone.setText(fphone);
                }
                else{
                    Toast.makeText(getApplicationContext(),"this is not working",Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(),"this 2 is not working",Toast.LENGTH_LONG).show();

            }
        });

        if(savedInstanceState == null){
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_frame,new HomeFragment()).commit();
            navigationView.setCheckedItem(R.id.dnav_home);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
         getMenuInflater().inflate(R.menu.side_menu,menu);
         return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.actionSetting:
                getSupportFragmentManager().beginTransaction().addToBackStack("tag").replace(R.id.fragment_frame,new SettingFragment()).commit();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()){
            case R.id.dnav_home:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_frame,new HomeFragment()).commit();
                break;
            case R.id.dnav_cart:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_frame,new CartFragment()).commit();
                break;
            case R.id.dnav_setting:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_frame,new SettingFragment()).commit();
                break;
            case R.id.nav_login:
                gotologin();
                break;
            case R.id.admin:
                startActivity(new Intent(getApplicationContext(), AdminActivity.class));
                break;
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void gotologin(){
        Intent i = new Intent(getApplicationContext(),LoginActivity.class);
        startActivity(i);
    }
    @Override
    public void onBackPressed() {
        if(drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }
        else {
            super.onBackPressed();
        }
    }
}