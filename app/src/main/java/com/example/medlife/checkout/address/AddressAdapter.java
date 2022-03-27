package com.example.medlife.checkout.address;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.example.medlife.R;
import com.example.medlife.api.response.Address;

import java.util.List;

public class AddressAdapter extends RecyclerView.Adapter<AddressAdapter.AddressViewHolder> {
    List<Address> addressList;
    Context context;
    LayoutInflater inflater;
    OnAddressItemClickListener onAddressItemClickListener;
    Boolean isAddress = false;
    Boolean removeEnabled = true;

    public AddressAdapter(List<Address> adressList, Context context) {
        this.addressList = adressList;
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.isAddress = isAddress;
    }

    @NonNull
    @Override
    public AddressViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //new
        if (isAddress)
            return new AddressViewHolder(inflater.inflate(R.layout.item_address, parent, false));
        //new
        else
            return new AddressViewHolder(inflater.inflate(R.layout.item_address, parent, false));
    }

    public void setOnAddressItemClickListener(OnAddressItemClickListener onAddressItemClickListener) {
        this.onAddressItemClickListener = onAddressItemClickListener;
    }

    public void setRemoveEnabled(Boolean removeEnabled) {this.removeEnabled= removeEnabled;};



    @Override
    public void onBindViewHolder(@NonNull AddressViewHolder holder, int position) {
        Address adress = addressList.get(position);
        holder.cityStreetTV.setText(adress.getCity() + " " + adress.getStreet());
        holder.provinceTV.setText(adress.getProvince());
        holder.decTV.setText(adress.getDescription());
        holder.addressLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onAddressItemClickListener.onAddressClick(holder.getAdapterPosition(), addressList.get(holder.getAdapterPosition()));
            }
        });

        if (isAddress) {
            if (removeEnabled)
                holder.removeAddressIV.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        onAddressItemClickListener.onRemoveAddress(holder.getAdapterPosition());

                    }
                });
            else {
                holder.removeAddressIV.setVisibility(View.GONE);
                holder.addressLL.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                setMargins(holder.addressLL, 0,0,16,0);
            }
        }


    }

    public static void setMargins (View v, int l, int t, int r, int b) {
        if (v.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
            ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) v.getLayoutParams();
            p.setMargins(l, t, r, b);
            v.requestLayout();
        }
    }

    @Override
    public int getItemCount() {
        return addressList.size();
    }

    public class AddressViewHolder extends RecyclerView.ViewHolder {
        TextView cityStreetTV, provinceTV, decTV, removeAddressIV;
        LinearLayout addressLL;

        public AddressViewHolder(@NonNull View itemView) {
            super(itemView);
            cityStreetTV = itemView.findViewById(R.id.cityStreetTV);
            provinceTV = itemView.findViewById(R.id.provinceTV);
            decTV = itemView.findViewById(R.id.decTV);
            addressLL = itemView.findViewById(R.id.addressLL);
            if(isAddress) {
                removeAddressIV = itemView.findViewById(R.id.removeAddressIV);
            }
        }
    }

    public interface OnAddressItemClickListener {
        public void onAddressClick(int position, Address adress);
        public void onRemoveAddress(int position);

    }
}
