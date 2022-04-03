package com.example.medlife.orderHistory;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.medlife.R;
import com.example.medlife.api.response.OrderHistory;
import com.example.medlife.orderHistory.orderDetails.OrderDetailsActivity;

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
        holder.orderId.setText("#" + orderHistoryList.get(position).getId() +"");
        holder.orderDate.setText(orderHistoryList.get(position).getOrderDateTime());
        if (orderHistoryList.get(position).getStatus() == 0){
            holder.orderStatus.setText("Pending");
        }else if (orderHistoryList.get(position).getStatus() == 1){
            holder.orderStatus.setText("Processing");
        }else
            holder.orderStatus.setText("Completed");

        holder.orderHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, OrderDetailsActivity.class);
                intent.putExtra(OrderDetailsActivity.key, orderHistoryList.get(holder.getAdapterPosition()));
                context.startActivity(intent);
            }

        });
    }

    @Override
    public int getItemCount() {
        return orderHistoryList.size();
    }

    public class OrderViewHolder extends RecyclerView.ViewHolder {
        TextView orderId, orderDate, orderStatus;
        ConstraintLayout orderHistory;


        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);
            orderId = itemView.findViewById(R.id.orderId);
            orderDate = itemView.findViewById(R.id.orderDate);
            orderStatus = itemView.findViewById(R.id.orderStatus);
            orderHistory = itemView.findViewById(R.id.orderHistory);

        }
    }
}
