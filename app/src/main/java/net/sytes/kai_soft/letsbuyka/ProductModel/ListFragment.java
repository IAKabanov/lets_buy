package net.sytes.kai_soft.letsbuyka.ProductModel;

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
 * Created by Лунтя on 07.04.2018.
 */

public class ListFragment extends Fragment implements View.OnClickListener{

    Button goToDetailBtn;
    DataBase dbProduct;
    RecyclerView recyclerView;
    IProductListActivityContract iProductListActivityContract;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_list, container, false);

        goToDetailBtn = rootView.findViewById(R.id.addBtn);
        goToDetailBtn.setOnClickListener(this);

        recyclerView = rootView.findViewById(R.id.rvProductsList);
        dbProduct = Application.getDB(); //new DataBase(getActivity());

        refresh();

        return rootView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof IProductListActivityContract){
            iProductListActivityContract = (IProductListActivityContract) context;
        }
    }

    @Override
    public void onDetach() {
        iProductListActivityContract = null;
        super.onDetach();
    }

    private void refresh(){
        SQLiteDatabase db = dbProduct.getWritableDatabase();
        ArrayList<Product> products = new ArrayList<>();

        Cursor c = db.query(DataBase.TABLE_NAME_PRODUCTS_LIST, null, null,
                null, null, null, DataBase.tableProducts.TABLE_ITEM_NAME);
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
            displayRW(products);


        } else
            c.close();
    }

    public void displayRW(ArrayList<Product> products){

        AdapterProductsList adapter = new AdapterProductsList(products, getActivity());
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
                iProductListActivityContract.onListFragmentButtonClick();
                break;
        }

    }

}
