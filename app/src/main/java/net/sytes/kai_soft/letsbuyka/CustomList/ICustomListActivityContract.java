package net.sytes.kai_soft.letsbuyka.CustomList;

import net.sytes.kai_soft.letsbuyka.ProductModel.Product;

/**
 * Created by Лунтя on 06.06.2018.
 */

public interface ICustomListActivityContract {
    void onListFragmentButtonClick();
    void onDetailFragmentButtonClick();
    void onListItemClick(Product product, String className);
}
