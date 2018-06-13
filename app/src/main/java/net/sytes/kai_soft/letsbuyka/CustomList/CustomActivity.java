package net.sytes.kai_soft.letsbuyka.CustomList;

import android.os.Bundle;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;


import net.sytes.kai_soft.letsbuyka.CRUDdb;
import net.sytes.kai_soft.letsbuyka.ProductModel.IProductListActivityContract;
import net.sytes.kai_soft.letsbuyka.ProductModel.ListFragment;
import net.sytes.kai_soft.letsbuyka.ProductModel.Product;
import net.sytes.kai_soft.letsbuyka.R;

/**
 * Created by Лунтя on 06.06.2018.
 */

public class CustomActivity extends AppCompatActivity implements IProductListActivityContract {

    //DetailCustomFragment detailFragment;      //Фрагмент детализации
    ListCustomFragment listFragment;          //Фрагмент списка
    ListFragment listProduct;
    FragmentManager fragmentManager;    //Фрагмент менеджер
    long id_list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_list);

        //Создали фрагменты
        fragmentManager = getSupportFragmentManager();
        listFragment = new ListCustomFragment();

        listFragment.setArguments(getIntent().getExtras());
        id_list = getIntent().getExtras().getLong("pos", 1);
        //Начинаем транзакцию
        FragmentTransaction ft = fragmentManager.beginTransaction();
        //Создаем и добавляем первый фрагмент
        ft.add(R.id.activityCustomList, listFragment, "listFragment");
        //Подтверждаем операцию
        ft.commit();
    }

    @Override
    public void onListFragmentButtonClick() {
        fragmentManager = getSupportFragmentManager();
        //detailFragment = new DetailCustomFragment();
        listProduct = new ListFragment();


        //Начинаем транзакцию
        FragmentTransaction ft = fragmentManager.beginTransaction();
        //Создаем и добавляем первый фрагмент
        ft.replace(R.id.activityCustomList, listProduct, "listProduct");
        //(R.id.activityCustomList, listProduct, "listProduct");
        //Подтверждаем операцию
        ft.commit();
    }

    @Override
    public void onDetailFragmentButtonClick() {

    }

    @Override
    public void onListItemClick(Product product, String className) {

        if (className.equals(ListFragment.class.getName())){
            CRUDdb.insertToTableCustomList(id_list, product.getId());

            fragmentManager = getSupportFragmentManager();
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
            ft.commit();
        } else if (className.equals(ListCustomFragment.class.getName())){
            long id = CRUDdb.findIdByListProduct(id_list, product.getId());
            CRUDdb.makeDeprecated(id);
        }
    }
    @Override
    public void onLongListItemClick(Product product, String className) {
        if (className.equals(ListCustomFragment.class.getName())) {
            long id = CRUDdb.findIdByListProduct(id_list, product.getId());
            CRUDdb.deleteItemCustomList(id);
        }
    }
}
