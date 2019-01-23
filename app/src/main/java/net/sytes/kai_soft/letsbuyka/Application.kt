package net.sytes.kai_soft.letsbuyka

import android.util.Log

class Application: android.app.Application() {
    companion object {
        private const val tag = "letsbuy_Application"
        private lateinit var dataBase: DataBase
        private lateinit var context: Application

        fun getDB(): DataBase{
            Log.i(tag, "getDB()")
            return dataBase
        }
    }


    override fun onCreate() {
        Log.i(tag, "onCreate()")
        context = this
        dataBase = DataBase.getInstance(context)
        super.onCreate()
    }
}