package net.sytes.kai_soft.letsbuyka.Lists

import java.io.Serializable

class List(val id: Int, var itemName: String): Serializable{

    companion object {
        private const val myTag = "letsbuy_List"
    }

    override fun toString(): String {
        return "$id, $itemName"
    }
}