package net.sytes.kai_soft.letsbuyka.ProductModel

import android.util.Log

import java.io.Serializable

class Product(val id: Int, var name: String, var description: String): Serializable {
    companion object {
        /*private const val tagID = "id"
        private const val tagName = "name"
        private const val tagDescription = "description"*/
        private const val myTag = "letsbuy_product"
    }

    constructor(): this(0, "", "")
    constructor(product: Product): this(product.id, product.name, product.description)

    override fun toString(): String {
        Log.i(myTag, "toString()")
        return "$id, $name, $description"
    }
}