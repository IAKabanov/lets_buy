package net.sytes.kai_soft.letsbuyka.ProductModel;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import net.sytes.kai_soft.letsbuyka.DetailFragment;
import net.sytes.kai_soft.letsbuyka.ListFragment;
import net.sytes.kai_soft.letsbuyka.R;

public class ProductsActivity extends AppCompatActivity implements
        IProductListActivityContract {

    DetailFragment detailFragment;      //Фрагмент детализации
    ListFragment listFragment;          //Фрагмент списка
    FragmentManager fragmentManager;    //Фрагмент менеджер


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products_list);

        //Создали фрагменты
        fragmentManager = getSupportFragmentManager();
        detailFragment = new DetailFragment();
        listFragment = new ListFragment();

        //Начинаем транзакцию
        FragmentTransaction ft = fragmentManager.beginTransaction();
        //Создаем и добавляем первый фрагмент
        ft.add(R.id.activityProductsList, listFragment, "listFragment");
        //Подтверждаем операцию
        ft.commit();
    }

    @Override
    public void onListFragmentButtonClick() {
        // начинаем транзакцию
        FragmentTransaction ft = fragmentManager.beginTransaction();
        // Создаем и добавляем первый фрагмент
        ft.remove(listFragment);

        Bundle bundle = new Bundle();
        bundle.putBoolean("editable", true);
        detailFragment.setArguments(bundle);
        bundle = null;

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


    @Override
    public void onListItemClick(Product product) {
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
        ft.commit();

        Toast.makeText(this, "Pressed " + product.getId() + " ID",
                Toast.LENGTH_SHORT).show();
    }
}
