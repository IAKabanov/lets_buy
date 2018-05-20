package net.sytes.kai_soft.letsbuyka.ProductModel;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import net.sytes.kai_soft.letsbuyka.R;

/**
 * Created by Лунтя on 14.04.2018.
 */

public class DataBase extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "LetsBuyka.db";

    public static final String TABLE_NAME_PRODUCTS_LIST = "products";
    public static final String TABLE_NAME_LISTS_LIST = "lists";
    public static final String TABLE_NAME_CUSTOM_LIST = "customList";


    private static DataBase instance;
    private static Context mContext;

    private DataBase(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }


/*    public boolean insert(String itemName, String description, String photo){
        ContentValues cv = new ContentValues();

        cv.put(TABLE_ITEM_NAME, itemName);
        cv.put(TABLE_DESCRIPTION, description);
        cv.put(TABLE_PHOTO, photo);


        return true;
    } */

    public static DataBase getInstance(Context context){
        Log.e("DB", "got");
        mContext = context;
        if (instance == null){
            instance = new DataBase(context);
        }
        return instance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.e("DB", "created");

        DataBase.tableProducts.createTableProducts(db);


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.e("DB", "upgraded");
    }

    public static class tableProducts{
        public static final String TABLE_ID = "_ID";
        public static final String TABLE_ITEM_NAME = "name";
        public static final String TABLE_DESCRIPTION = "description";
        public static final String TABLE_PHOTO = "photos";

        private static void createTableProducts(SQLiteDatabase db){
            db.execSQL("create table " + TABLE_NAME_PRODUCTS_LIST + " ( "
                    + DataBase.tableProducts.TABLE_ID + " integer primary key autoincrement, "
                    + DataBase.tableProducts.TABLE_ITEM_NAME + " text, "
                    + DataBase.tableProducts.TABLE_DESCRIPTION + " text, "
                    + DataBase.tableProducts.TABLE_PHOTO + " text " + " ); ");
            String[] productsArr = mContext.getResources().getStringArray(R.array.productsList);

            for (String aProductsArr : productsArr) {
                ContentValues cv = new ContentValues();
                cv.put(DataBase.tableProducts.TABLE_ITEM_NAME, aProductsArr);
                db.insert(TABLE_NAME_PRODUCTS_LIST, null, cv);
            }
        }

    }

}
