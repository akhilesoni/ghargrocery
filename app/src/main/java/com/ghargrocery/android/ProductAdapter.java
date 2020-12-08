package com.ghargrocery.android;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.myViewHoler> {
    private Context ctx;
    private List<Product> productList;

    public ProductAdapter(Context ctx, List<Product> productList) {
        this.ctx = ctx;
        this.productList = productList;
    }

    @NonNull
    @Override
    public myViewHoler onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(ctx).inflate(R.layout.product_card_view,null,false);
        return new myViewHoler(v);
    }

    @Override
    public void onBindViewHolder(@NonNull myViewHoler myViewHoler, int i) {
        final Product product = productList.get(i);
        myViewHoler.name.setText(product.getProduct_name());
        myViewHoler.prize.setText(product.getProduct_prize());
        myViewHoler.weight.setText(product.getProduct_weight());
        Glide.with(ctx).load(product.getProduct_img_url()).into(myViewHoler.imageView);
        myViewHoler.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppCompatActivity activity = (AppCompatActivity)v.getContext();
                ProductDetailFragment fragment = new ProductDetailFragment();
                Bundle bundle = new Bundle();
                bundle.putString("ProductId",product.getProduct_id());
                fragment.setArguments(bundle);
                activity.getSupportFragmentManager().beginTransaction().addToBackStack("tag").replace(R.id.fragment_frame,fragment).commit();
            }
        });

    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public class myViewHoler extends RecyclerView.ViewHolder{
        public TextView name,prize,weight;
        public ImageView imageView;
        public View mView;
        public myViewHoler(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.product_name);
            prize = itemView.findViewById(R.id.product_prize);
            weight = itemView.findViewById(R.id.product_weight);
            imageView = itemView.findViewById(R.id.product_image_view);
            mView = itemView;

        }
    }
}
