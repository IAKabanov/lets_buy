package net.sytes.kai_soft.letsbuyka.ProductModel;

import android.content.Context;

/**
 * Created by Лунтя on 30.04.2018.
 */

public interface IProductListActivityContract {
    void onListFragmentButtonClick();
    void onDetailFragmentButtonClick();
    void showAdapterToast(int position);
}
