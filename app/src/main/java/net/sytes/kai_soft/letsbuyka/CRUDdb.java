package net.sytes.kai_soft.letsbuyka;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

import net.sytes.kai_soft.letsbuyka.Lists.List;
import net.sytes.kai_soft.letsbuyka.ProductModel.Product;

/**
 * Created by Лунтя on 06.06.2018.
 */

public class CRUDdb {
    private static DataBase db = Application.getDB();

    public static void insertToTableProducts(String name, String descr, String photo) {
        ContentValues cv = new ContentValues();

        db = Application.getDB();

        SQLiteDatabase db = CRUDdb.db.getWritableDatabase();

        cv.put(DataBase.tableProducts.TABLE_ITEM_NAME, name);
        cv.put(DataBase.tableProducts.TABLE_DESCRIPTION, descr);
        cv.put(DataBase.tableProducts.TABLE_PHOTO, photo);
        // вставляем запись и получаем ее ID
        db.insert(DataBase.TABLE_NAME_PRODUCTS_LIST, null, cv);
    }

    public static void updateTableProducts(Product product) {
        ContentValues cv = new ContentValues();

        db = Application.getDB();

        SQLiteDatabase db = CRUDdb.db.getWritableDatabase();

        cv.put(DataBase.tableProducts.TABLE_ITEM_NAME, product.getItemName());
        cv.put(DataBase.tableProducts.TABLE_DESCRIPTION, product.getDescription());
        cv.put(DataBase.tableProducts.TABLE_PHOTO, product.getFirstImagePath());
        // вставляем запись и получаем ее ID
        //db.insert(DataBase.TABLE_NAME, null, cv);
        db.update(DataBase.TABLE_NAME_PRODUCTS_LIST, cv, DataBase.tableProducts.TABLE_ID + " = ?",
                new String[]{String.valueOf(product.getId())});
    }

    public static void deleteItemProducts(Product product){
        SQLiteDatabase db = CRUDdb.db.getWritableDatabase();

        db.delete(DataBase.TABLE_NAME_PRODUCTS_LIST, DataBase.tableProducts.TABLE_ID + " = ?",
                new String[]{String.valueOf(product.getId())});
    }

    public static void insertToTableLists(String name) {
        ContentValues cv = new ContentValues();

        db = Application.getDB();

        SQLiteDatabase db = CRUDdb.db.getWritableDatabase();

        cv.put(DataBase.tableLists.TABLE_ITEM_NAME, name);
        // вставляем запись и получаем ее ID
        db.insert(DataBase.TABLE_NAME_LISTS_LIST, null, cv);
    }

    public static void updateTableLists(List list) {
        ContentValues cv = new ContentValues();

        db = Application.getDB();

        SQLiteDatabase db = CRUDdb.db.getWritableDatabase();

        cv.put(DataBase.tableLists.TABLE_ITEM_NAME, list.getItemName());
        // вставляем запись и получаем ее ID
        //db.insert(DataBase.TABLE_NAME, null, cv);
        db.update(DataBase.TABLE_NAME_LISTS_LIST, cv, DataBase.tableLists.TABLE_ID + " = ?",
                new String[]{String.valueOf(list.getId())});
    }

    public static void deleteItemLists(List list){
        SQLiteDatabase db = CRUDdb.db.getWritableDatabase();

        db.delete(DataBase.TABLE_NAME_LISTS_LIST, DataBase.tableLists.TABLE_ID + " = ?",
                new String[]{String.valueOf(list.getId())});
    }
}