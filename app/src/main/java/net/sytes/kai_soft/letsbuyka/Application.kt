package net.sytes.kai_soft.letsbuyka

import android.util.Log

class Application: android.app.Application() {
    companion object {
        private const val myTag = "letsbuy_Application"
        private lateinit var dataBase: DataBase
        private lateinit var context: Application

        fun getDB(): DataBase{
            Log.i(myTag, "getDB()")
            return dataBase
        }
    }


    override fun onCreate() {
        Log.i(myTag, "onCreate()")
        context = this
        dataBase = DataBase.getInstance(context)
        super.onCreate()
    }
}