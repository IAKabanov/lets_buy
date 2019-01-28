package net.sytes.kai_soft.letsbuyka.Lists

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.SearchView
import android.support.v7.widget.Toolbar
import android.view.MenuItem

import net.sytes.kai_soft.letsbuyka.IFilterContract
import net.sytes.kai_soft.letsbuyka.R
import net.sytes.kai_soft.letsbuyka.IListActivityContract

import java.util.Stack

class ListsActivity: AppCompatActivity(), IListActivityContract{

    companion object {
        const val tagListFragment = "listFragment"
        const val tagDetailFragment = "detailFragment"
        const val tagDetailFragmentNew = "detailFragmentNew"
    }

    private lateinit var detailFragment: DetailFragmentList    //Фрагмент детализации
    private lateinit var listFragment: ListsListFragment          //Фрагмент списка
    private lateinit var fragmentManager: android.support.v4.app.FragmentManager    //Фрагмент менеджер
    private lateinit var toolbar: Toolbar
    private lateinit var actionSave: MenuItem
    private lateinit var actionCancel: MenuItem
    private lateinit var actionDelete: MenuItem
    private lateinit var actionSearch: MenuItem
    private lateinit var actionProduct: MenuItem
    private lateinit var searchView: SearchView
    private lateinit var iFilterContract: IFilterContract
    private lateinit var nameFragment: Stack<String>
    private lateinit var list: List

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lists)
    }

    override fun onListFragmentButtonClick() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onDetailFragmentButtonClick() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onListItemClick(position: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onListItemLongClick(list: List) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}