package net.sytes.kai_soft.letsbuyka.ProductModel

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
import net.sytes.kai_soft.letsbuyka.IActivityContract
import net.sytes.kai_soft.letsbuyka.SwipeController
import net.sytes.kai_soft.letsbuyka.IFilterContract
import net.sytes.kai_soft.letsbuyka.R
import net.sytes.kai_soft.letsbuyka.SwipeControllerActions

import java.util.ArrayList

class ListFragment: Fragment(), View.OnClickListener, IFilterContract{

    companion object {
        const val myTag = "letsbuy_PRlistFragment"
    }

    private lateinit var emptyList: TextView
    private lateinit var recyclerView: RecyclerView
    private lateinit var fabAdd: FloatingActionButton
    private lateinit var iActivityContract: IActivityContract
    private lateinit var iPRContract: IPRContract
    private lateinit var actualProduct: ArrayList<Product>
    private lateinit var swipeController: SwipeController
    private lateinit var adapter: AdapterProductsList

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        Log.i(myTag, "onCreateView()")
        if (inflater != null){
            val rootView = inflater.inflate(R.layout.fragment_list, container, false)
            fabAdd = rootView.findViewById(R.id.fabAdd)
            fabAdd.setOnClickListener(this)
            emptyList = rootView.findViewById(R.id.emptyList)
            recyclerView = rootView.findViewById(R.id.rvProductsList)
            refresh()
            return rootView
        }
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onAttach(context: Context?) {
        Log.i(myTag, "onAttach()")
        if (context is IActivityContract){
            iActivityContract = context
        }
        if (context is IPRContract){
            iPRContract = context
        }
        super.onAttach(context)
    }

    /*  It gets list of lists and invokes displayRV method  */
    private fun refresh(newLists: ArrayList<Product>? = null){
        if (newLists == null){
            Log.i(myTag, "refresh(null)")

            actualProduct = CRUDdb.readFromTableProducts()

            if (actualProduct.size == 0){
                emptyList.visibility = View.VISIBLE
            } else {
                emptyList.visibility = View.GONE
            }
            displayRV(actualProduct)

        } else {
            Log.i(myTag, "refresh(newLists)")
            displayRV(newLists)
        }
    }

    /*  It shows recyclerView with data which refresh method found  */
    private fun displayRV(products: ArrayList<Product>){
        Log.i(myTag, "displayRV()")
        adapter = AdapterProductsList(products, null, activity)
        val linearLayoutManager = LinearLayoutManager(activity)
        val itemAnimator = DefaultItemAnimator()

        recyclerView.adapter = adapter

        swipeController = SwipeController(object : SwipeControllerActions(){
            override fun onRightSwiped(position: Int) {
                Log.i(myTag, "onRightSwiped()")
                val builder = AlertDialog.Builder(context)
                builder.setTitle(getString(R.string.delete))
                        .setMessage(getString(R.string.deleteProduct))
                        .setPositiveButton(R.string.yes) { _, _ ->
                            adapter.notifyItemRemoved(position)
                            adapter.notifyItemRangeChanged(position, adapter.itemCount)
                            CRUDdb.deleteItemProducts(products[position])
                            products.removeAt(position)

                            if (products.size == 0){
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
                iPRContract.onListItemLongClick(products[position])
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
                    iActivityContract.onListFragmentButtonClick()
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
            val products = CRUDdb.readFromTableProducts(filter)
            refresh(products)
        }
    }

}