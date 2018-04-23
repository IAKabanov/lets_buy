package net.sytes.kai_soft.letsbuyka.ProductModel;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Лунтя on 14.04.2018.
 */

public class ProductDB extends SQLiteOpenHelper {
    public static final String TABLE_NAME = "products";
    public static final String TABLE_ID = "_ID";
    public static final String TABLE_ITEM_NAME = "name";
    public static final String TABLE_DESCRIPTION = "description";
    public static final String TABLE_PHOTO = "photos";


    public ProductDB(Context context) {
        super(context, TABLE_NAME, null, 1);
    }


/*    public boolean insert(String itemName, String description, String photo){
        ContentValues cv = new ContentValues();

        cv.put(TABLE_ITEM_NAME, itemName);
        cv.put(TABLE_DESCRIPTION, description);
        cv.put(TABLE_PHOTO, photo);


        return true;
    } */


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_NAME + "("
                + TABLE_ID + "integer primary key autoincrement,"
                + TABLE_ITEM_NAME + "text,"
                + TABLE_DESCRIPTION + "text,"
                + TABLE_PHOTO + "text" + ");");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
