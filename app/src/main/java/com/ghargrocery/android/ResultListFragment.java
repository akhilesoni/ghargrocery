package com.ghargrocery.android;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;


public class ResultListFragment extends Fragment {

    private RecyclerView recyclerView;
    private List<Product> productList;
    private ProductAdapter productAdapter;
    DatabaseReference databaseReference;
    private String category;
    private TextView searchTextView;
    private ProgressBar progressBar;
    private TextView resultTextView;
    public ResultListFragment() {
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_result_list, container, false);
        Bundle bundle = getArguments();
        category = bundle.getString("category");
        resultTextView = v.findViewById(R.id.result_text);
        searchTextView = v.findViewById(R.id.home_search_text);

        progressBar = v.findViewById(R.id.progressBar);
        recyclerView = v.findViewById(R.id.result_recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(),2));
        databaseReference = FirebaseDatabase.getInstance().getReference("products");
        productList = new ArrayList<Product>();
        Query query = databaseReference.orderByChild("product_category").equalTo(category);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    resultTextView.setText("Result showing in"+" "+category+" "+"Category");
                }
                else{
                    resultTextView.setText("No items found in"+" "+category+" "+"Category");


                }
                searchTextView.setText(category);
                for (DataSnapshot p : dataSnapshot.getChildren()){
                    Product product = p.getValue(Product.class);
                    productList.add(product);
                }
                productAdapter = new ProductAdapter(getContext(),productList);
                recyclerView.setAdapter(productAdapter);
                progressBar.setVisibility(View.INVISIBLE);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                progressBar.setVisibility(View.INVISIBLE);
                Toast.makeText(getContext(),"failed",Toast.LENGTH_LONG).show();
            }
        });

        return v;
    }


}