package com.example.medlife.orderHistory;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.medlife.R;
import com.example.medlife.api.response.OrderHistory;

import java.util.List;

public class OrdersAdapter extends RecyclerView.Adapter<OrdersAdapter.OrderViewHolder> {
    List<OrderHistory> orderHistoryList;
    Context context;
    LayoutInflater layoutInflater;


    public OrdersAdapter(List<OrderHistory> orderHistoryList, Context context) {
        this.orderHistoryList = orderHistoryList;
        this.context = context;
        layoutInflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public OrdersAdapter.OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new OrdersAdapter.OrderViewHolder(layoutInflater.inflate(R.layout.item_orders, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull OrdersAdapter.OrderViewHolder holder, int position) {
        holder.orderNo.setText("#" + orderHistoryList.get(position).getId() +"");
        holder.orderDate.setText(orderHistoryList.get(position).getOrderDateTime());
        holder.productsName.setText(orderHistoryList.get(position).getBag().get(0).getProduct().getProducts().get(0).getrName());
        if (orderHistoryList.get(position).getStatus() == 0){
            holder.status.setText("Pending");
        }else if (orderHistoryList.get(position).getStatus() == 1){
            holder.status.setText("Processing");
        }else
            holder.status.setText("Completed");

        holder.orderLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, OrderDetails.class);
                intent.putExtra(OrderDetails.key, orderHistoryList.get(holder.getAdapterPosition()));
                context.startActivity(intent);
            }

        });
    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class OrderViewHolder extends RecyclerView.ViewHolder {
        TextView orderNo, restaurantName, orderDate, status, productNameTV, quantityTV, PriceTV;
        LinearLayout orderLL;


        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);
            orderNo = itemView.findViewById(R.id.orderNo);
            restaurantName = itemView.findViewById(R.id.restaurantName);
            orderDate = itemView.findViewById(R.id.orderDate);
            status = itemView.findViewById(R.id.status);
            orderLL = itemView.findViewById(R.id.orderLL);
        }
    }
}
