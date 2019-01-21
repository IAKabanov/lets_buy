package net.sytes.kai_soft.letsbuyka.Lists;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;


import net.sytes.kai_soft.letsbuyka.ProductModel.Product;
import net.sytes.kai_soft.letsbuyka.R;

import java.util.ArrayList;

/**
 * Created by Лунтя on 02.06.2018.
 */

public class AdapterListsList extends RecyclerView.Adapter<AdapterListsList.MyViewHolder> {

    private static ArrayList<List> lists;
    static IListsListActivityContract iListsListActivityContract;

    public AdapterListsList(ArrayList<List> lists, Context context) {
        this.lists = lists;
        iListsListActivityContract = (IListsListActivityContract) context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recyclerview_lists_list, parent, false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        List list = lists.get(position);

        //holder.tvID.setText(String.valueOf(list.getId()));
        holder.tvName.setText(list.getItemName());
    }

    @Override
    public int getItemCount() {
        return this.lists.size();
    }

    static class  MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,
            View.OnLongClickListener{
       // private TextView tvID;
        private TextView tvName;
        private boolean longClicked = false;

        private RelativeLayout llItem;

        MyViewHolder(View itemView) {
            super(itemView);
            //tvID = itemView.findViewById(R.id.tvIDList);
            tvName = itemView.findViewById(R.id.tvNameList);
            llItem = itemView.findViewById(R.id.recyclerViewListsListItem);
            llItem.setOnClickListener(this);
            llItem.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (longClicked == false) {
                long pos = lists.get(getAdapterPosition()).getId();
                iListsListActivityContract.onListListItemClick(pos);

            }
            longClicked = false;
        }

        @Override
        public boolean onLongClick(View v) {
            longClicked = true;
            List list = lists.get(getAdapterPosition());
            iListsListActivityContract.onListListItemLongClick(list);
            return false;
        }
    }

}
