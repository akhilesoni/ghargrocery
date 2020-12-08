package com.ghargrocery.android;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class CartProductAdapter extends RecyclerView.Adapter<CartProductAdapter.myViewHolder> {
    private Context ctx;
    private List<CartProduct> productList;

    public CartProductAdapter(Context ctx, List<CartProduct> productList) {
        this.ctx = ctx;
        this.productList = productList;
    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(ctx).inflate(R.layout.cart_card_layout,null,true);
        return new myViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final myViewHolder myViewHolder, int i) {
        final CartProduct product = productList.get(i);
        myViewHolder.name.setText(product.getName());
        myViewHolder.prize.setText(product.getPrize());
        myViewHolder.weight.setText(product.getWeight()+" "+"KG");
        myViewHolder.category.setText(product.getCategory());
        myViewHolder.quantity.setText(product.getQuantity());
        Glide.with(ctx).load(product.getImg_url()).into(myViewHolder.imageView);
        myViewHolder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int q = Integer.parseInt(product.getQuantity());
                q = q-1;
                if(q==0){
                    String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                    FirebaseDatabase.getInstance().getReference("users").child(uid).child("cart").child(product.getId()).removeValue();
                }else {

                    String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                    FirebaseDatabase.getInstance().getReference("users").child(uid).child("cart").child(product.getId()).child("quantity").setValue(Integer.toString(q));
                }
            }
        });
        myViewHolder.add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int q = Integer.parseInt(product.getQuantity());
                q =q +1;
                String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                FirebaseDatabase.getInstance().getReference("users").child(uid).child("cart").child(product.getId()).child("quantity").setValue(Integer.toString(q));
            }
        });

    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public class myViewHolder extends RecyclerView.ViewHolder{
        public TextView name,prize,category,weight,quantity;
        public ImageView imageView;
        public TextView button,add;
        public View mView;
        public myViewHolder(@NonNull View itemView) {
            super(itemView);
            mView = itemView;
            name = mView.findViewById(R.id.product_name);
            prize = mView.findViewById(R.id.product_prize);
            weight = mView.findViewById(R.id.product_weight);
            category = mView.findViewById(R.id.product_category);
            quantity = mView.findViewById(R.id.quantity);
            button = mView.findViewById(R.id.delete_button);
            add = mView.findViewById(R.id.add_button);
            imageView = mView.findViewById(R.id.product_image_view);
        }
    }
}
