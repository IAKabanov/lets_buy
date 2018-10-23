package net.sytes.kai_soft.letsbuyka;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import net.sytes.kai_soft.letsbuyka.CustomList.CustomList;
import net.sytes.kai_soft.letsbuyka.Lists.List;
import net.sytes.kai_soft.letsbuyka.ProductModel.Product;

/**
 * Created by Лунтя on 06.06.2018.
 */
/*  !!!ЗДЕСЬ НАДО ДОБАВИТЬ ЧТЕНИЕ ЭЛЕМЕНТОВ БД. СЕЙЧАС ОНИ В АДАПТЕРАХ!!!*/

/*  Класс в котором можно производить добавление, считывание, обновление и удаление из таблиц
 *   базы данных */
public class CRUDdb {
    private static DataBase db = Application.Companion.getDB();   //Экземпляр базы данных

    /*  Добавление продукта в таблицу продуктов. Принимает имя и описание продукта  */
    public static void insertToTableProducts(String name, String description) {
        ContentValues cv = new ContentValues();

        //db = Application.getDB();  // Хз зачем я это сделал

        SQLiteDatabase db = CRUDdb.db.getWritableDatabase();

        cv.put(DataBase.tableProducts.TABLE_ITEM_NAME, name);
        cv.put(DataBase.tableProducts.TABLE_DESCRIPTION, description);

        db.insert(DataBase.TABLE_NAME_PRODUCTS_LIST, null, cv);
    }

    /*  Обновление элемента списка в таблице продуктов  */
    public static void updateTableProducts(Product product) {
        ContentValues cv = new ContentValues();

        //db = Application.getDB(); // Зачем я опять это сделал?!

        SQLiteDatabase db = CRUDdb.db.getWritableDatabase();

        cv.put(DataBase.tableProducts.TABLE_ITEM_NAME, product.getItemName());
        cv.put(DataBase.tableProducts.TABLE_DESCRIPTION, product.getDescription());
        //cv.put(DataBase.tableProducts.TABLE_PHOTO, product.getFirstImagePath());

        db.update(DataBase.TABLE_NAME_PRODUCTS_LIST, cv,
                DataBase.tableProducts.TABLE_ID + " = ?",
                new String[]{String.valueOf(product.getId())});
    }

    /*  Удаление элемента списка из таблицы продуктов   */
    public static void deleteItemProducts(Product product) {
        SQLiteDatabase db = CRUDdb.db.getWritableDatabase();

        db.delete(DataBase.TABLE_NAME_PRODUCTS_LIST,
                DataBase.tableProducts.TABLE_ID + " = ?",
                new String[]{String.valueOf(product.getId())});
    }


    /*  Добавление списка в таблицу списков. Принимает имя списка  */
    public static void insertToTableLists(String name) {
        ContentValues cv = new ContentValues();

        //db = Application.getDB(); //И ещё раз хз.

        SQLiteDatabase db = CRUDdb.db.getWritableDatabase();

        cv.put(DataBase.tableLists.TABLE_ITEM_NAME, name);

        db.insert(DataBase.TABLE_NAME_LISTS_LIST, null, cv);
    }

    /*  Обновление списка в таблице списков    */
    public static void updateTableLists(List list) {
        ContentValues cv = new ContentValues();

        //db = Application.getDB(); //Чёт плохо как-то...

        SQLiteDatabase db = CRUDdb.db.getWritableDatabase();

        cv.put(DataBase.tableLists.TABLE_ITEM_NAME, list.getItemName());

        db.update(DataBase.TABLE_NAME_LISTS_LIST, cv,
                DataBase.tableLists.TABLE_ID + " = ?",
                new String[]{String.valueOf(list.getId())});
    }

    /*  Удаление списка из таблицы списков */
    public static void deleteItemLists(List list) {
        SQLiteDatabase db = CRUDdb.db.getWritableDatabase();

        db.delete(DataBase.TABLE_NAME_LISTS_LIST, DataBase.tableLists.TABLE_ID + " = ?",
                new String[]{String.valueOf(list.getId())});
    }


    /*  Добавление элемента в таблицу пользовательских списков  */
    public static void insertToTableCustomList(long id_list, long id_product) {
        ContentValues cv = new ContentValues();

        //db = Application.getDB(); //...

        SQLiteDatabase SQLdb = CRUDdb.db.getWritableDatabase();

        /*  Здесь идёт проверка на наличие такой записи в этом пользовательском списке.
         *   Если такая запись есть, то её добавлять не нужно.*/
        Cursor c = SQLdb.rawQuery("select * from " +
                        DataBase.TABLE_NAME_CUSTOM_LIST + " where " +
                        DataBase.tableCustomList.TABLE_ID_LIST + " = " + String.valueOf(id_list) +
                        " and " + DataBase.tableCustomList.TABLE_ID_PRODUCT + " = " +
                        String.valueOf(id_product),
                null);
        if (c.moveToFirst() == false) {
            cv.put(DataBase.tableCustomList.TABLE_ID_LIST, id_list);
            cv.put(DataBase.tableCustomList.TABLE_ID_PRODUCT, id_product);
            cv.put(DataBase.tableCustomList.TABLE_DEPRECATED,
                    CustomList.DEPRECATED_FALSE);
            // вставляем запись и получаем ее ID
            SQLdb.insert(DataBase.TABLE_NAME_CUSTOM_LIST, null, cv);
        }
        c.close();
    }

    /*  Удаление элемента из таблицы пользовательских списков   */
    public static void deleteItemCustomList(long id) {
        SQLiteDatabase db = CRUDdb.db.getWritableDatabase();

        db.delete(DataBase.TABLE_NAME_CUSTOM_LIST, DataBase.tableLists.TABLE_ID + " = ?",
                new String[]{String.valueOf(id)});
    }

    /*  Зачёркиваем элемент в пользовательском списке   */
    public static void makeDeprecated(long id) {

        ContentValues cv = new ContentValues();

        //db = Application.getDB(); //Почему, мистер Андерсон, почему?

        SQLiteDatabase db = CRUDdb.db.getWritableDatabase();

        Cursor c = db.rawQuery("select * from " + DataBase.TABLE_NAME_CUSTOM_LIST + " where " +
                DataBase.tableCustomList.TABLE_ID + " = " + String.valueOf(id), null);

        int idDeprecatedColumn = c.getColumnIndex(DataBase.tableCustomList.TABLE_DEPRECATED);
        if (c.moveToFirst()) {
            /*Если элемент не был зачёркнут, зачёркиваем. Или наоборот*/
            if (c.getLong(idDeprecatedColumn) == CustomList.DEPRECATED_TRUE) {
                cv.put(DataBase.tableCustomList.TABLE_DEPRECATED,
                        CustomList.DEPRECATED_FALSE);
            } else {
                cv.put(DataBase.tableCustomList.TABLE_DEPRECATED,
                        CustomList.DEPRECATED_TRUE);
            }

            db.update(DataBase.TABLE_NAME_CUSTOM_LIST, cv,
                    DataBase.tableCustomList.TABLE_ID + " = ?",
                    new String[]{String.valueOf(id)});
        }
        c.close();
    }

}