package net.sytes.kai_soft.letsbuyka.ProductModel;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import net.sytes.kai_soft.letsbuyka.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Лунтя on 07.04.2018.
 */

public class AdapterProductsList extends RecyclerView.Adapter<AdapterProductsList.MyViewHolder> {

    private static ArrayList<Product> products;
    static IProductListActivityContract iProductListActivityContract;

    public AdapterProductsList(ArrayList<Product> products, Context context) {
        this.products = products;
        iProductListActivityContract = (IProductListActivityContract) context;
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


    static class  MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView tvID;
        private TextView tvName;
        private TextView tvDesc;
        private TextView tvImage;
        private LinearLayout llItem;

        MyViewHolder(View itemView) {
            super(itemView);
            tvID = itemView.findViewById(R.id.tvIDProduct);
            tvName = itemView.findViewById(R.id.tvNameProduct);
            tvDesc = itemView.findViewById(R.id.tvDescProduct);
            tvImage = itemView.findViewById(R.id.tvImagePathProduct);
            llItem = itemView.findViewById(R.id.recyclerViewListItem);
            llItem.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Product product = products.get(getAdapterPosition());
            iProductListActivityContract.onListItemClick(product);
        }
    }
}