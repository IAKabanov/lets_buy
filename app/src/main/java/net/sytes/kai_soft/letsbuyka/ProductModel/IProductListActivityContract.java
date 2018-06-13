package net.sytes.kai_soft.letsbuyka.ProductModel;

import android.support.annotation.Nullable;

/**
 * Created by Лунтя on 30.04.2018.
 */

public interface IProductListActivityContract {

    void onListFragmentButtonClick();
    void onDetailFragmentButtonClick();
    void onListItemClick(Product product, String className);
    void onLongListItemClick(Product product, String className);


}
