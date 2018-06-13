package net.sytes.kai_soft.letsbuyka.Lists;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import net.sytes.kai_soft.letsbuyka.Application;
import net.sytes.kai_soft.letsbuyka.DataBase;
import net.sytes.kai_soft.letsbuyka.R;

import java.util.ArrayList;

/**
 * Created by Лунтя on 06.06.2018.
 */

public class ListFragmentList extends Fragment implements View.OnClickListener {

    private Button goToDetailBtn;
    private RecyclerView recyclerView;
    private DataBase db;
    IListsListActivityContract iListsListActivityContract;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_lists_list, container, false);

        goToDetailBtn = rootView.findViewById(R.id.addBtn);
        goToDetailBtn.setOnClickListener(this);

        recyclerView = rootView.findViewById(R.id.rvListsList);
        db = Application.getDB(); //new DataBase(getActivity());

        refresh();

        return rootView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof IListsListActivityContract){
            iListsListActivityContract = (IListsListActivityContract) context;
        }
    }

    @Override
    public void onDetach() {
        iListsListActivityContract = null;
        super.onDetach();
    }

    private void refresh(){
        SQLiteDatabase db = this.db.getWritableDatabase();
        ArrayList<List> lists = new ArrayList<>();

        Cursor c = db.query(DataBase.TABLE_NAME_LISTS_LIST, null, null,
                null, null, null,
                DataBase.tableLists.TABLE_ID+ " asc");
        if (c.moveToFirst()) {

            // определяем номера столбцов по имени в выборке
            int idColIndex = c.getColumnIndex(DataBase.tableLists.TABLE_ID);
            int nameColIndex = c.getColumnIndex(DataBase.tableLists.TABLE_ITEM_NAME);
            do {
                lists.add(new List(c.getInt(idColIndex), c.getString(nameColIndex)));
                // переход на следующую строку
                // а если следующей нет (текущая - последняя), то false - выходим из цикла
            } while (c.moveToNext());
            displayRW(lists);

        } else
            c.close();
    }

    public void displayRW(ArrayList<List> lists){

        AdapterListsList adapter = new AdapterListsList(lists, getActivity());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        RecyclerView.ItemAnimator itemAnimator = new DefaultItemAnimator();

        recyclerView.setAdapter(adapter);
        //recyclerView.setHasFixedSize(true); // необязательно
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL); // необязательно
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setItemAnimator(itemAnimator);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case (R.id.addBtn):
                iListsListActivityContract.onListListFragmentButtonClick();
                break;
        }

    }

}
