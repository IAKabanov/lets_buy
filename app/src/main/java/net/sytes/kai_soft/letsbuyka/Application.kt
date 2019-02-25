package net.sytes.kai_soft.letsbuyka

import android.util.Log
import com.google.firebase.auth.FirebaseAuth

class Application: android.app.Application() {
    companion object {
        private const val myTag = "letsbuy_Application"
        private lateinit var dataBase: DataBase
        private lateinit var context: Application
        private lateinit var mAuth: FirebaseAuth

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