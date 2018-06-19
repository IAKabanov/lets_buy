package net.sytes.kai_soft.letsbuyka;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.ContactsContract;
import android.util.Log;

/**
 * Created by Лунтя on 14.04.2018.
 */

public class DataBase extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "LetsBuy.db";

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

        DataBase.tableProducts.createTableProducts(db);
        Log.e("DB", "products created");
        DataBase.tableLists.createTableList(db);
        Log.e("DB", "lists created");
        DataBase.tableCustomList.createTableCustomList(db);
        Log.e("DB", "cust list created");


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
        public static void a1(DataBase db){
            SQLiteDatabase bd = db.getWritableDatabase();
            for (int i = 0; i < 5; i++) {
                ContentValues cv = new ContentValues();
                cv.put(tableCustomList.TABLE_ID_LIST, 1);
                cv.put(tableCustomList.TABLE_ID_PRODUCT, i+1);
                bd.insert(TABLE_NAME_CUSTOM_LIST, null, cv);
            }

        }
    }

    public static class tableLists{
        public static final String TABLE_ID = "_ID";
        public static final String TABLE_ITEM_NAME = "name";

        private static void createTableList(SQLiteDatabase db){
            db.execSQL("create table " + TABLE_NAME_LISTS_LIST + " ( "
                    + DataBase.tableLists.TABLE_ID + " integer primary key autoincrement, "
                    + DataBase.tableLists.TABLE_ITEM_NAME + " text "+ " ); ");
        }

    }

    public static class tableCustomList{
        public static final String TABLE_ID = "_id";
        public static final String TABLE_ID_PRODUCT = "id_product";
        public static final String TABLE_ID_LIST = "id_list";
        public static final String TABLE_DEPRECATED = "deprecated";

        private static void createTableCustomList(SQLiteDatabase db){
            db.execSQL("create table " + TABLE_NAME_CUSTOM_LIST + " ( "
                    + DataBase.tableCustomList.TABLE_ID + " integer primary key autoincrement, "
                    + DataBase.tableCustomList.TABLE_ID_PRODUCT + " text, "
                    + DataBase.tableCustomList.TABLE_ID_LIST + " text, " +
                    tableCustomList.TABLE_DEPRECATED + " integer " + " ); ");
        }

    }

}
