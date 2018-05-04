package net.sytes.kai_soft.letsbuyka.ProductModel;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import net.sytes.kai_soft.letsbuyka.DetailFragment;
import net.sytes.kai_soft.letsbuyka.ListFragment;
import net.sytes.kai_soft.letsbuyka.R;

public class ProductsListActivity extends AppCompatActivity implements
        IProductListActivityListContract, IProductListActivityDetailContract {

    DetailFragment detailFragment;
    ListFragment listFragment;
    FragmentManager fragmentManager;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products_list);
        fragmentManager = getSupportFragmentManager();
        detailFragment = new DetailFragment();
        listFragment = new ListFragment();

        // начинаем транзакцию
        FragmentTransaction ft = fragmentManager.beginTransaction();
        // Создаем и добавляем первый фрагмент
        ListFragment listFragment = new ListFragment();
        ft.add(R.id.activityProductsList, listFragment, "listFragment");
        // Подтверждаем операцию
        ft.commit();
    }

    @Override
    public void onListFragmentButtonClick() {
        // начинаем транзакцию
        FragmentTransaction ft = fragmentManager.beginTransaction();
        // Создаем и добавляем первый фрагмент
        ft.remove(listFragment);
        ft.add(R.id.activityProductsList, detailFragment, "detailFragment");
        // Подтверждаем операцию
        ft.commit();


    }

    @Override
    public void onDetailFragmentButtonClick() {
        // начинаем транзакцию
        FragmentTransaction ft = fragmentManager.beginTransaction();
        // Создаем и добавляем первый фрагмент
        ft.remove(detailFragment);
        ft.add(R.id.activityProductsList, listFragment, "listFragment");
        // Подтверждаем операцию
        ft.commit();

    }
}
