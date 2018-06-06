package net.sytes.kai_soft.letsbuyka.Lists;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import net.sytes.kai_soft.letsbuyka.R;

/**
 * Created by Лунтя on 01.06.2018.
 */

public class ListsListActivity extends AppCompatActivity {
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
}
