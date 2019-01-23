package net.sytes.kai_soft.letsbuyka

import net.sytes.kai_soft.letsbuyka.Lists.List

/*  Interface for activity  */
interface IListActivityContract {
    fun onListFragmentButtonClick()
    fun onDetailFragmentButtonClick()
    fun onListItemClick(position: Int)
    fun onListItemLongClick(list: List)

}