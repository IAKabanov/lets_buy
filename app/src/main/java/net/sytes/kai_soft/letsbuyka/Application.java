package net.sytes.kai_soft.letsbuyka;

import net.sytes.kai_soft.letsbuyka.ProductModel.DataBase;

/**
 * Created by Лунтя on 08.05.2018.
 */

public class Application extends android.app.Application {
    private static DataBase dataBase;

    @Override
    public void onCreate() {
        dataBase = DataBase.getInstance(getApplicationContext());

        super.onCreate();
    }

    public static DataBase getDB(){
        return dataBase;
    }

}
