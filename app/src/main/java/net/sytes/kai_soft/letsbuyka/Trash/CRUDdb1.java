package net.sytes.kai_soft.letsbuyka.Trash;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.content.Context;

import net.sytes.kai_soft.letsbuyka.Application;
import net.sytes.kai_soft.letsbuyka.Constants;
import net.sytes.kai_soft.letsbuyka.DataBase;
import net.sytes.kai_soft.letsbuyka.Lists.List;
import net.sytes.kai_soft.letsbuyka.ProductModel.Product;
import net.sytes.kai_soft.letsbuyka.R;

import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;

/**
 * Created by Лунтя on 06.06.2018.
 */
/*  !!!ЗДЕСЬ НАДО ДОБАВИТЬ ЧТЕНИЕ ЭЛЕМЕНТОВ БД. СЕЙЧАС ОНИ В АДАПТЕРАХ!!!*/

/*  Класс в котором можно производить добавление, считывание, обновление и удаление из таблиц
 *   базы данных */
public class CRUDdb1 {
    private static DataBase db = Application.Companion.getDB();   //Экземпляр базы данных

    /*  Добавление продукта в таблицу продуктов. Принимает имя и описание продукта  */
    public static void insertToTableProducts(String name, String description) {
        ContentValues cv = new ContentValues();

        //db = Application.getDB();  // Хз зачем я это сделал

        SQLiteDatabase db = CRUDdb1.db.getWritableDatabase();

        cv.put(Constants.TABLE_ITEM_NAME, name);
        cv.put(Constants.TABLE_DESCRIPTION, description);

        db.insert(Constants.TABLE_NAME_PRODUCTS, null, cv);
    }

    /*  Обновление элемента списка в таблице продуктов  */
    public static void updateTableProducts(Product product) {
        ContentValues cv = new ContentValues();

        //db = Application.getDB(); // Зачем я опять это сделал?!

        SQLiteDatabase db = CRUDdb1.db.getWritableDatabase();

        cv.put(Constants.TABLE_ITEM_NAME, product.getName());
        cv.put(Constants.TABLE_DESCRIPTION, product.getDescription());
        //cv.put(DataBase.tableProducts.TABLE_PHOTO, product.getFirstImagePath());

        db.update(Constants.TABLE_NAME_PRODUCTS, cv,
                Constants.TABLE_ID + " = ?",
                new String[]{String.valueOf(product.getId())});
    }

    /*  Удаление элемента списка из таблицы продуктов   */
    public static void deleteItemProducts(Product product) {
        SQLiteDatabase db = CRUDdb1.db.getWritableDatabase();

        db.delete(Constants.TABLE_NAME_PRODUCTS,
                Constants.TABLE_ID + " = ?",
                new String[]{String.valueOf(product.getId())});
    }


    /*  Добавление списка в таблицу списков. Принимает имя списка  */
    public static void insertToTableLists(String name) {
        ContentValues cv = new ContentValues();

        //db = Application.getDB(); //И ещё раз хз.

        SQLiteDatabase db = CRUDdb1.db.getWritableDatabase();

        cv.put(Constants.TABLE_ITEM_NAME, name);

        db.insert(Constants.TABLE_NAME_LISTS, null, cv);
    }

    /*  Получение элементов из таблицы списков  */

    public static ArrayList<List> readFromTableLists(@Nullable String filter){
        SQLiteDatabase db = CRUDdb1.db.getWritableDatabase();

        ArrayList<List> lists = new ArrayList<>();
        Cursor c;
        if (filter == null){
            c = db.query(Constants.TABLE_NAME_LISTS, null, null,
                    null, null, null,
                    Constants.TABLE_ID + " asc");
        } else {
            c = db.rawQuery("select * from " + Constants.TABLE_NAME_LISTS +
                            " where " + Constants.TABLE_ITEM_NAME + " like '%"
                            + filter + "%'",
                    null);
        }

        if (c.moveToFirst()) {

            // определяем номера столбцов по имени в выборке
            int idColIndex = c.getColumnIndex(Constants.TABLE_ID);
            int nameColIndex = c.getColumnIndex(Constants.TABLE_ITEM_NAME);
            do {
                lists.add(new List(c.getInt(idColIndex), c.getString(nameColIndex)));
                // переход на следующую строку
                // а если следующей нет (текущая - последняя), то false - выходим из цикла
            } while (c.moveToNext());
        } else {
            c.close();
        }

        return lists;
    }

    /*  Обновление списка в таблице списков    */
    public static void updateTableLists(List list) {
        ContentValues cv = new ContentValues();

        //db = Application.getDB(); //Чёт плохо как-то...

        SQLiteDatabase db = CRUDdb1.db.getWritableDatabase();

        cv.put(Constants.TABLE_ITEM_NAME, list.getItemName());

        db.update(Constants.TABLE_NAME_LISTS, cv,
                Constants.TABLE_ID + " = ?",
                new String[]{String.valueOf(list.getId())});
    }

    /*  Удаление списка из таблицы списков */
    public static void deleteItemLists(List list) {
        SQLiteDatabase db = CRUDdb1.db.getWritableDatabase();

        db.delete(Constants.TABLE_NAME_LISTS, Constants.TABLE_ID + " = ?",
                new String[]{String.valueOf(list.getId())});
    }


    /*  Добавление элемента в таблицу пользовательских списков  */
    public static void insertToTableCustomList(long id_list, long id_product) {
        ContentValues cv = new ContentValues();

        //db = Application.getDB(); //...

        SQLiteDatabase SQLdb = CRUDdb1.db.getWritableDatabase();

        /*  Здесь идёт проверка на наличие такой записи в этом пользовательском списке.
         *   Если такая запись есть, то её добавлять не нужно.*/
        Cursor c = SQLdb.rawQuery("select * from " +
                        Constants.TABLE_NAME_CUSTOM_LIST + " where " +
                        Constants.TABLE_ID_LIST + " = " + String.valueOf(id_list) +
                        " and " + Constants.TABLE_ID_PRODUCT + " = " +
                        String.valueOf(id_product),
                null);
        if (c.moveToFirst() == false) {
            cv.put(Constants.TABLE_ID_LIST, id_list);
            cv.put(Constants.TABLE_ID_PRODUCT, id_product);
            cv.put(Constants.TABLE_DEPRECATED,
                    Constants.DEPRECATED_FALSE);
            // вставляем запись и получаем ее ID
            SQLdb.insert(Constants.TABLE_NAME_CUSTOM_LIST, null, cv);
        }
        c.close();
    }

    /*  Удаление элемента из таблицы пользовательских списков   */
    public static void deleteItemCustomList(long id) {
        SQLiteDatabase db = CRUDdb1.db.getWritableDatabase();

        db.delete(Constants.TABLE_NAME_CUSTOM_LIST, Constants.TABLE_ID + " = ?",
                new String[]{String.valueOf(id)});
    }

    /*  Зачёркиваем элемент в пользовательском списке   */
    public static void makeDeprecated(long id) {

        ContentValues cv = new ContentValues();

        //db = Application.getDB(); //Почему, мистер Андерсон, почему?

        SQLiteDatabase db = CRUDdb1.db.getWritableDatabase();

        Cursor c = db.rawQuery("select * from " + Constants.TABLE_NAME_CUSTOM_LIST + " where " +
                Constants.TABLE_ID + " = " + String.valueOf(id), null);

        int idDeprecatedColumn = c.getColumnIndex(Constants.TABLE_DEPRECATED);
        if (c.moveToFirst()) {
            /*Если элемент не был зачёркнут, зачёркиваем. Или наоборот*/
            if (c.getLong(idDeprecatedColumn) == Constants.DEPRECATED_TRUE) {
                cv.put(Constants.TABLE_DEPRECATED,
                        Constants.DEPRECATED_FALSE);
            } else {
                cv.put(Constants.TABLE_DEPRECATED,
                        Constants.DEPRECATED_TRUE);
            }

            db.update(Constants.TABLE_NAME_CUSTOM_LIST, cv,
                    Constants.TABLE_ID + " = ?",
                    new String[]{String.valueOf(id)});
        }
        c.close();
    }

    public static void fillProducts(Context context, SQLiteDatabase db){

        ContentValues cv = new ContentValues();

        //db = Application.getDB();  // Хз зачем я это сделал

        String[] products = context.getResources().getStringArray(R.array.productsList);

        for (String name:products) {
            cv.put(Constants.TABLE_ITEM_NAME, name);
            cv.put(Constants.TABLE_DESCRIPTION, "");
            db.insert(Constants.TABLE_NAME_PRODUCTS, null, cv);
        }


    }

}