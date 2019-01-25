package net.sytes.kai_soft.letsbuyka.Lists

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v4.app.Fragment
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import android.util.Log
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

/* Lists fragment   */
class ListsListFragment: Fragment(), View.OnClickListener, IFilterContract {

    companion object {
        const val myTag = "letsbuy_listsFragment"
    }

    private lateinit var recyclerView: RecyclerView
    private lateinit var emptyList: TextView
    private lateinit var fabAdd: FloatingActionButton
    private lateinit var iListActivityContract: IListActivityContract
    private lateinit var actualList: ArrayList<List>
    private lateinit var swipeController: SwipeController
    private lateinit var adapter: AdapterListsList

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        Log.i(myTag, "onCreateView()")
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
        Log.i(myTag, "onAttach()")
        if (context is IListActivityContract){
            iListActivityContract = context
        }
        super.onAttach(context)
    }

    /*  It gets list of lists and invokes displayRV method  */
    private fun refresh(newLists: ArrayList<List>? = null){
        if (newLists == null){
            Log.i(myTag, "refresh(null)")
        } else {
            Log.i(myTag, "refresh(newLists)")
        }

        if (newLists == null){
             actualList = CRUDdb.readFromTableLists()

            if (actualList.size == 0){
                emptyList.visibility = View.VISIBLE
            } else {
                emptyList.visibility = View.GONE
            }
            displayRV(actualList)
        } else {
            displayRV(newLists)
        }
    }

    /*  It shows recyclerView with data which refresh method found  */
    private fun displayRV(lists: ArrayList<List>){
        Log.i(myTag, "displayRV()")
        adapter = AdapterListsList(lists, activity)
        val linearLayoutManager = LinearLayoutManager(activity)
        val itemAnimator = DefaultItemAnimator()

        recyclerView.adapter = adapter

        swipeController = SwipeController(object : SwipeControllerActions(){
            override fun onRightSwiped(position: Int) {
                Log.i(myTag, "onRightSwiped()")
                val builder = AlertDialog.Builder(context)
                builder.setTitle(getString(R.string.deleting))
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
                Log.i(myTag, "onLeftSwiped()")
                iListActivityContract.onListItemLongClick(lists[position])
            }
        }, context)

        val itemTouchHelper = ItemTouchHelper(swipeController)
        itemTouchHelper.attachToRecyclerView(recyclerView)

        linearLayoutManager.orientation = LinearLayoutManager.VERTICAL
        recyclerView.layoutManager = linearLayoutManager
        recyclerView.itemAnimator = itemAnimator
    }

    override fun onClick(v: View?) {
        if (v != null){
            when (v.id){
                R.id.fabAddList ->{
                    Log.i(myTag, "onClick(fabAddList)")
                    iListActivityContract.onListFragmentButtonClick()
                }
            }
        }
    }

    override fun onFilterMake(newFilter: String?) {
        Log.i(myTag, "onFilterMake($newFilter)")
        adapter.notifyDataSetChanged()
        if (newFilter != null){
            setFilter(newFilter)
        }

    }

    private fun setFilter(filter: String){
        Log.i(myTag, "onFilterMake($filter)")
        if (filter.isEmpty()){
            refresh()
        } else {
            val lists = CRUDdb.readFromTableLists(filter)
            refresh(lists)
        }
    }
}