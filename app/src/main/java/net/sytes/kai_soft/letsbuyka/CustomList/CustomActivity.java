package net.sytes.kai_soft.letsbuyka.CustomList;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;


import net.sytes.kai_soft.letsbuyka.Application;
import net.sytes.kai_soft.letsbuyka.CRUDdb;
import net.sytes.kai_soft.letsbuyka.DataBase;
import net.sytes.kai_soft.letsbuyka.Lists.IListsListContract;
import net.sytes.kai_soft.letsbuyka.Lists.List;
import net.sytes.kai_soft.letsbuyka.ProductModel.DetailFragment;
import net.sytes.kai_soft.letsbuyka.ProductModel.IProductDetailContract;
import net.sytes.kai_soft.letsbuyka.ProductModel.IProductListActivityContract;
import net.sytes.kai_soft.letsbuyka.ProductModel.IProductListContract;
import net.sytes.kai_soft.letsbuyka.ProductModel.ListFragment;
import net.sytes.kai_soft.letsbuyka.ProductModel.Product;
import net.sytes.kai_soft.letsbuyka.R;

import java.util.Stack;

/**
 * Created by Лунтя on 06.06.2018.
 */

public class CustomActivity extends AppCompatActivity implements IProductListActivityContract,
        ICustomListActivityContract {

    //DetailCustomFragment detailFragment;      //Фрагмент детализации
    ListCustomFragment listFragment;          //Фрагмент списка
    ListFragment listProduct;
    DetailFragment detailProduct;
    FragmentManager fragmentManager;    //Фрагмент менеджер
    Toolbar toolBar;
    MenuItem actionSave, actionCancel, actionDelete, actionSearch;//, actionEdit;
    SearchView searchView;
    Stack<String> nameFragment;
    IListFragment iListFragment;
    IProductDetailContract iProductDetailContract;
    IProductListContract iProductListContract;


    long id_list;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        getMenuInflater().inflate(R.menu.save_menu, menu);
        actionSave = menu.findItem(R.id.action_save);
        actionCancel = menu.findItem(R.id.action_cancel);
        actionDelete = menu.findItem(R.id.action_delete);
        //actionEdit = menu.findItem(R.id.action_edit);
        actionSearch = menu.findItem(R.id.action_search);
        searchView = (SearchView) actionSearch.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (nameFragment.peek().equals("listProduct")){
                    if (listProduct instanceof IProductListContract) {
                        iProductListContract = (IProductListContract) listProduct;
                        iProductListContract.onFilterMake(newText);
                    }
                }
                if (nameFragment.peek().equals("listFragment")){
                    if (listFragment instanceof IListFragment) {
                        iListFragment = (IListFragment) listFragment;
                        iListFragment.onFilterMake(newText);
                    }
                }




                return false;
            }
        });
        searchView.setVisibility(View.VISIBLE);
        return true;
    }

    @Override
    public void onBackPressed() {
        nameFragment.pop();
        refreshToolbar();
        //onListDetailFragmentButtonClick();
        hideKeyboard();
        super.onBackPressed();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_list);


        //Создали фрагменты
        fragmentManager = getSupportFragmentManager();
        listFragment = new ListCustomFragment();
        detailProduct = new DetailFragment();

        listFragment.setArguments(getIntent().getExtras());
        id_list = getIntent().getExtras().getLong("pos", 1);

        //Начинаем транзакцию
        FragmentTransaction ft = fragmentManager.beginTransaction();
        //Создаем и добавляем первый фрагмент
        ft.add(R.id.activityCustomList, listFragment, "listFragment");
        //ft.addToBackStack("listFragment");
        //Подтверждаем операцию
        ft.commit();

        toolBar = findViewById(R.id.toolbarProduct);
        toolBar.setTitle(readListName(id_list));
        setSupportActionBar(toolBar);

        nameFragment = new Stack<>();
        nameFragment.push("listFragment");

    }

    @Override
    public void onListFragmentButtonClick() {
        nameFragment.push("detailProduct");
        refreshToolbar();
        //toolBar.setTitle(R.string.emptyProduct);

        // начинаем транзакцию
        FragmentTransaction ft = fragmentManager.beginTransaction();
        // Создаем и добавляем первый фрагмент

        Bundle bundle = new Bundle();
        bundle.putBoolean("editable", true);
        detailProduct.setArguments(bundle);

        ft.replace(R.id.activityCustomList, detailProduct, "detailProduct");
        // Подтверждаем операцию
        ft.addToBackStack("detailProduct");
        ft.commit();

    }

    @Override
    public void onDetailFragmentButtonClick() {

    }

    @Override
    public void onListItemClick(Product product, String className) {
        if (className.equals(ListFragment.class.getName())) {
            CRUDdb.insertToTableCustomList(id_list, product.getId());

            /*fragmentManager = getSupportFragmentManager();
            //detailFragment = new DetailCustomFragment();
            listFragment = new ListCustomFragment();

            Bundle bundle = new Bundle();
            bundle.putLong("pos", id_list);
            listFragment.setArguments(bundle);
            //Начинаем транзакцию
            FragmentTransaction ft = fragmentManager.beginTransaction();
            //Создаем и добавляем первый фрагмент
            ft.replace(R.id.activityCustomList, listFragment, "listFragment");
            //Подтверждаем операцию
            ft.commit();*/
            onBackPressed();

        } else if (className.equals(ListCustomFragment.class.getName())) {
            long id = findIdByListProduct(id_list, product.getId());
            CRUDdb.makeDeprecated(id);
        }
    }

    @Override
    public void onLongListItemClick(Product product, String className) {
        if (className.equals(ListCustomFragment.class.getName())) {
            long id = findIdByListProduct(id_list, product.getId());
            CRUDdb.deleteItemCustomList(id);
        }
    }

    public void refreshToolbar() {
        String actualFragment;
        if (nameFragment.size() != 0) {
            actualFragment = nameFragment.peek();

        } else {
            actualFragment = "listFragment";
        }

        switch (actualFragment) {

            case "listFragment":
                actionSave.setVisible(false);
                actionCancel.setVisible(false);
                actionDelete.setVisible(false);
                actionSearch.setVisible(true);
                actionSearch.collapseActionView();
                toolBar.requestFocus();
                toolBar.setTitle(readListName(id_list));
                break;

            case "listProduct":
                actionSave.setVisible(false);
                actionCancel.setVisible(false);
                actionDelete.setVisible(false);
                actionSearch.setVisible(true);
                actionSearch.collapseActionView();
                toolBar.requestFocus();
                toolBar.setTitle(R.string.products);
                break;

            case "detailProduct":
                actionSave.setVisible(true);
                actionCancel.setVisible(true);
                actionDelete.setVisible(false);
                actionSearch.setVisible(false);
                actionSearch.collapseActionView();
                toolBar.setTitle(R.string.emptyProduct);
                toolBar.requestFocus();
                break;

        }
    }

    private void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(toolBar.getWindowToken(), 0);
    }


    @Override
    public void onCustomListFragmentButtonClick() {

        fragmentManager = getSupportFragmentManager();
        //detailFragment = new DetailCustomFragment();
        listProduct = new ListFragment();
        //Начинаем транзакцию
        FragmentTransaction ft = fragmentManager.beginTransaction();
        //Создаем и добавляем первый фрагмент
        ft.replace(R.id.activityCustomList, listProduct, "listProduct");
        ft.addToBackStack("listProduct");
        //(R.id.activityCustomList, listProduct, "listProduct");
        //Подтверждаем операцию
        ft.commit();

        nameFragment.push("listProduct");
        refreshToolbar();
    }

    @Override
    public void onCustomDetailFragmentButtonClick() {

    }

    @Override
    public void onCustomListItemClick(Product product, String className) {

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // получим идентификатор выбранного пункта меню
        int id = item.getItemId();
        // Операции для выбранного пункта меню
        switch (id) {
            case R.id.action_save:
                if (detailProduct != null) {
                    if (detailProduct instanceof IProductDetailContract) {
                        iProductDetailContract = (IProductDetailContract) detailProduct;
                        detailProduct.savePressed();
                        onBackPressed();
                    }
                }
                return true;

            case R.id.action_cancel:
                onBackPressed();
                return true;

            case R.id.action_delete:
                if (detailProduct != null) {
                    if (detailProduct instanceof IProductDetailContract) {
                        iProductDetailContract = (IProductDetailContract) detailProduct;
                        detailProduct.deletePressed();
                        onBackPressed();
                    }
                }
                return true;

            /*case R.id.action_edit:
                detailFragment.editPressed();
                actionEdit.setVisible(false);
                return true;*/

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public static String readListName(long id) {

        //db = Application.getDB(); //Во имя чего?

        SQLiteDatabase db = Application.getDB().getWritableDatabase();

        Cursor c = db.rawQuery("Select * from "
                + DataBase.TABLE_NAME_LISTS_LIST + " where " + DataBase.tableLists.TABLE_ID + " = "
                + String.valueOf(id), null);
        int nameIndex = c.getColumnIndex(DataBase.tableLists.TABLE_ITEM_NAME);
        if(c.moveToFirst()) {
            return c.getString(nameIndex);
        }
        c.close();
        return "0";
    }

    /*  Вспомогательная функция для поиска id в custom list по id списка и id продукта*/
    public static long findIdByListProduct(long id_list, long id_product) {
        SQLiteDatabase SQLdb = Application.getDB().getWritableDatabase();

        Cursor c = SQLdb.rawQuery("select * from "
                        + DataBase.TABLE_NAME_CUSTOM_LIST + " where "
                        + DataBase.tableCustomList.TABLE_ID_PRODUCT + " = "
                        + String.valueOf(id_product) + " and "
                        + DataBase.tableCustomList.TABLE_ID_LIST + " = " + String.valueOf(id_list)
                , null);
        int idColIndex = c.getColumnIndex(DataBase.tableCustomList.TABLE_ID);
        if (c.moveToFirst()){
            return c.getLong(idColIndex);
        }
        c.close();
        return 0;
    }

}
