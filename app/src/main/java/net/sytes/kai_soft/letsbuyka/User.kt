package net.sytes.kai_soft.letsbuyka

import android.app.Activity
import android.app.Fragment
import android.content.Context
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import android.widget.Toast
import android.content.ContentValues.TAG
import android.support.annotation.NonNull
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.*
import java.lang.Exception
import java.lang.NullPointerException


class User(val context: Activity, val iUserContract: IUserContract) {

    val mAuth: FirebaseAuth = FirebaseAuth.getInstance()
    var user: FirebaseUser? = null


    fun signUp(login: String, password: String) {
        mAuth.createUserWithEmailAndPassword(login, password)
                .addOnCompleteListener(context, object : OnCompleteListener<AuthResult> {
                    override fun onComplete(task: Task<AuthResult>) {
                        if (task.isSuccessful) run {
                            user = mAuth.currentUser
                            sendEmailVerification(context, user)
                            iUserContract.refreshError()
                        } else {
                            iUserContract.refreshError(fbExceptionToString(task.exception))
                        }
                    }
                })
    }


    fun signIn(login: String, password: String) {
        mAuth.signInWithEmailAndPassword(login, password)
                .addOnCompleteListener(context, object : OnCompleteListener<AuthResult> {
                    override fun onComplete(task: Task<AuthResult>) {
                        if (task.isSuccessful) run {
                            user = mAuth.currentUser
                            iUserContract.refreshError()
                            iUserContract.signInSuccessful()
                        } else {
                            iUserContract.refreshError(fbExceptionToString(task.exception))
                        }
                    }
                })
    }


    fun sendEmailVerification(context: Activity, user: FirebaseUser?) {
        if (user != null) {
            user.sendEmailVerification()
                    .addOnCompleteListener(context) {
                        if (it.isSuccessful) {
                            iUserContract.eMailHasSent()
                        }
                    }
        }
    }


    private fun fbExceptionToString(exception: Exception?): String? {
        if (exception != null) {
            if (exception is FirebaseAuthException) {
                when (exception.errorCode) {
                    Constants.FIREBASE_EXCEPTION_WEAK_PASSWORD -> {
                        iUserContract.clearET(false, true, true)
                        return context.resources.getString(R.string.passwordException)
                    }
                    Constants.FIREBASE_EXCEPTION_INVALID_EMAIL -> {
                        iUserContract.clearET(true, true, true)
                        return context.resources.getString(R.string.credentialException)
                    }
                    Constants.FIREBASE_EXCEPTION_EMAIL_ALREADY_IN_USE -> {
                        iUserContract.clearET(false, true, true)
                        return context.resources.getString(R.string.emailAlreadyInUseException)
                    }
                    Constants.FIREBASE_EXCEPTION_USER_NOT_FOUND -> {
                        iUserContract.clearET(false, true, true)
                        return context.resources.getString(R.string.wrongLogPass)
                    }
                    Constants.FIREBASE_EXCEPTION_WRONG_PASSWORD -> {
                        iUserContract.clearET(false, true, true)
                        return context.resources.getString(R.string.wrongLogPass)
                    }

                }
            }
            if (exception is FirebaseNetworkException) {
                return context.resources.getString(R.string.noNetworkException)
            }
        }
        return exception!!.message
    }

}