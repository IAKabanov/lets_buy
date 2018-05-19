package net.sytes.kai_soft.letsbuyka;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import net.sytes.kai_soft.letsbuyka.ProductModel.IProductListActivityContract;
import net.sytes.kai_soft.letsbuyka.ProductModel.Product;
import net.sytes.kai_soft.letsbuyka.ProductModel.ProductDB;

import java.util.ArrayList;

/**
 * Created by Лунтя on 30.04.2018.
 */

public class DetailFragment extends Fragment implements View.OnClickListener {

    Button insertBtn, backToListBtn, deleteBtn;
    EditText etName, etDescr, etPhoto;
    ProductDB dbProduct;
    IProductListActivityContract IProductListActivityContract;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);

        insertBtn = rootView.findViewById(R.id.insertBtn);
        backToListBtn = rootView.findViewById(R.id.backToListBtn);
        deleteBtn = rootView.findViewById(R.id.deleteBtn);

        etName = rootView.findViewById(R.id.etName);
        etDescr = rootView.findViewById(R.id.etDescr);
        etPhoto = rootView.findViewById(R.id.etPhoto);


        //dbProduct = Application.getDB(); //new ProductDB(this.getContext());

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();

        makeEditable(true);

        emptyEditText();

        backToListBtn.setOnClickListener(this);
        insertBtn.setOnClickListener(this);
        deleteBtn.setVisibility(View.GONE);

        Bundle bundle = getArguments();

        if (bundle.getBoolean("editable")) {
            insertBtn.setText(R.string.save);

        } else {
            Product product = (Product) bundle.getSerializable("product");

            etName.setText(product.getItemName());
            etDescr.setText(product.getDescription());
            etPhoto.setText(product.getFirstImagePath());

            makeEditable(false);

            insertBtn.setText(R.string.edit);

            deleteBtn.setOnClickListener(this);
            deleteBtn.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof IProductListActivityContract) {
            IProductListActivityContract = (IProductListActivityContract) context;
        }
    }

    @Override
    public void onDetach() {
        IProductListActivityContract = null;
        super.onDetach();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case (R.id.insertBtn):
                insertBtn.setText(R.string.save);
                if (isEditable() == false) {
                    makeEditable(true);
                } else {
                    if (isNew() == false) {
                        Bundle bundle = getArguments();
                        updateTable((Product) bundle.getSerializable("product"));
                        IProductListActivityContract.onDetailFragmentButtonClick();
                    } else {
                        insertToTable();
                        IProductListActivityContract.onDetailFragmentButtonClick();
                    }
                }
                break;
            case (R.id.backToListBtn):
                IProductListActivityContract.onDetailFragmentButtonClick();
                break;
            case (R.id.deleteBtn):
                Bundle bundle = getArguments();
                deleteItem((Product) bundle.getSerializable("product"));
                IProductListActivityContract.onDetailFragmentButtonClick();
                break;
        }
    }

    private boolean isEditable() {
        return etName.isEnabled();
    }

    private boolean isNew() {
        Bundle bundle = getArguments();
        if (bundle.getBoolean("editable") == true) {
            return true;
        }
        return false;
    }

    private void makeEditable(boolean state) {
        etName.setEnabled(state);
        etDescr.setEnabled(state);
        etPhoto.setEnabled(state);
    }

    private void emptyEditText() {
        etName.setText("");
        etDescr.setText("");
        etPhoto.setText("");
    }

    private void insertToTable() {
        ContentValues cv = new ContentValues();

        String name = etName.getText().toString();
        String descr = etDescr.getText().toString();
        String photo = etPhoto.getText().toString();

        dbProduct = Application.getDB();

        SQLiteDatabase db = dbProduct.getWritableDatabase();

        cv.put(ProductDB.TABLE_ITEM_NAME, name);
        cv.put(ProductDB.TABLE_DESCRIPTION, descr);
        cv.put(ProductDB.TABLE_PHOTO, photo);
        // вставляем запись и получаем ее ID
        db.insert(ProductDB.TABLE_NAME, null, cv);
    }

    private void updateTable(Product product) {
        ContentValues cv = new ContentValues();

        String name = etName.getText().toString();
        String descr = etDescr.getText().toString();
        String photo = etPhoto.getText().toString();

        dbProduct = Application.getDB();

        SQLiteDatabase db = dbProduct.getWritableDatabase();

        cv.put(ProductDB.TABLE_ITEM_NAME, name);
        cv.put(ProductDB.TABLE_DESCRIPTION, descr);
        cv.put(ProductDB.TABLE_PHOTO, photo);
        // вставляем запись и получаем ее ID
        //db.insert(ProductDB.TABLE_NAME, null, cv);
        db.update(ProductDB.TABLE_NAME, cv, ProductDB.TABLE_ID + " = ?",
                new String[]{String.valueOf(product.getId())});
    }

    private void deleteItem(Product product){
        dbProduct = Application.getDB();

        SQLiteDatabase db = dbProduct.getWritableDatabase();

        db.delete(ProductDB.TABLE_NAME,ProductDB.TABLE_ID + " = ?",
                 new String[]{String.valueOf(product.getId())});
    }

}