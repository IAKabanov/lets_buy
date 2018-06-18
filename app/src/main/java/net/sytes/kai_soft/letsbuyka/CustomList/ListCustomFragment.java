package net.sytes.kai_soft.letsbuyka.CustomList;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
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
import net.sytes.kai_soft.letsbuyka.ProductModel.AdapterProductsList;
import net.sytes.kai_soft.letsbuyka.ProductModel.IProductListActivityContract;
import net.sytes.kai_soft.letsbuyka.ProductModel.Product;
import net.sytes.kai_soft.letsbuyka.R;

import java.util.ArrayList;

/**
 * Created by Лунтя on 06.06.2018.
 */

public class ListCustomFragment extends Fragment implements View.OnClickListener, IListFragment {
    FloatingActionButton fabAdd;
    DataBase dbProduct;
    RecyclerView recyclerView;
    ICustomListActivityContract iCustomListActivityContract;
    long currentListID;
    public final String CLASS_NAME = getClass().getName();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_custom_list, container, false);

        fabAdd = rootView.findViewById(R.id.fabAddCustomList);
        fabAdd.setOnClickListener(this);
        //goToDetailBtn = rootView.findViewById(R.id.addBtn);
        //goToDetailBtn.setOnClickListener(this);

        recyclerView = rootView.findViewById(R.id.rvProductsList);
        dbProduct = Application.getDB();

        currentListID = getArguments().getLong("pos", 0);
        refresh(currentListID, null);

        return rootView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof ICustomListActivityContract) {
            iCustomListActivityContract = (ICustomListActivityContract) context;
        }
    }

    @Override
    public void onDetach() {
        iCustomListActivityContract = null;
        super.onDetach();
    }

    private void refresh(long pos, ArrayList<Product> newProducts) {
        SQLiteDatabase db = dbProduct.getWritableDatabase();
        ArrayList<Integer> deprecatedList = getDeprecatedForList(pos, db);

        if (newProducts == null) {
            ArrayList<Product> products = new ArrayList<>();
            String selectionArgs = getIDForList(pos, db);
            if (selectionArgs.length() > 0) {

                Cursor c = db.rawQuery("select * from "
                        + DataBase.TABLE_NAME_PRODUCTS_LIST + " where _id in " + selectionArgs, null);


                if (c.moveToFirst()) {

                    // определяем номера столбцов по имени в выборке
                    int idColIndex = c.getColumnIndex(DataBase.tableProducts.TABLE_ID);
                    int nameColIndex = c.getColumnIndex(DataBase.tableProducts.TABLE_ITEM_NAME);
                    int descColIndex = c.getColumnIndex(DataBase.tableProducts.TABLE_DESCRIPTION);
                    //int photoColIndex = c.getColumnIndex(DataBase.tableProducts.TABLE_PHOTO);


                    do {
                        products.add(new Product(c.getInt(idColIndex), c.getString(nameColIndex),
                                c.getString(descColIndex)));
                        // переход на следующую строку
                        // а если следующей нет (текущая - последняя), то false - выходим из цикла
                    } while (c.moveToNext());
                    //getDeprecatedForList(pos, db);
                } else
                    c.close();
            }
            displayRW(products, deprecatedList);
        } else {
            displayRW(newProducts, deprecatedList);
        }
    }

    public void displayRW(ArrayList<Product> products, ArrayList<Integer> deprecatedList) {

        AdapterProductsList adapter = new AdapterProductsList(products, deprecatedList, getActivity(), this, CLASS_NAME);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        RecyclerView.ItemAnimator itemAnimator = new DefaultItemAnimator();

        recyclerView.setAdapter(adapter);
        //recyclerView.setHasFixedSize(true); // необязательно
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL); // необязательно
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setItemAnimator(itemAnimator);
    }

    private String getIDForList(long id_list, SQLiteDatabase db) {
        Cursor c = db.query(DataBase.TABLE_NAME_CUSTOM_LIST,
                new String[]{DataBase.tableCustomList.TABLE_ID_PRODUCT},
                DataBase.tableCustomList.TABLE_ID_LIST + " = ?",
                new String[]{String.valueOf(id_list)}, null, null,
                DataBase.tableCustomList.TABLE_ID_PRODUCT + " asc");

        if (c.moveToFirst()) {
            int idColIndex = c.getColumnIndex(DataBase.tableCustomList.TABLE_ID_PRODUCT);
            StringBuffer id = new StringBuffer();
            ArrayList<Long> _id = new ArrayList<>();
            id.append("(");
            do {
                id.append(c.getLong(idColIndex));
                id.append(",");
            } while (c.moveToNext());
            id.deleteCharAt(id.lastIndexOf(","));
            id.append(")");

            c.close();
            return id.toString();

        } else
            c.close();

        return "";
    }

    private ArrayList<Integer> getDeprecatedForList(long id_list, SQLiteDatabase db) {
        Cursor c = db.rawQuery("select * from " +
                DataBase.TABLE_NAME_CUSTOM_LIST + " where " + DataBase.tableCustomList.TABLE_ID_LIST +
                " = " + String.valueOf(id_list) + " and " + DataBase.tableCustomList.TABLE_DEPRECATED + " = " +
                String.valueOf(CustomList.DEPRECATED_TRUE), null);

        if (c.moveToFirst()) {
            int deprColIndex = c.getColumnIndex(DataBase.tableCustomList.TABLE_ID_PRODUCT);
            ArrayList<Integer> deprecatedList = new ArrayList<>();
            do {
                deprecatedList.add(c.getInt(deprColIndex));
            } while (c.moveToNext());


            c.close();
            return deprecatedList;

        } else
            c.close();

        return new ArrayList<>();
    }


    @Override
    public void onClick(View v) {
        iCustomListActivityContract.onCustomListFragmentButtonClick();
    }

    @Override
    public void onItemClick() {
        refresh(currentListID, null);
    }

    private void setFilter(String s) {
        if (s.length() == 0) {
            refresh(currentListID, null);
        } else {
            SQLiteDatabase db = dbProduct.getWritableDatabase();
            ArrayList<Product> products = new ArrayList<>();
            String selectionArgs = getIDForList(currentListID, db);

            Cursor c = db.rawQuery("select * from " + DataBase.TABLE_NAME_PRODUCTS_LIST +
                            " where " + DataBase.tableProducts.TABLE_ITEM_NAME + " like '%"
                            + s + "%' and " + DataBase.tableProducts.TABLE_ID +" in " + selectionArgs,
                    null);
            if (c.moveToFirst()) {

                // определяем номера столбцов по имени в выборке
                int idColIndex = c.getColumnIndex(DataBase.tableProducts.TABLE_ID);
                int nameColIndex = c.getColumnIndex(DataBase.tableProducts.TABLE_ITEM_NAME);
                int descColIndex = c.getColumnIndex(DataBase.tableProducts.TABLE_DESCRIPTION);
                //int photoColIndex = c.getColumnIndex(DataBase.tableProducts.TABLE_PHOTO);


                do {
                    products.add(new Product(c.getInt(idColIndex), c.getString(nameColIndex),
                            c.getString(descColIndex)));
                    // переход на следующую строку
                    // а если следующей нет (текущая - последняя), то false - выходим из цикла
                } while (c.moveToNext());


            }
            refresh(currentListID, products);
        }
    }

    @Override
    public void onFilterMake(String newFilter) {
        setFilter(newFilter);
    }
}
