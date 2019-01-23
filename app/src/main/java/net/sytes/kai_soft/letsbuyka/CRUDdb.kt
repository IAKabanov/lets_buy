package net.sytes.kai_soft.letsbuyka

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.util.Log
import net.sytes.kai_soft.letsbuyka.Lists.List

import net.sytes.kai_soft.letsbuyka.ProductModel.Product

@Suppress("DUPLICATE_LABEL_IN_WHEN")
/*  CRUD class for DataBase*/
class CRUDdb {
    companion object {
        private val dataBase: DataBase = Application.getDB()
        private const val tag = "letsbuy_CRUDdb"

        /*  Add product */
        fun insertToTableProducts(name: String, description: String) {
            Log.i(tag, "insertToTableProducts($name, $description)")

            val cv = ContentValues()

            val db = CRUDdb.dataBase.writableDatabase

            cv.put(Constants.TABLE_ITEM_NAME, name)
            cv.put(Constants.TABLE_DESCRIPTION, description)

            db.insert(Constants.TABLE_NAME_PRODUCTS, null, cv)
        }

        /*  Update product  */
        fun updateTableProducts(product: Product) {
            Log.i(tag, "updateTableProducts($product)")

            val cv = ContentValues()

            val db = CRUDdb.dataBase.writableDatabase

            cv.put(Constants.TABLE_ITEM_NAME, product.name)
            cv.put(Constants.TABLE_DESCRIPTION, product.description)

            db.update(Constants.TABLE_NAME_PRODUCTS, cv,
                    Constants.TABLE_ID + " = ?",
                    arrayOf(product.id.toString()))
        }

        /*  Delete product  */
        fun deleteItemProducts(product: Product) {
            Log.i(tag, "deleteItemProducts($product)")

            val db = CRUDdb.dataBase.writableDatabase

            db.delete(Constants.TABLE_NAME_PRODUCTS,
                    Constants.TABLE_ID + " = ?",
                    arrayOf(product.id.toString()))
        }

        /*  Initial filling products. Works only first start    */
        fun fillProducts(context: Context, dataBase: SQLiteDatabase) {
            Log.i(tag, "fillProducts()")

            val cv = ContentValues()

            val product = context.resources.getStringArray(R.array.productsList)

            for (name: String in product) {
                cv.put(Constants.TABLE_ITEM_NAME, name)
                cv.put(Constants.TABLE_DESCRIPTION, "")
                dataBase.insert(Constants.TABLE_NAME_PRODUCTS, null, cv)
            }
        }


        /*  Add list    */
        fun insertToTableLists(name: String) {
            Log.i(tag, "insertToTableLists($name)")

            val cv = ContentValues()

            val db = CRUDdb.dataBase.writableDatabase

            cv.put(Constants.TABLE_ITEM_NAME, name)

            db.insert(Constants.TABLE_NAME_LISTS, null, cv)
        }

        /*  Read from "lists" with filter   */
        @SuppressLint("Recycle")
        fun readFromTableLists(filter: String? = null): ArrayList<List> {
            Log.i(tag, "readFromTableLists($filter)")

            val dataBase = CRUDdb.dataBase.readableDatabase

            val lists = ArrayList<List>()
            val c = if (filter == null) {
                dataBase.query(Constants.TABLE_NAME_LISTS, null, null, null, null, null,
                        Constants.TABLE_ID + " asc")
            } else {
                dataBase.rawQuery("select * from " + Constants.TABLE_NAME_LISTS +
                        " where " + Constants.TABLE_ITEM_NAME + " like '%"
                        + filter + "%'",
                        null)

            }

            if (c.moveToFirst()) {
                val idColIndex = c.getColumnIndex(Constants.TABLE_ID)
                val nameColIndex = c.getColumnIndex(Constants.TABLE_ITEM_NAME)
                do {
                    lists.add(List(c.getInt(idColIndex), c.getString(nameColIndex)))
                } while (c.moveToNext())
            }
            c.close()
            return lists
        }

        /*  Update list */
        fun updateTableLists(list: List) {
            Log.i(tag, "updateTableLists($list)")

            val cv = ContentValues()

            val dataBase = CRUDdb.dataBase.writableDatabase

            cv.put(Constants.TABLE_ITEM_NAME, list.itemName)

            dataBase.update(Constants.TABLE_NAME_LISTS, cv,
                    Constants.TABLE_ID + " = ?",
                    arrayOf(list.id.toString()))
        }

        /*  Delete list */
        fun deleteItemLists(list: List) {
            Log.i(tag, "deleteItemLists($list)")

            val dataBase = CRUDdb.dataBase.writableDatabase

            dataBase.delete(Constants.TABLE_NAME_LISTS, Constants.TABLE_ID + " = ?",
                    arrayOf(list.id.toString()))
        }


        /*  Add element to custom list  */
        fun insertToTableCustomList(idList: Int, idProduct: Int) {
            Log.i(tag, "insertToTableCustomList($idList,$idProduct)")

            val dataBase = CRUDdb.dataBase.writableDatabase

            val cv = ContentValues()

            val c = dataBase.rawQuery("select * from " +
                    Constants.TABLE_NAME_CUSTOM_LIST + " where " +
                    Constants.TABLE_ID_LIST + " = " + idList.toString() +
                    " and " + Constants.TABLE_ID_PRODUCT + " = " +
                    idProduct.toString(),
                    null)
            if (!c.moveToFirst()) {
                cv.put(Constants.TABLE_ID_LIST, idList)
                cv.put(Constants.TABLE_ID_PRODUCT, idProduct)
                cv.put(Constants.TABLE_DEPRECATED, Constants.DEPRECATED_FALSE)

                dataBase.insert(Constants.TABLE_NAME_CUSTOM_LIST, null, cv)
            }
            c.close()
        }

        /*  Delete element from custom list */
        fun deleteItemCustomList(id: Int) {
            Log.i(tag, "deleteItemCustomList($id)")

            val dataBase = CRUDdb.dataBase.writableDatabase

            dataBase.delete(Constants.TABLE_NAME_CUSTOM_LIST,
                    Constants.TABLE_ID + " = ?", arrayOf(id.toString()))

        }

        /*  Crossing out element*/
        fun makeDeprecated(id: Int) {
            Log.i(tag, "makeDeprecated($id)")

            val dataBase = CRUDdb.dataBase.writableDatabase
            val cv = ContentValues()
            val c = dataBase.rawQuery("select * from " + Constants.TABLE_NAME_CUSTOM_LIST
                    + " where " + Constants.TABLE_ID + " = " + id.toString(), null)
            val idDeprecatedColumn = c.getColumnIndex(Constants.TABLE_DEPRECATED)
            if (c.moveToFirst()) {
                if (c.getInt(idDeprecatedColumn) == Constants.DEPRECATED_TRUE) {
                    cv.put(Constants.TABLE_DEPRECATED, Constants.DEPRECATED_FALSE)
                } else {
                    cv.put(Constants.TABLE_DEPRECATED, Constants.DEPRECATED_TRUE)
                }
                dataBase.update(Constants.TABLE_NAME_CUSTOM_LIST, cv,
                        Constants.TABLE_ID + " = ?", arrayOf(id.toString()))
            }
            c.close()
        }

    }
}