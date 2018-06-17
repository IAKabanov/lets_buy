package net.sytes.kai_soft.letsbuyka.ProductModel;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;


import net.sytes.kai_soft.letsbuyka.R;

public class ProductsActivity extends AppCompatActivity implements
        IProductListActivityContract{

    DetailFragment detailFragment;      //Фрагмент детализации
    ListFragment listFragment;          //Фрагмент списка
    FragmentManager fragmentManager;    //Фрагмент менеджер
    Toolbar toolBar;
    MenuItem actionSave, actionCancel, actionDelete, actionEdit, actionSearch;
    SearchView searchView;
    IProductDetailContract iProductDetailContract;
    IProductListContract iProductListContract;


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onDetailFragmentButtonClick();
            }
        });

        getMenuInflater().inflate(R.menu.save_menu, menu);
        actionSave = menu.findItem(R.id.action_save);
        actionCancel = menu.findItem(R.id.action_cancel);
        actionDelete = menu.findItem(R.id.action_delete);
        actionEdit = menu.findItem(R.id.action_edit);
        actionSearch = menu.findItem(R.id.action_search);
        searchView = (SearchView) actionSearch.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (listFragment instanceof IProductListContract) {
                    iProductListContract = (IProductListContract) listFragment;
                    listFragment.onFilterMake(newText);
                }

                return false;
            }
        });
        return true;
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
        actionEdit.setVisible(false);
        actionSearch.setVisible(false);

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
        actionEdit.setVisible(false);
        actionSearch.setVisible(true);
        toolBar.setTitle(R.string.products);
        onBackPressed();

    }



    @Override
    public void onListItemClick(Product product, String className) {

        // начинаем транзакцию
        FragmentTransaction ft = fragmentManager.beginTransaction();
        // Создаем и добавляем первый фрагмент
        ft.remove(listFragment);

        Bundle bundle = new Bundle();
        bundle.putBoolean("editable", false);
        bundle.putSerializable("product", product);
        detailFragment.setArguments(bundle);

        ft.add(R.id.activityProductsList, detailFragment, "detailFragment");
        // Подтверждаем операцию
        ft.addToBackStack("detailFragment");
        ft.commit();

        actionSave.setVisible(true);
        actionCancel.setVisible(true);

        if (detailFragment.isNew()){
            actionDelete.setVisible(false);
            actionEdit.setVisible(false);
        }else{
            actionDelete.setVisible(true);
            actionEdit.setVisible(true);
        }
        actionSearch.setVisible(false);

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
                    if (detailFragment instanceof IProductDetailContract) {
                        iProductDetailContract = (IProductDetailContract) detailFragment;
                        detailFragment.savePressed();
                    }
                }
                return true;

            case R.id.action_cancel:
                onDetailFragmentButtonClick();
                return true;

            case R.id.action_delete:
                if (detailFragment != null) {
                    if (detailFragment instanceof IProductDetailContract) {
                        iProductDetailContract = (IProductDetailContract) detailFragment;
                        detailFragment.deletePressed();
                    }
                }
                return true;

            case R.id.action_edit:
                detailFragment.editPressed();
                actionEdit.setVisible(false);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }


    @Override
    public void onLongListItemClick(Product product, String className) {

    }
}
