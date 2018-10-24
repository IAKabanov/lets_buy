package net.sytes.kai_soft.letsbuyka

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log

class DataBase(context: Context, name: String = Constants.DATABASE_NAME,
               factory: SQLiteDatabase.CursorFactory? = null,
               version: Int = 1):
        SQLiteOpenHelper(context, name, factory, version) {

    companion object {
        private const val tag = "letsbuy_DataBase"
        private var instance: DataBase? = null
        private lateinit var aContext: Application

        fun getInstance(context: Application): DataBase{
            Log.i(tag, "getInstance()")
            if (instance == null){
                instance = DataBase(context)
                aContext = context
            }
            return instance as DataBase
        }

        class TableProducts{
            companion object {
                internal fun createTableProducts(db: SQLiteDatabase){
                    db.execSQL("create table " + Constants.TABLE_NAME_PRODUCTS_LIST + " ( "
                            + Constants.TABLE_ID + " integer primary key autoincrement, "
                            + Constants.TABLE_ITEM_NAME + " text, "
                            + Constants.TABLE_DESCRIPTION + " text " + " ); ")
                    Log.i(tag, "Products created")

                    /* Заполняем первоначальными значениями    */
                    Log.i(tag, "Filling products")
                    //CRUDdb.fillProducts(aContext, db)
                }
            }
        }

        class TableLists{
            companion object {
                /*  Таблица со списками. Создание   */
                internal fun createTableList(db: SQLiteDatabase){
                    db.execSQL("create table " + Constants.TABLE_NAME_LISTS_LIST + " ( "
                            + Constants.TABLE_ID + " integer primary key autoincrement, "
                            + Constants.TABLE_ITEM_NAME + " text " + " ); ")
                    Log.i(tag, "Lists created")
                }
            }
        }

        class TableCustomList{
            companion object {
                // Создание таблицы
                internal fun createTableCustomList(db: SQLiteDatabase){
                    db.execSQL("create table " + Constants.TABLE_NAME_CUSTOM_LIST + " ( "
                            + Constants.TABLE_ID + " integer primary key autoincrement, "
                            + Constants.TABLE_ID_PRODUCT + " text, "
                            + Constants.TABLE_ID_LIST + " text, " +
                            Constants.TABLE_DEPRECATED + " integer " + " ); ")
                    Log.i(tag, "Custom list created")
                }
            }
        }
    }


    override fun onUpgrade(db: SQLiteDatabase?, p1: Int, p2: Int) {
        Log.i("DB", "upgraded")
    }

    override fun onCreate(db: SQLiteDatabase?) {
        Log.i(tag, "onCreate")
        if (db != null){
            DataBase.Companion.TableProducts.createTableProducts(db)
            DataBase.Companion.TableLists.createTableList(db)
            DataBase.Companion.TableCustomList.createTableCustomList(db)
        }
        else{
            Log.w(tag, "did not created, db == null")
        }
    }


}