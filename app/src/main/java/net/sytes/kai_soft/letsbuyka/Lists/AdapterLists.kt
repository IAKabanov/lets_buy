package net.sytes.kai_soft.letsbuyka.Lists

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView


import net.sytes.kai_soft.letsbuyka.R
import net.sytes.kai_soft.letsbuyka.IListActivityContract

import java.util.ArrayList

class AdapterLists(_lists: ArrayList<List>, context: Context):
        RecyclerView.Adapter<AdapterLists.Companion.MyViewHolder>() {

    companion object {
        private lateinit var lists: ArrayList<List>
        private lateinit var iListActivityContract: IListActivityContract
        private const val myTag = "letsbuy_LAdapter"

        class MyViewHolder(itemView: View): RecyclerView.ViewHolder(itemView), View.OnClickListener{
            internal val tvName: TextView = itemView.findViewById(R.id.tvNameList)
            private val rlItem: RelativeLayout = itemView.findViewById(R.id.recyclerViewListsListItem)

            init {
                rlItem.setOnClickListener(this)
            }

            override fun onClick(v: View?) {
                val pos = lists[adapterPosition].id
                Log.i(myTag, "AdapterLists -> MyViewHolder -> onClick(position = $pos)")
                iListActivityContract.onListItemClick(pos)
            }
        }
    }
    init {
        lists = _lists
        if (context is IListActivityContract){
            iListActivityContract = context
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): AdapterLists.Companion.MyViewHolder {
        Log.i(myTag, "onCreateViewHolder()")
        val view = LayoutInflater.from(parent!!.context)
                .inflate(R.layout.recyclerview_lists_list, parent, false)
        return AdapterLists.Companion.MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: AdapterLists.Companion.MyViewHolder?, position: Int) {
        Log.i(myTag, "onBindViewHolder()")
        val list = lists[position]
        if (holder != null){
            holder.tvName.text = list.itemName
        }

    }

    override fun getItemCount(): Int {
        Log.i(myTag, "getItemCount()")
        return lists.size
    }
}
