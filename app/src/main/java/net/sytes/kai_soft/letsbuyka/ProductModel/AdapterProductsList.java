package net.sytes.kai_soft.letsbuyka.ProductModel;

import android.app.Fragment;
import android.content.Context;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import net.sytes.kai_soft.letsbuyka.CustomList.CustomList;
import net.sytes.kai_soft.letsbuyka.CustomList.IListFragment;
import net.sytes.kai_soft.letsbuyka.CustomList.ListCustomFragment;
import net.sytes.kai_soft.letsbuyka.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


/**
 * Created by Лунтя on 07.04.2018.
 */

public class AdapterProductsList extends RecyclerView.Adapter<AdapterProductsList.MyViewHolder> {

    private ArrayList<Product> products;
    private static ArrayList<Integer> deprecatedList;
    static IProductListActivityContract iProductListActivityContract;
    private static String className;
    static IListFragment iListFragment;

    private String TAG = "LModAdapterProductsList";


    public AdapterProductsList(ArrayList<Product> products,
                               @Nullable ArrayList<Integer> deprecatedList,
                               Context context, android.support.v4.app.Fragment fragment, String className) {
        Log.i(TAG, "init()");
        this.products = products;
        this.className = className;
        this.deprecatedList = deprecatedList;
        if (context instanceof IProductListActivityContract) {
            iProductListActivityContract = (IProductListActivityContract) context;
        }

        if (fragment != null) {
            if (fragment instanceof IListFragment) {
                iListFragment = (IListFragment) fragment;
            }
        }

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.i(TAG, "onCreateViewHolder()");
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recyclerview_products_list_item, parent, false);

        return new MyViewHolder(view);
    }


    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Log.i(TAG, "onBindViewHolder()");

        Product product = products.get(position);

       // holder.tvID.setText(String.valueOf(product.getId()));
        holder.tvName.setText(product.getItemName());
        if (product.getDescription() != null && product.getDescription().length() !=0){
            holder.tvDesc.setVisibility(View.VISIBLE);
            holder.tvDesc.setText(product.getDescription());
        }else{
            holder.tvDesc.setVisibility(View.GONE);
        }

       // holder.tvImage.setText(product.getFirstImagePath());
        if (deprecatedList != null) {
            if (deprecatedList.size() > 0) {
                for (int i = 0; i < deprecatedList.size(); i++) {
                    if (deprecatedList.get(i) == product.getId()) {
                        holder.tvName.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
                    }
                }
            }
        }

        //Integer deprecated = deprecatedList.get(position);

    }

    @Override
    public int getItemCount() {
        return this.products.size();
    }


    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,
            View.OnLongClickListener {
        //private TextView tvID;
        private TextView tvName;
        private TextView tvDesc;
        //private TextView tvImage;
        private LinearLayout llItem;
        private boolean longClicked = false;

        MyViewHolder(View itemView) {
            super(itemView);
            Log.i(TAG, "MyViewHolder::getItemCount()");
           // tvID = itemView.findViewById(R.id.tvIDProduct);
            tvName = itemView.findViewById(R.id.tvNameProduct);
            tvDesc = itemView.findViewById(R.id.tvDescProduct);
           // tvImage = itemView.findViewById(R.id.tvImagePathProduct);
            llItem = itemView.findViewById(R.id.recyclerViewListItem);
            llItem.setOnClickListener(this);

            if (className.equals(ListCustomFragment.class.getName())) {
                llItem.setOnLongClickListener(this);
            }


        }

        @Override
        public void onClick(View v) {
            if (longClicked == false) {
                switch (v.getId()) {
                    case (R.id.recyclerViewListItem):
                        Product product = products.get(getAdapterPosition());
                        if (iProductListActivityContract != null) {
                            iProductListActivityContract.onListItemClick(product, className);
                        }
                        if (iListFragment != null) {
                            iListFragment.onItemClick();
                        }

                }
            }
            longClicked = false;

        }

        @Override
        public boolean onLongClick(View v) {
            longClicked = true;
            switch (v.getId()) {
                case (R.id.recyclerViewListItem):
                    if (products.size() > 0) {
                        Product product = products.get(getAdapterPosition());
                        if (iProductListActivityContract != null) {
                            iProductListActivityContract.onLongListItemClick(product, className);
                        }
                        if (iListFragment != null) {
                            iListFragment.onItemClick();
                        }
                    }
            }
            return false;
        }
    }
}