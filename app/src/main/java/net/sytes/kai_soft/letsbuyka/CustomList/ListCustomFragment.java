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
import android.widget.TextView;

import net.sytes.kai_soft.letsbuyka.Application;
import net.sytes.kai_soft.letsbuyka.Constants;
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
    TextView emptyListNow;
    DataBase dbProduct;
    RecyclerView recyclerView, recyclerViewDeprecated;
    ICustomListActivityContract iCustomListActivityContract;
    long currentListID;
    public final String CLASS_NAME = getClass().getName();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_custom_list, container, false);

        fabAdd = rootView.findViewById(R.id.fabAddCustomList);
        fabAdd.setOnClickListener(this);
        emptyListNow = rootView.findViewById(R.id.emptyList);
        //goToDetailBtn = rootView.findViewById(R.id.addBtn);
        //goToDetailBtn.setOnClickListener(this);

        recyclerView = rootView.findViewById(R.id.rvProductsList);
        recyclerViewDeprecated = rootView.findViewById(R.id.rvDeprecatedProductsList);
        dbProduct = Application.Companion.getDB();

        currentListID = getArguments().getLong("pos", 0);
        //refresh(currentListID, null);
        refreshProductRW(currentListID, null);
        refreshDeprecatedRW(currentListID, null);

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
        refreshProductRW(pos, newProducts);
        refreshDeprecatedRW(pos, newProducts);

    }

    private void refreshProductRW(long pos, ArrayList<Product> newProducts){
        SQLiteDatabase db = dbProduct.getWritableDatabase();
        if (newProducts == null) {
            ArrayList<Product> products = new ArrayList<>();
            String selectionArgs = getIDForList(pos, db);
            if (selectionArgs.length() > 0) {
                Cursor c = db.rawQuery("select * from "
                        + Constants.TABLE_NAME_PRODUCTS_LIST + " where "
                        + Constants.TABLE_ID
                        + " in " + selectionArgs, null);


                if (c.moveToFirst()) {

                    // определяем номера столбцов по имени в выборке
                    int idColIndex = c.getColumnIndex(Constants.TABLE_ID);
                    int nameColIndex = c.getColumnIndex(Constants.TABLE_ITEM_NAME);
                    int descColIndex = c.getColumnIndex(Constants.TABLE_DESCRIPTION);
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
            if (products.size() == 0){
                emptyListNow.setVisibility(View.VISIBLE);
            } else {
                emptyListNow.setVisibility(View.GONE);
            }
            displayRW(products, null, recyclerView);
        }else {
            displayRW(newProducts, null, recyclerView);
        }
    }

    private void refreshDeprecatedRW(long pos, ArrayList<Product> newProducts){
        SQLiteDatabase db = dbProduct.getWritableDatabase();
        ArrayList<Integer> deprecatedList = getDeprecatedForList(pos, db);
        if (newProducts == null) {
            ArrayList<Product> deprecatedProduct = new ArrayList<>();
            String selectionArgsDeprec = getIDDeprecatedForList(pos, db);
            if (selectionArgsDeprec.length() > 0) {
                Cursor c = db.rawQuery("select * from "
                        + Constants.TABLE_NAME_PRODUCTS_LIST + " where "
                        + Constants.TABLE_ID
                        + " in " + selectionArgsDeprec, null);


                if (c.moveToFirst()) {

                    // определяем номера столбцов по имени в выборке
                    int idColIndex = c.getColumnIndex(Constants.TABLE_ID);
                    int nameColIndex = c.getColumnIndex(Constants.TABLE_ITEM_NAME);
                    int descColIndex = c.getColumnIndex(Constants.TABLE_DESCRIPTION);
                    //int photoColIndex = c.getColumnIndex(DataBase.tableProducts.TABLE_PHOTO);


                    do {
                        deprecatedProduct.add(new Product(c.getInt(idColIndex), c.getString(nameColIndex),
                                c.getString(descColIndex)));
                        // переход на следующую строку
                        // а если следующей нет (текущая - последняя), то false - выходим из цикла
                    } while (c.moveToNext());
                    //getDeprecatedForList(pos, db);
                } else
                    c.close();
            }
            displayRW(deprecatedProduct, deprecatedList, recyclerViewDeprecated);
        } else {
            displayRW(newProducts, deprecatedList, recyclerView);
        }
    }

    public void displayRW(ArrayList<Product> products, ArrayList<Integer> deprecatedList,
                          RecyclerView rv) {

        AdapterProductsList adapter = new AdapterProductsList(products, deprecatedList,
                getActivity(), this, CLASS_NAME);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        RecyclerView.ItemAnimator itemAnimator = new DefaultItemAnimator();

        rv.setAdapter(adapter);
        //recyclerView.setHasFixedSize(true); // необязательно
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL); // необязательно
        rv.setLayoutManager(linearLayoutManager);
        rv.setItemAnimator(itemAnimator);
    }

    private String getIDForList(long id_list, SQLiteDatabase db) {
        Cursor c = db.rawQuery("select * from " + Constants.TABLE_NAME_CUSTOM_LIST + " where "
                        + Constants.TABLE_ID_LIST + " = " + String.valueOf(id_list)
                + " and " + Constants.TABLE_DEPRECATED + " = "
                + String.valueOf(CustomList.DEPRECATED_FALSE), null);

        if (c.moveToFirst()) {
            int idColIndex = c.getColumnIndex(Constants.TABLE_ID_PRODUCT);
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

    private String getIDDeprecatedForList(long id_list, SQLiteDatabase db) {
        Cursor c = db.rawQuery("select * from " + Constants.TABLE_NAME_CUSTOM_LIST + " where "
                + Constants.TABLE_ID_LIST + " = " + String.valueOf(id_list)
                + " and " + Constants.TABLE_DEPRECATED + " = "
                + String.valueOf(CustomList.DEPRECATED_TRUE), null);

        if (c.moveToFirst()) {
            int idColIndex = c.getColumnIndex(Constants.TABLE_ID_PRODUCT);
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
                Constants.TABLE_NAME_CUSTOM_LIST + " where " + Constants.TABLE_ID_LIST +
                " = " + String.valueOf(id_list) + " and " + Constants.TABLE_DEPRECATED + " = " +
                String.valueOf(CustomList.DEPRECATED_TRUE), null);

        if (c.moveToFirst()) {
            int deprColIndex = c.getColumnIndex(Constants.TABLE_ID_PRODUCT);
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

            Cursor c = db.rawQuery("select * from " + Constants.TABLE_NAME_PRODUCTS_LIST +
                            " where " + Constants.TABLE_ITEM_NAME + " like '%"
                            + s + "%' and " + Constants.TABLE_ID +" in " + selectionArgs,
                    null);
            if (c.moveToFirst()) {

                // определяем номера столбцов по имени в выборке
                int idColIndex = c.getColumnIndex(Constants.TABLE_ID);
                int nameColIndex = c.getColumnIndex(Constants.TABLE_ITEM_NAME);
                int descColIndex = c.getColumnIndex(Constants.TABLE_DESCRIPTION);
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
