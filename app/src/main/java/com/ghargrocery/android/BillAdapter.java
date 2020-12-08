package com.ghargrocery.android;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class BillAdapter extends RecyclerView.Adapter<BillAdapter.myViewHolder> {
    private Context context;
    private List<CartProduct> cartProductList;

    public BillAdapter(Context context, List<CartProduct> cartProductList) {
        this.context = context;
        this.cartProductList = cartProductList;
    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(context).inflate(R.layout.bill_card_layout,null,false);
        return new myViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull myViewHolder myViewHolder, int i) {
        CartProduct product = cartProductList.get(i);
        myViewHolder.name.setText(product.getName());
        myViewHolder.prize.setText(product.getPrize());
        myViewHolder.weight.setText(product.getWeight());
        myViewHolder.quantity.setText(product.getQuantity());
        String to = Integer.toString(Integer.parseInt(product.getQuantity())*Integer.parseInt(product.getPrize()));
        myViewHolder.total.setText(to);
    }

    @Override
    public int getItemCount() {
        return cartProductList.size();
    }

    public class myViewHolder extends RecyclerView.ViewHolder{
        public TextView name,prize,total,weight,quantity;
        public myViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            prize = itemView.findViewById(R.id.prize);
            quantity = itemView.findViewById(R.id.quantity);
            weight = itemView.findViewById(R.id.weight);
            total = itemView.findViewById(R.id.total);

        }
    }
}
