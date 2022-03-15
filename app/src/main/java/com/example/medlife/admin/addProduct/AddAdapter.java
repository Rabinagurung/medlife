package com.example.medlife.admin.addProduct;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.medlife.R;
import com.example.medlife.api.response.Category;

import java.util.List;

public class AddAdapter extends RecyclerView.Adapter<AddAdapter.ViewHolder> {
    LayoutInflater inflater;
    boolean isImage = false;
    List<Uri> uris;
    List<Category> cats;
    OnItemCLick onItemCLick;

    public AddAdapter(boolean isImage, List<Uri> uris, List<Category> cats, Context context, OnItemCLick onItemCLick) {
        this.isImage = isImage;
        this.uris = uris;
        inflater = LayoutInflater.from(context);
        this.cats = cats;
        this.onItemCLick = onItemCLick;
    }


    @NonNull
    @Override
    public AddAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (isImage)
            return new ViewHolder(inflater.inflate(R.layout.item_image, parent, false), isImage);
        else
            return new ViewHolder(inflater.inflate(R.layout.item_add_category, parent, false), isImage);

    }

    @Override
    public void onBindViewHolder(@NonNull AddAdapter.ViewHolder holder, int position) {
        if (isImage) {
            holder.imageView.setImageURI(uris.get(position));
            holder.imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) { onItemCLick.onCLick(holder.getAdapterPosition()); }
            });
        } else {
            holder.textView.setText(cats.get(position).getName());
            holder.textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) { onItemCLick.onCLick(holder.getAdapterPosition()); }
            });
        }

    }

    @Override
    public int getItemCount() {
        if (isImage)
            return uris.size();
        else
            return cats.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView textView;
        boolean isC;

        public ViewHolder(@NonNull View itemView, boolean isC) {
            super(itemView);
            if (isC)
                imageView = itemView.findViewById(R.id.cImg);
            else
                textView = itemView.findViewById(R.id.catNameTV);
        }
    }

    interface OnItemCLick {
        public void onCLick(int position);
    }
}
