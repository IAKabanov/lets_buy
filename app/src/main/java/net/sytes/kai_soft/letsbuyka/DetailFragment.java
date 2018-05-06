package net.sytes.kai_soft.letsbuyka;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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

    Button Insert, BackToList;
    EditText etName, etDescr, etPhoto;
    ProductDB dbProduct;
    IProductListActivityContract IProductListActivityContract;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);

        Insert = rootView.findViewById(R.id.insert);
        BackToList = rootView.findViewById(R.id.backToList);

        etName = rootView.findViewById(R.id.etName);
        etDescr = rootView.findViewById(R.id.etDescr);
        etPhoto = rootView.findViewById(R.id.etPhoto);

        dbProduct = new ProductDB(this.getContext());

        Insert.setOnClickListener(this);
        BackToList.setOnClickListener(this);

        return rootView;

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
        ContentValues cv = new ContentValues();

        String name = etName.getText().toString();
        String descr = etDescr.getText().toString();
        String photo = etPhoto.getText().toString();

        SQLiteDatabase db = dbProduct.getWritableDatabase();

        ArrayList<Product> products = new ArrayList<>();

        switch (v.getId()) {
            case (R.id.insert):
                cv.put(ProductDB.TABLE_ITEM_NAME, name);
                cv.put(ProductDB.TABLE_DESCRIPTION, descr);
                cv.put(ProductDB.TABLE_PHOTO, photo);
                // вставляем запись и получаем ее ID
                db.insert(ProductDB.TABLE_NAME, null, cv);
                break;
            case (R.id.backToList):
                IProductListActivityContract.onDetailFragmentButtonClick();
                break;
        }
    }


}