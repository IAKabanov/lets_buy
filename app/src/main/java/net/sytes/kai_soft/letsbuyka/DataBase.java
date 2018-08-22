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


/*  Синглтоновый класс. Здесь происходит создание, получение и апгрейд бд   */
public class DataBase extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "LetsBuy.db";

    public static final String TABLE_NAME_PRODUCTS_LIST = "products";
    public static final String TABLE_NAME_LISTS_LIST = "lists";
    public static final String TABLE_NAME_CUSTOM_LIST = "customList";

    private static final String TAG = "DataBase";

    private static DataBase instance;
    private static Context mContext;

    private DataBase(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    public static DataBase getInstance(Context context){

        Log.i(TAG, "got instance");
        mContext = context;
        if (instance == null){
            instance = new DataBase(context);
        }
        return instance;

    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        DataBase.tableProducts.createTableProducts(db);
        DataBase.tableLists.createTableList(db);
        DataBase.tableCustomList.createTableCustomList(db);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.i("DB", "upgraded");
    }


    /*  Таблица продуктов. При создании добавляется стандартный набор продуктов.
    *   Стандартный набор лежит в res\values\strings\productsList   */
    public static class tableProducts{
        public static final String TABLE_ID = "_ID";
        public static final String TABLE_ITEM_NAME = "name";
        public static final String TABLE_DESCRIPTION = "description";
        public static final String TABLE_PHOTO = "photos";

        private static void createTableProducts(SQLiteDatabase db){
            //  Создание таблицы
            db.execSQL("create table " + TABLE_NAME_PRODUCTS_LIST + " ( "
                    + DataBase.tableProducts.TABLE_ID + " integer primary key autoincrement, "
                    + DataBase.tableProducts.TABLE_ITEM_NAME + " text, "
                    + DataBase.tableProducts.TABLE_DESCRIPTION + " text, "
                    + DataBase.tableProducts.TABLE_PHOTO + " text " + " ); ");
            Log.i(TAG, "products created");

            //  Заполнение стандартным набором
            String[] productsArr = mContext.getResources().getStringArray(R.array.productsList);
            for (String aProductsArr : productsArr) {
                ContentValues cv = new ContentValues();
                cv.put(DataBase.tableProducts.TABLE_ITEM_NAME, aProductsArr);
                db.insert(TABLE_NAME_PRODUCTS_LIST, null, cv);
            }
            Log.i(TAG, "products filled");
        }
    }

    /*  Таблица со списками. Создание   */
    public static class tableLists{
        public static final String TABLE_ID = "_ID";
        public static final String TABLE_ITEM_NAME = "name";

        private static void createTableList(SQLiteDatabase db){
            db.execSQL("create table " + TABLE_NAME_LISTS_LIST + " ( "
                    + DataBase.tableLists.TABLE_ID + " integer primary key autoincrement, "
                    + DataBase.tableLists.TABLE_ITEM_NAME + " text "+ " ); ");
            Log.i(TAG, "lists created");
        }

    }

    /*  Таблица с созданным пользователем списком   */
    public static class tableCustomList{
        public static final String TABLE_ID = "_id";
        public static final String TABLE_ID_PRODUCT = "id_product";
        public static final String TABLE_ID_LIST = "id_list";
        public static final String TABLE_DEPRECATED = "deprecated";

        /*  Синтетическое заполнение для дебага. Принимает id списка и id продукта, начиная с
            которого добавится count последующих продуктов   */
        public static void a1(DataBase db, int idList, int idProduct, int count){
            SQLiteDatabase bd = db.getWritableDatabase();
            for (int i = 0; i < count; i++) {
                ContentValues cv = new ContentValues();
                cv.put(tableCustomList.TABLE_ID_LIST, idList);
                cv.put(tableCustomList.TABLE_ID_PRODUCT, idProduct + i);
                bd.insert(TABLE_NAME_CUSTOM_LIST, null, cv);
            }
        }

        private static void createTableCustomList(SQLiteDatabase db){
            db.execSQL("create table " + TABLE_NAME_CUSTOM_LIST + " ( "
                    + DataBase.tableCustomList.TABLE_ID + " integer primary key autoincrement, "
                    + DataBase.tableCustomList.TABLE_ID_PRODUCT + " text, "
                    + DataBase.tableCustomList.TABLE_ID_LIST + " text, " +
                    tableCustomList.TABLE_DEPRECATED + " integer " + " ); ");
            Log.i(TAG, "custom list created");
        }

    }

}
