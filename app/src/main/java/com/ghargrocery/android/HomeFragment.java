package com.ghargrocery.android;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {
    private View view;
    private RecyclerView category_recyclerView;
    private List<Category> categoryList;
    DatabaseReference categoriesDatabaseReference;
    private CategoriesAdapter mAdapter;
    private CardView notice_card;
    private TextView notice_text;
    private ProgressBar progressBar;
    private TextView r1,r2,r3;
    DatabaseReference featuredbf;
    private List<Product> featureList;
    private RecyclerView feature_recycler_view;
    private FloatingActionButton cartfab;
    DatabaseReference bestdbf;
    private List<Product> bestList;
    private RecyclerView best_recycler_view;

    private ProductAdapter productAdapter;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home,container,false);
        notice_text = view.findViewById(R.id.notice_text);
        notice_card =  view.findViewById(R.id.notice_card);
        progressBar = view.findViewById(R.id.progressBar);
        r1 = view.findViewById(R.id.r1);
        r2 = view.findViewById(R.id.r2);
        r3 = view.findViewById(R.id.r3);

        cartfab = view.findViewById(R.id.cart_fab);

        category_recyclerView = view.findViewById(R.id.home_categories_recycler_view);
        category_recyclerView.setHasFixedSize(true);
        category_recyclerView.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false));

        feature_recycler_view = view.findViewById(R.id.home_feature_products_recycler_view);
        feature_recycler_view.setHasFixedSize(true);
        feature_recycler_view.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false));

        best_recycler_view = view.findViewById(R.id.home_best_recycler_view);
        best_recycler_view.hasFixedSize();
        best_recycler_view.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false));

        categoryList = new ArrayList<Category>();
        featureList = new ArrayList<Product>();
        bestList = new ArrayList<Product>();

        featuredbf = FirebaseDatabase.getInstance().getReference("products");
        categoriesDatabaseReference = FirebaseDatabase.getInstance().getReference("categories");
        bestdbf = FirebaseDatabase.getInstance().getReference("products");

        categoriesDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot postSnapshot : dataSnapshot.getChildren()){
                    Category c = postSnapshot.getValue(Category.class);
                    categoryList.add(c);
                }

                mAdapter = new CategoriesAdapter(categoryList,getContext());
                category_recyclerView.setAdapter(mAdapter);
                notice_card.setVisibility(View.INVISIBLE);
                progressBar.setVisibility(View.INVISIBLE);
                r2.setVisibility(View.VISIBLE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

        featuredbf.orderByChild("in_featured").equalTo(true).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot p : dataSnapshot.getChildren()){
                    Product product = p.getValue(Product.class);
                    featureList.add(product);
                }
                productAdapter = new ProductAdapter(getContext(),featureList);
                feature_recycler_view.setAdapter(productAdapter);
                r1.setVisibility(View.VISIBLE);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getContext(),"inner value problem",Toast.LENGTH_LONG).show();

            }
        });

        bestdbf.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot p : dataSnapshot.getChildren()){
                    Product currentProduct = p.getValue(Product.class);
                    bestList.add(currentProduct);
                }
                productAdapter = new ProductAdapter(getContext(),bestList);
                best_recycler_view.setAdapter(productAdapter);
                r3.setVisibility(View.VISIBLE);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

        cartfab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().beginTransaction().addToBackStack("tag").replace(R.id.fragment_frame,new CartFragment()).commit();
            }
        });
        return view;
    }
}
