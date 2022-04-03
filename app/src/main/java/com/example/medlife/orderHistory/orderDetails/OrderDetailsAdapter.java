package com.example.medlife.orderHistory.orderDetails;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.medlife.R;
import com.example.medlife.api.response.Bag;
import com.squareup.picasso.Picasso;

import java.util.List;

public class OrderDetailsAdapter extends RecyclerView.Adapter<OrderDetailsAdapter.OrderViewHolder> {
    List<Bag> bagList;
    Context context;
    LayoutInflater layoutInflater;


    public OrderDetailsAdapter(List<Bag> bagList, Context context) {
        this.bagList = bagList;
        this.context = context;
        layoutInflater = LayoutInflater.from(context);
    }


    @NonNull
    @Override
    public OrderDetailsAdapter.OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new OrderDetailsAdapter.OrderViewHolder(layoutInflater.inflate(R.layout.item_cart_order, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull OrderDetailsAdapter.OrderViewHolder holder, int position) {
        holder.productNameTV.setText(bagList.get(position).getProduct().getName());
        holder.PriceTV.setText("Rs. " + bagList.get(position).getProduct().getPrice()*bagList.get(position).getQuantity() + "");
        holder.quantityTV.setText(bagList.get(position).getQuantity() + "");
        Picasso.get().load(bagList.get(position).getProduct().getImages().get(0)).into(holder.productI);
    }

    @Override
    public int getItemCount() {
        return bagList.size();
    }
}
