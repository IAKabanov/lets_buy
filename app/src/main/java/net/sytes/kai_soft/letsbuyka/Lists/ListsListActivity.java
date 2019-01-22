package net.sytes.kai_soft.letsbuyka.Lists;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import net.sytes.kai_soft.letsbuyka.CustomList.CustomActivity;
import net.sytes.kai_soft.letsbuyka.IFilterContract;
import net.sytes.kai_soft.letsbuyka.IMenuContract;
import net.sytes.kai_soft.letsbuyka.ProductModel.ProductsActivity;
import net.sytes.kai_soft.letsbuyka.R;
import net.sytes.kai_soft.letsbuyka.IListActivityContract;

import java.util.Objects;
import java.util.Stack;

/**
 * Created by Лунтя on 01.06.2018.
 */
/*  Активити с выбором списка   */
public class ListsListActivity extends AppCompatActivity implements IListActivityContract {

    private static final String tagListFragment = "listFragment";
    private static final String tagDetailFragment = "detailFragment";
    private static final String tagDetailFragmentNew = "detailFragmentNew";

    DetailFragmentList detailFragment;      //Фрагмент детализации
    ListsListFragment listFragment;          //Фрагмент списка
    FragmentManager fragmentManager;    //Фрагмент менеджер
    Toolbar toolBar;
    MenuItem actionSave, actionCancel, actionDelete, actionSearch, actionProduct;
    SearchView searchView;
    IFilterContract iFilterContract;
    IMenuContract iMenuContract;
    Stack<String> nameFragment;
    List list;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        toolBar.setNavigationOnClickListener(v -> onBackPressed());

        getMenuInflater().inflate(R.menu.save_menu, menu);
        actionSave = menu.findItem(R.id.action_save);
        actionCancel = menu.findItem(R.id.action_cancel);
        actionDelete = menu.findItem(R.id.action_delete);
        actionSearch = menu.findItem(R.id.action_search);
        actionProduct = menu.findItem(R.id.action_products);
        searchView = (SearchView) actionSearch.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (listFragment != null) {
                    iFilterContract = listFragment;
                    iFilterContract.onFilterMake(newText);
                }

                return false;
            }
        });
        actionProduct.setVisible(true);
        actionProduct.setTitle(R.string.action_products);
        searchView.setVisibility(View.VISIBLE);
        return true;
    }

    @Override
    public void onBackPressed() {
        //refreshToolbar(nameFragment.peek());
        onDetailFragmentButtonClick();
        hideKeyboard();
        super.onBackPressed();
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lists_list);

        toolBar = findViewById(R.id.toolbarProduct);
        toolBar.setTitle(R.string.lists);
        setSupportActionBar(toolBar);

        //Создали фрагменты
        fragmentManager = getSupportFragmentManager();
        detailFragment = new DetailFragmentList();
        listFragment = new ListsListFragment();

        //Начинаем транзакцию
        FragmentTransaction ft = fragmentManager.beginTransaction();
        //Создаем и добавляем первый фрагмент
        ft.add(R.id.activityListsList, listFragment, tagListFragment);
        //Подтверждаем операцию
        ft.commit();

        nameFragment = new Stack<>();
        nameFragment.push(tagListFragment);
    }

    @Override
    public void onListFragmentButtonClick() {

        // начинаем транзакцию
        FragmentTransaction ft = fragmentManager.beginTransaction();
        // Создаем и добавляем первый фрагмент
        //ft.remove(listFragment);

        Bundle bundle = new Bundle();
        bundle.putBoolean("editable", true);
        detailFragment.setArguments(bundle);
        bundle = null;

        ft.replace(R.id.activityListsList, detailFragment, tagDetailFragmentNew);
        ft.addToBackStack(tagDetailFragmentNew);
        // Подтверждаем операцию
        ft.commit();
        nameFragment.push(tagDetailFragmentNew);
        refreshToolbar();
    }

    @Override
    public void onDetailFragmentButtonClick() {
        //nameFragment.push("listFragment");
        refreshToolbar();

    }

    @Override
    public void onListItemClick(long position) {

        Intent intent = new Intent(ListsListActivity.this, CustomActivity.class);

        intent.putExtra("pos", position);
        startActivity(intent);
    }

    @Override
    public void onListItemLongClick(List list) {

        this.list = list;

        // начинаем транзакцию
        FragmentTransaction ft = fragmentManager.beginTransaction();
        // Создаем и добавляем первый фрагмент
        //ft.remove(listFragment);

        Bundle bundle = new Bundle();
        bundle.putBoolean("editable", false);
        bundle.putSerializable("list", list);
        detailFragment.setArguments(bundle);

        ft.replace(R.id.activityListsList, detailFragment, tagDetailFragment);
        ft.addToBackStack(tagDetailFragment);
        // Подтверждаем операцию
        ft.commit();
        nameFragment.push(tagDetailFragment);
        refreshToolbar();

        /*actionSave.setVisible(true);
        actionCancel.setVisible(true);

        if (detailFragment.isNew()){
            actionDelete.setVisible(false);
            //actionEdit.setVisible(false);
        }else{
            actionDelete.setVisible(true);
            // actionEdit.setVisible(true);
        }
        actionSearch.setVisible(false);
        actionSearch.collapseActionView();
        //searchView.setVisibility(View.GONE);

        toolBar.setTitle(list.getItemName());

        toolBar.requestFocus();*/



        //Toast.makeText(this, "Pressed " + list.getId() + " ID",
        //        Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // получим идентификатор выбранного пункта меню
        int id = item.getItemId();
        // Операции для выбранного пункта меню
        switch (id) {
            case R.id.action_save:
                if (detailFragment != null) {
                    if (detailFragment instanceof IMenuContract) {
                        iMenuContract = (IMenuContract) detailFragment;
                        detailFragment.savePressed();
                        onBackPressed();
                    }
                }
                return true;

            case R.id.action_cancel:
                onBackPressed();
                return true;

            case R.id.action_delete:
                if (detailFragment != null) {
                    if (detailFragment instanceof IMenuContract) {
                        iMenuContract = (IMenuContract) detailFragment;
                        detailFragment.deletePressed();
                        onBackPressed();
                    }
                }
                return true;

            case R.id.action_products:
                Intent intent = new Intent(ListsListActivity.this, ProductsActivity.class);
                startActivity(intent);
                return true;

            /*case R.id.action_edit:
                detailFragment.editPressed();
                actionEdit.setVisible(false);
                return true;*/

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void refreshToolbar() {
        String actualFragment;
        if (nameFragment.size() != 0) {
            actualFragment = nameFragment.pop();

        }else{
            actualFragment = tagListFragment;
        }

        switch (actualFragment) {

            case tagListFragment:
                actionSave.setVisible(false);
                actionCancel.setVisible(false);
                actionDelete.setVisible(false);
                actionSearch.setVisible(true);
                actionSearch.collapseActionView();
                actionProduct.setVisible(true);
                toolBar.requestFocus();
                toolBar.setTitle(R.string.lists);
                break;

            case tagDetailFragment:
                actionSave.setVisible(true);
                actionCancel.setVisible(true);
                actionDelete.setVisible(true);
                actionSearch.setVisible(false);
                actionProduct.setVisible(false);
                actionSearch.collapseActionView();

                toolBar.setTitle(list.getItemName());

                toolBar.requestFocus();
                break;

            case tagDetailFragmentNew:
                actionSave.setVisible(true);
                actionCancel.setVisible(true);
                actionDelete.setVisible(false);
                actionSearch.setVisible(false);
                actionProduct.setVisible(false);
                actionSearch.collapseActionView();
                toolBar.setTitle(R.string.emptyList);
                toolBar.requestFocus();

                break;

        }
    }

    private void hideKeyboard(){
        InputMethodManager imm = (InputMethodManager) getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(toolBar.getWindowToken(), 0);
    }
}
