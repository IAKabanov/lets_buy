package net.sytes.kai_soft.letsbuyka.Lists;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import net.sytes.kai_soft.letsbuyka.ProductModel.Product;
import net.sytes.kai_soft.letsbuyka.R;

/**
 * Created by Лунтя on 01.06.2018.
 */

public class ListsListActivity extends AppCompatActivity implements IListsListActivityContract{
    DetailFragmentList detailFragment;      //Фрагмент детализации
    ListFragmentList listFragment;          //Фрагмент списка
    FragmentManager fragmentManager;    //Фрагмент менеджер


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lists_list);

        //Создали фрагменты
        fragmentManager = getSupportFragmentManager();
        detailFragment = new DetailFragmentList();
        listFragment = new ListFragmentList();

        //Начинаем транзакцию
        FragmentTransaction ft = fragmentManager.beginTransaction();
        //Создаем и добавляем первый фрагмент
        ft.add(R.id.activityListsList, listFragment, "listFragment");
        //Подтверждаем операцию
        ft.commit();
    }

    @Override
    public void onListListFragmentButtonClick() {
        // начинаем транзакцию
        FragmentTransaction ft = fragmentManager.beginTransaction();
        // Создаем и добавляем первый фрагмент
        ft.remove(listFragment);

        Bundle bundle = new Bundle();
        bundle.putBoolean("editable", true);
        detailFragment.setArguments(bundle);
        bundle = null;

        ft.add(R.id.activityListsList, detailFragment, "detailFragment");
        // Подтверждаем операцию
        ft.commit();
    }

    @Override
    public void onListDetailFragmentButtonClick() {
        // начинаем транзакцию
        FragmentTransaction ft = fragmentManager.beginTransaction();
        // Создаем и добавляем первый фрагмент
        ft.remove(detailFragment);
        ft.add(R.id.activityListsList, listFragment, "listFragment");
        // Подтверждаем операцию
        ft.commit();
    }

    @Override
    public void onListListItemClick(List list) {
        // начинаем транзакцию
        FragmentTransaction ft = fragmentManager.beginTransaction();
        // Создаем и добавляем первый фрагмент
        ft.remove(listFragment);

        Bundle bundle = new Bundle();
        bundle.putBoolean("editable", false);
        bundle.putSerializable("list", list);
        detailFragment.setArguments(bundle);

        ft.add(R.id.activityListsList, detailFragment, "detailFragment");
        // Подтверждаем операцию
        ft.commit();

        //Toast.makeText(this, "Pressed " + list.getId() + " ID",
        //        Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onListListItemLongClick(List list) {
        Toast.makeText(this, "Pressed " + list.getId() + "",
                Toast.LENGTH_SHORT).show();
    }
}
