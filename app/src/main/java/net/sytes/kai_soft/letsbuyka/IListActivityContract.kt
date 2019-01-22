package net.sytes.kai_soft.letsbuyka

import net.sytes.kai_soft.letsbuyka.Lists.List

interface IListActivityContract {
    fun onListFragmentButtonClick()
    fun onDetailFragmentButtonClick()
    fun onListItemClick(position: Long)
    fun onListItemLongClick(list: List)

}