package com.ghargrocery.android.Admin;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ghargrocery.android.Address;
import com.ghargrocery.android.CartProduct;
import com.ghargrocery.android.Product;
import com.ghargrocery.android.R;
import com.ghargrocery.android.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class OrdersAdapter extends RecyclerView.Adapter<OrdersAdapter.myViewHolder> {
    private Context context;
    private List<Order> orderList;

    public OrdersAdapter(Context context, List<Order> orderList) {
        this.context = context;
        this.orderList = orderList;
    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(context).inflate(R.layout.order_card_layout,null,false);
        return new myViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final myViewHolder myViewHolder, int i) {
        Order order = orderList.get(i);
        myViewHolder.date.setText(order.date);
        FirebaseDatabase.getInstance().getReference("users").child(order.getUser_id()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                Address mAddress = dataSnapshot.child("address").getValue(Address.class);
                String mItems = "items:-" ;
                int mTotal = 0;
                myViewHolder.name.setText(user.getFname());
                myViewHolder.address.setText(mAddress.getFull_name()+", "+mAddress.getHouse_no()+", "+mAddress.getArea()+", "+mAddress.getArea()+", "+mAddress.getCity()+", "+mAddress.getPincode());
                dataSnapshot.child("cart").getChildren();
                for( DataSnapshot p : dataSnapshot.child("cart").getChildren()){
                    CartProduct product = p.getValue(CartProduct.class);
                    mItems = mItems+product.getQuantity()+" "+"X"+" ( "+ product.getName() +" ), ";
                    mTotal = mTotal + Integer.parseInt(product.getQuantity())*Integer.parseInt(product.getPrize());

                }
                myViewHolder.items.setText(mItems);
                myViewHolder.total.setText(Integer.toString(mTotal));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    public class myViewHolder extends RecyclerView.ViewHolder{
        public TextView name,address,total,items,date;
        public myViewHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.name);
            address = itemView.findViewById(R.id.address);
            total = itemView.findViewById(R.id.total);
            items = itemView.findViewById(R.id.items);
            date = itemView.findViewById(R.id.date);

        }
    }
}
