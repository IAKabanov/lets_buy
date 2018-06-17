package net.sytes.kai_soft.letsbuyka.ProductModel;

/**
 * Created by Лунтя on 15.06.2018.
 */

public interface IProductDetailContract {
    void savePressed();
    void deletePressed();
    void editPressed();
    boolean isNew();
}
