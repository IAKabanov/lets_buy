package net.sytes.kai_soft.letsbuyka.Lists;

import net.sytes.kai_soft.letsbuyka.ProductModel.Product;

/**
 * Created by Лунтя on 02.06.2018.
 */

public interface IListsListActivityContract {
    void onListFragmentButtonClick();
    void onDetailFragmentButtonClick();
    void onListItemClick(List list);
}
