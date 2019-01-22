package net.sytes.kai_soft.letsbuyka.Lists

import android.app.AlertDialog
import android.content.Context
import android.graphics.Canvas
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v4.app.Fragment
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

import net.sytes.kai_soft.letsbuyka.CRUDdb
import net.sytes.kai_soft.letsbuyka.IListActivityContract
import net.sytes.kai_soft.letsbuyka.SwipeController
import net.sytes.kai_soft.letsbuyka.IFilterContract
import net.sytes.kai_soft.letsbuyka.R
import net.sytes.kai_soft.letsbuyka.SwipeControllerActions

import java.util.ArrayList

class ListsListFragment: Fragment(), View.OnClickListener, IFilterContract {

    private lateinit var recyclerView: RecyclerView
    private lateinit var emptyList: TextView
    private lateinit var fabAdd: FloatingActionButton
    private lateinit var iListActivityContract: IListActivityContract
    internal lateinit var swipeController: SwipeController

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        if (inflater != null){
            val rootView = inflater.inflate(R.layout.fragment_lists_list, container, false)
            fabAdd = rootView.findViewById(R.id.fabAddList)
            fabAdd.setOnClickListener(this)

            emptyList = rootView.findViewById(R.id.emptyList)

            recyclerView = rootView.findViewById(R.id.rvListsList)

            refresh()

            return rootView
        }
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onAttach(context: Context?) {
        if (context is IListActivityContract){
            iListActivityContract = context
        }
        super.onAttach(context)
    }

    private fun refresh(newLists: ArrayList<List>? = null){
        if (newLists == null){
            val lists = CRUDdb.readFromTableLists(null)

            if (lists.size == 0){
                emptyList.visibility = View.VISIBLE
            } else {
                emptyList.visibility = View.GONE
            }
            displayRV(lists)
        } else {
            displayRV(newLists)
        }
    }

    private fun displayRV(lists: ArrayList<List>){
        val adapter = AdapterListsList(lists, activity)
        val linearLayoutManager = LinearLayoutManager(activity)
        val itemAnimator = DefaultItemAnimator()

        recyclerView.adapter = adapter

        swipeController = SwipeController(object : SwipeControllerActions(){
            override fun onRightSwiped(position: Int) {
                val builder = AlertDialog.Builder(context)
                builder.setTitle(getString(R.string.delete))
                        .setMessage(getString(R.string.deleteList))
                        .setPositiveButton(R.string.yes) { _, _ ->
                            adapter.notifyItemRemoved(position)
                            adapter.notifyItemRangeChanged(position, adapter.itemCount)
                            CRUDdb.deleteItemLists(lists[position])
                            lists.removeAt(position)

                            if (lists.size == 0){
                                emptyList.visibility = View.VISIBLE
                            }else{
                                emptyList.visibility = View.GONE
                            }
                        }
                        .setNegativeButton(R.string.no){ _, _ ->
                            adapter.notifyDataSetChanged()
                        }

                val alert = builder.create()
                alert.show()
            }

            override fun onLeftSwiped(position: Int) {
                iListActivityContract.onListItemLongClick(lists[position])
            }
        })

        recyclerView.addItemDecoration(object : RecyclerView.ItemDecoration() {
            override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State?) {
                swipeController.onDraw(c)
            }
        })

        val itemTouchHelper = ItemTouchHelper(swipeController)
        itemTouchHelper.attachToRecyclerView(recyclerView)

        linearLayoutManager.orientation = LinearLayoutManager.VERTICAL
        recyclerView.layoutManager = linearLayoutManager
        recyclerView.itemAnimator = itemAnimator
        

    }

    override fun onClick(v: View?) {
        if (v != null){
            when (v.id){
                R.id.fabAddList -> iListActivityContract.onListFragmentButtonClick()
            }
        }
    }

    override fun onFilterMake(newFilter: String?) {
        if (newFilter != null){
            setFilter(newFilter)
        }

    }

    private fun setFilter(filter: String){
        if (filter.isEmpty()){
            refresh()
        } else {
            val lists = CRUDdb.readFromTableLists(filter)
            refresh(lists)
        }
    }




}