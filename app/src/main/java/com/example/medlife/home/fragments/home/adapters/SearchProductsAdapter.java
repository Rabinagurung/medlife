package com.example.medlife.home.fragments.home.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.medlife.R;
import com.example.medlife.api.response.Product;
import com.example.medlife.singleProductPage.SingleProductActivity;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class SearchProductsAdapter extends RecyclerView.Adapter<SearchProductsAdapter.SearchViewHolder> implements Filterable {

    List<Product> productListFull;
    List<Product> searchData;
    LayoutInflater layoutInflater;
    Context context;

    public SearchProductsAdapter(List<Product> productListFull, Context context) {
        this.productListFull = productListFull;
        searchData = new ArrayList<>(productListFull);
        layoutInflater = LayoutInflater.from(context);
        this.context = context;

    }


    @NonNull
    @Override
    public SearchProductsAdapter.SearchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new SearchViewHolder(layoutInflater.inflate(R.layout.search_products, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull SearchProductsAdapter.SearchViewHolder holder, int position) {
        holder.productNameSearch.setText(productListFull.get(position).getName());
        holder.productPriceSearch.setText("Rs. " + productListFull.get(position).getDiscountPrice() +"");
        Picasso.get().load(productListFull.get(position).getImages().get(0)).into(holder.productImageSearch);

        holder.searchLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, SingleProductActivity.class);
                intent.putExtra(SingleProductActivity.DATA_KEY, productListFull.get(holder.getAdapterPosition()));
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return productListFull.size();
    }

    @Override
    public Filter getFilter() {
        return filter;
    }

    private Filter filter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            List<Product> suggestions = new ArrayList<>();
            if (charSequence == null || charSequence.length() == 0){
                suggestions.addAll(searchData);
            }else {
                String filterPattern = charSequence.toString().toLowerCase().trim();
                for (Product item : searchData){
                    if(item.getName().toLowerCase().contains(filterPattern)){
                        suggestions.add(item);

                    }
                }
            }
            FilterResults results = new FilterResults();
            results.values = suggestions;
            return results;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            productListFull.clear();
            productListFull.addAll((List) filterResults.values);
            notifyDataSetChanged();
        }

    };





    public class SearchViewHolder extends RecyclerView.ViewHolder {
        ImageView productImageSearch;
        TextView productNameSearch, productPriceSearch;
        LinearLayout searchLayout;

        public SearchViewHolder(@NonNull View itemView) {
            super(itemView);
            productImageSearch = itemView.findViewById(R.id.productImageSearch);
            productNameSearch = itemView.findViewById(R.id.productNameSearch);
            productPriceSearch = itemView.findViewById(R.id.productPriceSearch);
            searchLayout = itemView.findViewById(R.id.searchLayout);
        }
    }
}

