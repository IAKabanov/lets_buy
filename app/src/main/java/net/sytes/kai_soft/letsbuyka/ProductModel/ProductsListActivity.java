package net.sytes.kai_soft.letsbuyka.ProductModel;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import net.sytes.kai_soft.letsbuyka.R;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class ProductsListActivity extends AppCompatActivity implements View.OnClickListener {

    Button Insert, Refresh;
    EditText etName, etDescr, etPhoto;
    ProductDB dbProduct;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products_list);

        Insert = findViewById(R.id.insert);
        Refresh = findViewById(R.id.refresh);

        etName = findViewById(R.id.etName);
        etDescr = findViewById(R.id.etDescr);
        etPhoto = findViewById(R.id.etPhoto);

        dbProduct = new ProductDB(this);

        Insert.setOnClickListener(this);
        Refresh.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        ContentValues cv = new ContentValues();

        String name = etName.getText().toString();
        String descr = etDescr.getText().toString();
        String photo = etPhoto.getText().toString();

        SQLiteDatabase db = dbProduct.getWritableDatabase();

        ArrayList<Product> products = new ArrayList<>();

        switch (v.getId()){
            case (R.id.insert):
                cv.put(ProductDB.TABLE_ITEM_NAME, name);
                cv.put(ProductDB.TABLE_DESCRIPTION, descr);
                cv.put(ProductDB.TABLE_PHOTO, photo);
                // вставляем запись и получаем ее ID
                db.insert(ProductDB.TABLE_NAME, null, cv);
                break;

            case (R.id.refresh):
                Cursor c = db.query(ProductDB.TABLE_NAME, null, null,
                        null, null, null, null);
                if (c.moveToFirst()) {

                    // определяем номера столбцов по имени в выборке
                    int idColIndex = c.getColumnIndex(ProductDB.TABLE_ID);
                    int nameColIndex = c.getColumnIndex(ProductDB.TABLE_ITEM_NAME);
                    int descColIndex = c.getColumnIndex(ProductDB.TABLE_DESCRIPTION);
                    int photoColIndex = c.getColumnIndex(ProductDB.TABLE_PHOTO);


                    do {
                        products.add(new Product(c.getInt(idColIndex), c.getString(nameColIndex),
                                c.getString(descColIndex), c.getString(photoColIndex)));
                        // переход на следующую строку
                        // а если следующей нет (текущая - последняя), то false - выходим из цикла
                    } while (c.moveToNext());
                    displayRW(products);


                } else
                c.close();
                break;
        }
    }

    public void displayRW(ArrayList<Product> products){
        RecyclerView recyclerView = findViewById(R.id.rvProductsList);
        AdapterProductsList adapter = new AdapterProductsList(products);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        RecyclerView.ItemAnimator itemAnimator = new DefaultItemAnimator();

        recyclerView.setAdapter(adapter);
        //recyclerView.setHasFixedSize(true); // необязательно
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL); // необязательно
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setItemAnimator(itemAnimator);
    }
}
