package net.sytes.kai_soft.letsbuyka.Lists

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.FragmentManager
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.SearchView
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import net.sytes.kai_soft.letsbuyka.*

import net.sytes.kai_soft.letsbuyka.CustomList.CustomActivity
import net.sytes.kai_soft.letsbuyka.ProductModel.ProductsActivity

import java.util.Stack

/*  Activity, which need to interact with lists   */
class ListsActivity : AppCompatActivity(), IActivityContract, ILAListContract {

    companion object {
        const val myTag = "letsbuy_listsActivity"
        const val tagListFragment = "listFragment"
        const val tagDetailFragment = "detailFragment"
        const val tagDetailFragmentNew = "detailFragmentNew"
    }

    private lateinit var detailFragment: DetailFragmentList    //Фрагмент детализации
    private lateinit var listFragment: ListsListFragment          //Фрагмент списка
    private lateinit var logInFragment: LogInFragment
    private lateinit var fragmentManager: FragmentManager    //Фрагмент менеджер
    private lateinit var toolbar: Toolbar
    private lateinit var actionSave: MenuItem
    private lateinit var actionCancel: MenuItem
    private lateinit var actionDelete: MenuItem
    private lateinit var actionSearch: MenuItem
    private lateinit var actionProduct: MenuItem
    private lateinit var actionLogIn: MenuItem
    private lateinit var searchView: SearchView
    private lateinit var iFilterContract: IFilterContract
    private lateinit var iMenuContract: IMenuContract
    private lateinit var nameFragment: Stack<String>
    private lateinit var list: List

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.i(myTag, "onCreate()")
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lists)

        initToolbar()
        initFragments()
        addFirstFragment()
    }

    private fun initToolbar(){
        Log.i(myTag, "initToolbar()")
        toolbar = findViewById(R.id.toolbarProduct)
        setSupportActionBar(toolbar)
    }
    private fun initFragments(){
        Log.i(myTag, "initFragments()")
        fragmentManager = supportFragmentManager
        detailFragment = DetailFragmentList()
        listFragment = ListsListFragment()
        logInFragment = LogInFragment()
    }
    private fun addFirstFragment(){
        Log.i(myTag, "addFirstFragment()")
        val ft = fragmentManager.beginTransaction()
        ft.add(R.id.activityListsList, listFragment, tagListFragment)
        ft.commit()
        nameFragment = Stack()
        nameFragment.push(tagListFragment)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        Log.i(myTag, "onCreateOptionsMenu()")
        if (supportActionBar != null && menu != null) {
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)
            toolbar.setNavigationOnClickListener {
                Log.i(myTag, "toolbar -> onBackPressed()")
                onBackPressed()
            }
            menuInflater.inflate(R.menu.save_menu, menu)

            initMenuItems(menu)

            val temp = menu.findItem(R.id.action_firebase)
            temp.isVisible = true

            refreshToolbar()

            return true
        }
        return super.onCreateOptionsMenu(menu)
    }

    private fun initMenuItems(menu: Menu){
        Log.i(myTag, "initMenuItems()")
        actionSave = menu.findItem(R.id.action_save)
        actionCancel = menu.findItem(R.id.action_cancel)
        actionDelete = menu.findItem(R.id.action_delete)
        actionProduct = menu.findItem(R.id.action_products)
        actionLogIn = menu.findItem(R.id.action_logIn)
        actionSearch = menu.findItem(R.id.action_search)
        searchView = actionSearch.actionView as SearchView
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return true
            }
            override fun onQueryTextChange(newText: String?): Boolean {
                Log.i(myTag, "initMenuItems() -> onQueryTextChange($newText)")
                iFilterContract = listFragment
                iFilterContract.onFilterMake(newText)
                return true
            }
        })
        actionProduct.isVisible = true
        actionProduct.setTitle(R.string.action_products)
        searchView.visibility = View.VISIBLE
    }

    override fun onBackPressed() {
        Log.i(myTag, "onBackPressed()")
        onDetailFragmentButtonClick()
        super.onBackPressed()
    }

    /*  This fun invokes when fab has been clicked   */
    override fun onListFragmentButtonClick() {
        Log.i(myTag, "onListFragmentButtonClick()")
        val ft = fragmentManager.beginTransaction()

        val bundle = Bundle()
        bundle.putBoolean("editable", true)
        detailFragment.arguments = bundle

        ft.replace(R.id.activityListsList, detailFragment, tagDetailFragmentNew)
        ft.addToBackStack(tagDetailFragmentNew)
        ft.commit()
        nameFragment.push(tagDetailFragmentNew)
        refreshToolbar()
    }

    /*  This fun invokes when backButton in detailFragment has been clicked   */
    override fun onDetailFragmentButtonClick() {
        Log.i(myTag, "onDetailFragmentButtonClick()")
        refreshToolbar()
        iMenuContract = detailFragment
        iMenuContract.hideKeyboard()
    }

    /*  This fun invokes when any item on recycler view has been clicked   */
    override fun onListItemClick(position: Int) {
        Log.i(myTag, "onListItemClick($position)")
        val intent = Intent(this@ListsActivity, CustomActivity::class.java)
        intent.putExtra("pos", position)
        startActivity(intent)
    }

    /*  This fun invokes when element has been swiped right to edit   */
    override fun onListItemLongClick(list: List) {
        Log.i(myTag, "onListItemLongClick($list)")
        this.list = list

        val ft = fragmentManager.beginTransaction()
        val bundle = Bundle()
        //bundle.putBoolean("editable", false)
        bundle.putSerializable("list", list)
        detailFragment.arguments = bundle

        ft.replace(R.id.activityListsList, detailFragment, tagDetailFragment)
        ft.addToBackStack(tagDetailFragment)
        ft.commit()

        nameFragment.push(tagDetailFragment)

        refreshToolbar()
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        Log.i(myTag, "onOptionsItemSelected()")
        if (item != null) {
            val id = item.itemId

            when (id) {
                R.id.action_save -> {
                    Log.i(myTag, "onOptionsItemSelected() -> action_save")
                    iMenuContract = detailFragment
                    detailFragment.savePressed()
                    onBackPressed()
                    return true
                }
                R.id.action_cancel -> {
                    Log.i(myTag, "onOptionsItemSelected() -> action_cancel")
                    onBackPressed()
                    return true
                }
                R.id.action_delete -> {
                    Log.i(myTag, "onOptionsItemSelected() -> action_delete")
                    iMenuContract = detailFragment
                    detailFragment.deletePressed()
                    onBackPressed()
                    return true
                }
                R.id.action_products -> {
                    Log.i(myTag, "onOptionsItemSelected() -> action_products")
                    val intent = Intent(this@ListsActivity, ProductsActivity::class.java)
                    refreshToolbar(true)
                    startActivity(intent)
                    return true
                }

                R.id.action_firebase -> {
                    Log.i(myTag, "onOptionsItemSelected() -> action_products")
                    val intent = Intent(this@ListsActivity, FBActivity::class.java)
                    startActivity(intent)
                    return true
                }

                R.id.action_logIn -> {
                    val ft = fragmentManager.beginTransaction()

                    ft.replace(R.id.activityListsList, logInFragment, "logIn")
                    ft.addToBackStack("logIn")
                    ft.commit()
                }

                else -> {
                    Log.w(myTag, "onOptionsItemSelected() -> else")
                    return super.onOptionsItemSelected(item)
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun refreshToolbar(gotoProductsActivity: Boolean = false) {
        Log.i(myTag, "refreshToolbar()")
        if (gotoProductsActivity){
            actionLogIn.isVisible = false
        }
        else{
            val actualFragment = if (nameFragment.size != 0) {
                nameFragment.pop()
            } else {
                tagListFragment
            }
            when (actualFragment) {
                tagListFragment -> {
                    Log.i(myTag, "refreshToolbar() -> list fragment")
                    actionSave.isVisible = false
                    actionCancel.isVisible = false
                    actionDelete.isVisible = false
                    actionSearch.isVisible = true
                    actionProduct.isVisible = true
                    actionLogIn.isVisible = true
                    actionSearch.collapseActionView()
                    toolbar.requestFocus()
                    toolbar.setTitle(R.string.lists)
                }
                tagDetailFragment -> {
                    Log.i(myTag, "refreshToolbar() -> detail fragment")
                    actionSave.isVisible = true
                    actionCancel.isVisible = true
                    actionDelete.isVisible = true
                    actionSearch.isVisible = false
                    actionProduct.isVisible = false
                    actionLogIn.isVisible = false
                    actionSearch.collapseActionView()
                    toolbar.requestFocus()
                    toolbar.title = list.itemName
                }
                tagDetailFragmentNew -> {
                    Log.i(myTag, "refreshToolbar() -> new detail fragment")
                    actionSave.isVisible = true
                    actionCancel.isVisible = true
                    actionDelete.isVisible = false
                    actionSearch.isVisible = false
                    actionProduct.isVisible = false
                    actionLogIn.isVisible = false
                    actionSearch.collapseActionView()
                    toolbar.requestFocus()
                    toolbar.setTitle(R.string.emptyList)
                }
            }
        }

    }
}