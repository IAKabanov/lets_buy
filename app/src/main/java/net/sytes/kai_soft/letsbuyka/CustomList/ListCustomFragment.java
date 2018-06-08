package net.sytes.kai_soft.letsbuyka.CustomList;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import net.sytes.kai_soft.letsbuyka.Application;
import net.sytes.kai_soft.letsbuyka.DataBase;
import net.sytes.kai_soft.letsbuyka.ProductModel.Product;
import net.sytes.kai_soft.letsbuyka.R;

import java.util.ArrayList;

/**
 * Created by Лунтя on 06.06.2018.
 */

public class ListCustomFragment extends Fragment implements View.OnClickListener {
    Button goToDetailBtn;
    DataBase dbProduct;
    RecyclerView recyclerView;
    ICustomListActivityContract iCustomListActivityContract;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_custom_list, container, false);

        goToDetailBtn = rootView.findViewById(R.id.addBtn);
        goToDetailBtn.setOnClickListener(this);

        recyclerView = rootView.findViewById(R.id.rvProductsList);
        dbProduct = Application.getDB(); //new DataBase(getActivity());

        refresh(getArguments().getLong("pos", 0));

        return rootView;
    }

    private void refresh(long pos) {


        SQLiteDatabase db = dbProduct.getWritableDatabase();
        ArrayList<Product> products = new ArrayList<>();


        Cursor c = db.query(DataBase.TABLE_NAME_PRODUCTS_LIST, null,
                DataBase.tableProducts.TABLE_ID + " = ?",
                getIDForList(pos,db), null, null, DataBase.tableProducts.TABLE_ITEM_NAME);

        if (c.moveToFirst()) {

            // определяем номера столбцов по имени в выборке
            int idColIndex = c.getColumnIndex(DataBase.tableProducts.TABLE_ID);
            int nameColIndex = c.getColumnIndex(DataBase.tableProducts.TABLE_ITEM_NAME);
            int descColIndex = c.getColumnIndex(DataBase.tableProducts.TABLE_DESCRIPTION);
            int photoColIndex = c.getColumnIndex(DataBase.tableProducts.TABLE_PHOTO);


            do {
                products.add(new Product(c.getInt(idColIndex), c.getString(nameColIndex),
                        c.getString(descColIndex), c.getString(photoColIndex)));
                // переход на следующую строку
                // а если следующей нет (текущая - последняя), то false - выходим из цикла
            } while (c.moveToNext());
            //displayRW(products);


        } else
            c.close();
    }

    private String[] getIDForList(long id_list, SQLiteDatabase db) {
        Cursor c = db.query(DataBase.TABLE_NAME_CUSTOM_LIST,
                new String[]{DataBase.tableCustomList.TABLE_ID_PRODUCT},
                DataBase.tableCustomList.TABLE_ID_LIST + " = ?",
                new String[]{String.valueOf(id_list)}, null, null, null);

        if (c.moveToFirst()) {
            int idColIndex = c.getColumnIndex(DataBase.tableCustomList.TABLE_ID_PRODUCT);
            ArrayList<Long> id = new ArrayList<>();

            do {
                id.add(c.getLong(idColIndex));
            } while (c.moveToNext());
            String[] idString = new String[id.size()];
            for (int i = 0; i < id.size(); i++){
               idString[i] = String.valueOf(id.get(i));
            }

            c.close();
            return idString;

        } else
            c.close();

        return new String[0];
    }

    @Override
    public void onClick(View v) {

    }
}
