package net.sytes.kai_soft.letsbuyka.ProductModel

interface IPRContract {
    fun onListItemClick(product: Product, className: String)
    fun onLongListItemClick(product: Product, className: String)
}