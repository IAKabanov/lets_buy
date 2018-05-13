package net.sytes.kai_soft.letsbuyka;

import net.sytes.kai_soft.letsbuyka.ProductModel.ProductDB;

/**
 * Created by Лунтя on 08.05.2018.
 */

public class Application extends android.app.Application {
    private static ProductDB productDB;

    @Override
    public void onCreate() {
        productDB = ProductDB.getInstance(getApplicationContext());

        super.onCreate();
    }

    public static ProductDB getDB(){
        return productDB;
    }

}
