package com.ghargrocery.android;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.List;

public class CategoriesAdapter extends RecyclerView.Adapter<CategoriesAdapter.myViewHolder> {
    private List<Category> categoriesList;
    private Context ctx;

    public CategoriesAdapter(List<Category> categoriesList, Context ctx) {
        this.categoriesList = categoriesList;
        this.ctx = ctx;
    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(ctx).inflate(R.layout.category_card_layout,null,false);
        return new myViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final myViewHolder myViewHolder, int i) {
        final Category categoryCurrent = categoriesList.get(i);
        myViewHolder.name.setText(categoryCurrent.name);
        Glide.with(ctx).load(categoryCurrent.getIcon_url()).into(myViewHolder.imageView);
        myViewHolder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppCompatActivity activity = (AppCompatActivity)v.getContext();
                ResultListFragment fragment = new ResultListFragment();
                Bundle bundle = new Bundle();
                bundle.putString("category",categoryCurrent.getName());
                fragment.setArguments(bundle);
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.fragment_frame,fragment).addToBackStack("tag").commit();
            }
        });
    }

    @Override
    public int getItemCount() {
        return categoriesList.size();
    }

    public class myViewHolder extends RecyclerView.ViewHolder{
        public ImageView imageView;
        public TextView name;
        public View mView;
        public myViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.home_categories_card_image_view);
            name = itemView.findViewById(R.id.home_categories_card_name);
            mView = itemView;
        }
    }
}
