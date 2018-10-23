package net.sytes.kai_soft.letsbuyka;

import android.content.Context;

/**
 * Created by Лунтя on 08.05.2018.
 */
/*  В этом классе мы открываем базу данных  */
public class Application extends android.app.Application {
    private static DataBase dataBase;
    private Context context;

    @Override
    public void onCreate() {
        dataBase = DataBase.getInstance(getApplicationContext());
        //DataBase.tableProducts.a1(dataBase);
        super.onCreate();
    }

    public static DataBase getDB(){
        return dataBase;
    }
}
