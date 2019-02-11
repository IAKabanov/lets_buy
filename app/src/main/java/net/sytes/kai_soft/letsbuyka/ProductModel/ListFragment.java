package net.sytes.kai_soft.letsbuyka.ProductModel;

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
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import net.sytes.kai_soft.letsbuyka.Application;
import net.sytes.kai_soft.letsbuyka.Constants;
import net.sytes.kai_soft.letsbuyka.DataBase;
import net.sytes.kai_soft.letsbuyka.IFilterContract;
import net.sytes.kai_soft.letsbuyka.R;
import net.sytes.kai_soft.letsbuyka.SwipeController;
import net.sytes.kai_soft.letsbuyka.SwipeControllerActions;

import java.util.ArrayList;

/**
 * Created by Лунтя on 07.04.2018.
 */

public class ListFragment extends Fragment implements View.OnClickListener, IFilterContract {

   // EditText filter;
    DataBase dbProduct;
    TextView emptyList;
    RecyclerView recyclerView;
    IProductListActivityContract iProductListActivityContract;
    FloatingActionButton fabAdd;
    public final String CLASS_NAME = getClass().getName();

    SwipeController swipeController;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_list, container, false);

        fabAdd = rootView.findViewById(R.id.fabAdd);
        fabAdd.setOnClickListener(this);
        fabAdd.requestFocus();

        emptyList = rootView.findViewById(R.id.emptyList);

        recyclerView = rootView.findViewById(R.id.rvProductsList);
        dbProduct = Application.Companion.getDB(); //new DataBase(getActivity());

        refresh(null);

        return rootView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof IProductListActivityContract) {
            iProductListActivityContract = (IProductListActivityContract) context;
        }
    }

    @Override
    public void onDetach() {
        iProductListActivityContract = null;
        super.onDetach();
    }

    private void refresh(@Nullable ArrayList<Product> newProducts) {

        if (newProducts == null) {
            SQLiteDatabase db = dbProduct.getWritableDatabase();
            ArrayList<Product> products = new ArrayList<>();

            Cursor c = db.query(Constants.TABLE_NAME_PRODUCTS, null, null,
                    null, null, null, Constants.TABLE_ITEM_NAME);
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

            } else {
                c.close();
            }
            if (products.size() == 0){
                emptyList.setVisibility(View.VISIBLE);
            }else{
                emptyList.setVisibility(View.GONE);
            }
            displayRW(products);
        } else {
            displayRW(newProducts);
        }

    }

    public void displayRW(ArrayList<Product> products) {

        AdapterProductsList adapter = new AdapterProductsList(products, null, getActivity(), null, CLASS_NAME);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        RecyclerView.ItemAnimator itemAnimator = new DefaultItemAnimator();

        recyclerView.setAdapter(adapter);
        swipeController = new SwipeController(new SwipeControllerActions() {
            @Override
            public void onLeftSwiped(int position) {
                int a;
                super.onLeftSwiped(position);
            }

            @Override
            public void onRightSwiped(int position) {
                int a;
                super.onRightSwiped(position);
            }
        }, getContext());

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(swipeController);
        itemTouchHelper.attachToRecyclerView(recyclerView);

        //recyclerView.setHasFixedSize(true); // необязательно
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL); // необязательно
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setItemAnimator(itemAnimator);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case (R.id.fabAdd):
                iProductListActivityContract.onListFragmentButtonClick();
                fabAdd.requestFocus();
                break;
        }
    }

    private void setFilter(String s){
        if (s.length() == 0) {
            refresh(null);
        } else {
            SQLiteDatabase db = dbProduct.getWritableDatabase();
            ArrayList<Product> products = new ArrayList<>();

            Cursor c = db.rawQuery("select * from " + Constants.TABLE_NAME_PRODUCTS +
                            " where " + Constants.TABLE_ITEM_NAME + " like '%"
                            + s + "%'",
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
            refresh(products);
        }
    }

   /* private void initFilter() {
        filter.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
            }

            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                if (s.length() == 0) {
                    refresh(null);
                } else {
                    SQLiteDatabase db = dbProduct.getWritableDatabase();
                    ArrayList<Product> products = new ArrayList<>();


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


                    }
                    refresh(products);
                }
                }



        });



    }
*/
    @Override
    public void onFilterMake(String s) {
        setFilter(s);
    }
}
