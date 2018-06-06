package net.sytes.kai_soft.letsbuyka;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

import net.sytes.kai_soft.letsbuyka.ProductModel.Product;

/**
 * Created by Лунтя on 08.05.2018.
 */

public class Application extends android.app.Application {
    private static DataBase dataBase;

    @Override
    public void onCreate() {
        dataBase = DataBase.getInstance(getApplicationContext());

        super.onCreate();
    }

    public static DataBase getDB(){
        return dataBase;
    }

    /**
     * Created by Лунтя on 20.05.2018.
     */

    public static class CRUDdb {
        private static DataBase dbProduct = getDB();

        public static void insertToTable(String name, String descr, String photo) {
            ContentValues cv = new ContentValues();

            dbProduct = getDB();

            SQLiteDatabase db = dbProduct.getWritableDatabase();

            cv.put(DataBase.tableProducts.TABLE_ITEM_NAME, name);
            cv.put(DataBase.tableProducts.TABLE_DESCRIPTION, descr);
            cv.put(DataBase.tableProducts.TABLE_PHOTO, photo);
            // вставляем запись и получаем ее ID
            db.insert(DataBase.TABLE_NAME_PRODUCTS_LIST, null, cv);
        }

        public static void updateTable(Product product) {
            ContentValues cv = new ContentValues();

            dbProduct = getDB();

            SQLiteDatabase db = dbProduct.getWritableDatabase();

            cv.put(DataBase.tableProducts.TABLE_ITEM_NAME, product.getItemName());
            cv.put(DataBase.tableProducts.TABLE_DESCRIPTION, product.getDescription());
            cv.put(DataBase.tableProducts.TABLE_PHOTO, product.getFirstImagePath());
            // вставляем запись и получаем ее ID
            //db.insert(DataBase.TABLE_NAME, null, cv);
            db.update(DataBase.TABLE_NAME_PRODUCTS_LIST, cv, DataBase.tableProducts.TABLE_ID + " = ?",
                    new String[]{String.valueOf(product.getId())});
        }

        public static void deleteItem(Product product){
            SQLiteDatabase db = dbProduct.getWritableDatabase();

            db.delete(DataBase.TABLE_NAME_PRODUCTS_LIST, DataBase.tableProducts.TABLE_ID + " = ?",
                    new String[]{String.valueOf(product.getId())});
        }
    }
}
