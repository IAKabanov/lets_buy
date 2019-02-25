package net.sytes.kai_soft.letsbuyka

class Constants {
    companion object {
        const val DATABASE_NAME = "LetsBuy15.db"
        const val TABLE_NAME_PRODUCTS = "products"
        const val TABLE_NAME_LISTS = "lists"
        const val TABLE_NAME_CUSTOM_LIST = "customList"
        const val TABLE_ID = "_ID"
        const val TABLE_ITEM_NAME = "name"
        const val TABLE_DESCRIPTION = "description"
        const val TABLE_ID_PRODUCT = "id_product"
        const val TABLE_ID_LIST = "id_list"
        const val TABLE_DEPRECATED = "deprecated"
        const val DEPRECATED_TRUE = 1
        const val DEPRECATED_FALSE = 2
        const val EDITABLE = "editable"
        const val PRODUCT = "product"

        const val FIREBASE_EXCEPTION_WEAK_PASSWORD = "ERROR_WEAK_PASSWORD"
        const val FIREBASE_EXCEPTION_INVALID_EMAIL = "ERROR_INVALID_EMAIL"
        const val FIREBASE_EXCEPTION_EMAIL_ALREADY_IN_USE = "ERROR_EMAIL_ALREADY_IN_USE"
        const val FIREBASE_EXCEPTION_USER_NOT_FOUND = "ERROR_USER_NOT_FOUND"
        const val FIREBASE_EXCEPTION_WRONG_PASSWORD = "ERROR_WRONG_PASSWORD"

    }
}