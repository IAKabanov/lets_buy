package net.sytes.kai_soft.letsbuyka.ProductModel;

import android.content.Context;
import android.content.Intent;
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


import net.sytes.kai_soft.letsbuyka.IFilterContract;
import net.sytes.kai_soft.letsbuyka.IMenuContract;
import net.sytes.kai_soft.letsbuyka.Lists.ListsActivity;
import net.sytes.kai_soft.letsbuyka.R;
/*  Активити со списками продуктов  */
public class ProductsActivity extends AppCompatActivity implements
        IProductListActivityContract{

    DetailFragment detailFragment;      //Фрагмент детализации
    ListFragment listFragment;          //Фрагмент списка
    FragmentManager fragmentManager;    //Фрагмент менеджер
    Toolbar toolBar;
    MenuItem actionSave, actionCancel, actionDelete, actionSearch, actionProduct;
    SearchView searchView;
    IMenuContract iMenuContract;
    IFilterContract iFilterContract;


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
        actionProduct = menu.findItem(R.id.action_products);
        searchView = (SearchView) actionSearch.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (listFragment instanceof IFilterContract) {
                    iFilterContract = (IFilterContract) listFragment;
                    iFilterContract.onFilterMake(newText);
                }

                return false;
            }
        });
        actionProduct.setVisible(true);
        actionProduct.setTitle(R.string.action_products_list);
        searchView.setVisibility(View.VISIBLE);
        return true;
    }

    @Override
    public void onBackPressed() {
        onDetailFragmentButtonClick();
        hideKeyboard();
        super.onBackPressed();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products_list);

        toolBar = findViewById(R.id.toolbarProduct);
        toolBar.setTitle(R.string.products);
        setSupportActionBar(toolBar);



        //Создали фрагменты
        fragmentManager = getSupportFragmentManager();
        detailFragment = new DetailFragment();
        listFragment = new ListFragment();

        //Начинаем транзакцию
        FragmentTransaction ft = fragmentManager.beginTransaction();
        //Создаем и добавляем первый фрагмент
        ft.add(R.id.activityProductsList, listFragment, "listFragment");
        //Подтверждаем операцию
        //ft.addToBackStack("listFragment");
        ft.commit();
    }

    @Override
    public void onListFragmentButtonClick() {


        actionSave.setVisible(true);
        actionCancel.setVisible(true);
        actionDelete.setVisible(false);
        //actionEdit.setVisible(false);
        actionSearch.setVisible(false);
        actionSearch.collapseActionView();
        actionProduct.setVisible(false);
        //searchView.setVisibility(View.GONE);
        toolBar.requestFocus();

        toolBar.setTitle(R.string.emptyProduct);

        // начинаем транзакцию
        FragmentTransaction ft = fragmentManager.beginTransaction();
        // Создаем и добавляем первый фрагмент

        Bundle bundle = new Bundle();
        bundle.putBoolean("editable", true);
        detailFragment.setArguments(bundle);

        ft.replace(R.id.activityProductsList, detailFragment, "detailFragment");
        // Подтверждаем операцию
        ft.addToBackStack("detailFragment");
        ft.commit();
    }

    @Override
    public void onDetailFragmentButtonClick() {

        actionSave.setVisible(false);
        actionCancel.setVisible(false);
        actionDelete.setVisible(false);
        //actionEdit.setVisible(false);
        actionSearch.setVisible(true);
        actionProduct.setVisible(true);
        //searchView.setVisibility(View.VISIBLE);
        toolBar.setTitle(R.string.products);

        toolBar.requestFocus();

        //onBackPressed();
    }



    @Override
    public void onListItemClick(Product product, String className) {

        // начинаем транзакцию
        FragmentTransaction ft = fragmentManager.beginTransaction();
        // Создаем и добавляем первый фрагмент
        //ft.remove(listFragment);

        Bundle bundle = new Bundle();
        bundle.putBoolean("editable", false);
        bundle.putSerializable("product", product);
        detailFragment.setArguments(bundle);

        ft.replace(R.id.activityProductsList, detailFragment, "detailFragment");
        // Подтверждаем операцию
        ft.addToBackStack("detailFragment");
        ft.commit();

        actionSave.setVisible(true);
        actionCancel.setVisible(true);
        actionProduct.setVisible(false);

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

        toolBar.setTitle(product.getName());

        toolBar.requestFocus();

        //Toast.makeText(this, "Pressed " + product.getId() + " ID",
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

            case R.id.action_products:
                Intent intent = new Intent(ProductsActivity.this, ListsActivity.class);
                startActivity(intent);
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

            /*case R.id.action_edit:
                detailFragment.editPressed();
                actionEdit.setVisible(false);
                return true;*/

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void hideKeyboard(){
        InputMethodManager imm = (InputMethodManager) getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(toolBar.getWindowToken(), 0);
    }


    @Override
    public void onLongListItemClick(Product product, String className) {

    }
}
