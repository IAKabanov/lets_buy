package net.sytes.kai_soft.letsbuyka.ProductModel;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import net.sytes.kai_soft.letsbuyka.R;

import java.util.ArrayList;

/**
 * Created by Лунтя on 07.04.2018.
 */

public class AdapterProductsList extends RecyclerView.Adapter<AdapterProductsList.MyViewHolder>
    {

    private ArrayList<Product> products;


    AdapterProductsList(ArrayList<Product> products) {
        this.products = products;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recyclerview_products_list_item, parent, false);

        return new MyViewHolder(view);
    }


    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Product product = products.get(position);

        holder.tvID.setText(String.valueOf(product.getId()));
        holder.tvName.setText(product.getItemName());
        holder.tvDesc.setText(product.getDescription());
        holder.tvImage.setText(product.getFirstImagePath());
    }

        @Override
        public int getItemCount() {
            return this.products.size();
        }


        static class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView tvID;
        private TextView tvName;
        private TextView tvDesc;
        private TextView tvImage;

        MyViewHolder(View itemView) {
            super(itemView);
            tvID = itemView.findViewById(R.id.tvIDProduct);
            tvName = itemView.findViewById(R.id.tvNameProduct);
            tvDesc = itemView.findViewById(R.id.tvDescProduct);
            tvImage = itemView.findViewById(R.id.tvImagePathProduct);
        }
    }
}