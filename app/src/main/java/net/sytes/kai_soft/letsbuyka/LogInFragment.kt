package net.sytes.kai_soft.letsbuyka

import android.app.AlertDialog
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*


class LogInFragment : Fragment(), View.OnClickListener, IUserContract {

    companion object {
        const val myTag = "letsbuy_logInFragment"
    }

    private lateinit var logIn: EditText
    private var logInWidth = 0
    private lateinit var password: EditText
    private var passwordWidth = 0
    private lateinit var rePassword: EditText
    private var rePasswordWidth = 0
    private lateinit var llRePassword: LinearLayout
    private lateinit var tvRegister: TextView
    private lateinit var tvForgetPass: TextView
    private lateinit var tvError: TextView
    private lateinit var btnSignIn: Button
    private var isLoggingIn = true
    private lateinit var user: User


    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        Log.i(myTag, "onCreateView()")
        if (inflater != null) {
            val rootView = inflater.inflate(R.layout.fragment_log_in, container, false)
            initViews(rootView)
            var i = 0
            rootView.addOnLayoutChangeListener { _, _, _, _, _, _, _, _, _ ->

                if (i == 0) {
                    logInWidth = logIn.measuredWidth
                    passwordWidth = password.measuredWidth
                    rePasswordWidth = rePassword.measuredWidth
                }
                refreshViews(i)
                i++
            }
            user = User(activity, this)
            return rootView
        }
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    private fun initViews(rootView: View) {
        logIn = rootView.findViewById(R.id.fbLogin)
        password = rootView.findViewById(R.id.fbPassword)
        rePassword = rootView.findViewById(R.id.fbRePassword)
        llRePassword = rootView.findViewById(R.id.fbllRePassword)
        tvError = rootView.findViewById(R.id.fbTVError)
        btnSignIn = rootView.findViewById(R.id.fbSignIn)
        btnSignIn.setOnClickListener(this)
        tvRegister = rootView.findViewById(R.id.fbTvRegister)
        tvRegister.setOnClickListener(this)
        tvForgetPass = rootView.findViewById(R.id.fbTvForgetPass)
        tvForgetPass.setOnClickListener(this)
    }
    private fun getMinWidthET(etViews: Array<EditText>): Int {
        var minWidth = etViews[0].measuredWidth
        for (j in 1 until etViews.size) {
            if ((etViews[j].measuredWidth < minWidth) && (llRePassword.visibility == View.VISIBLE)) {
                minWidth = etViews[j].measuredWidth
            }
        }
        return minWidth
    }
    private fun setViewMargin(view: View, viewWidth: Int, minWidth: Int) {
        if (view.width != 0) {
            val params = view.layoutParams
            params.width = minWidth
            val margParams = params as ViewGroup.MarginLayoutParams
            margParams.leftMargin = viewWidth - minWidth
            view.layoutParams = margParams
        }
    }

    private fun refreshViews(j: Int = 0) {
        if (isLoggingIn) {
            llRePassword.visibility = View.GONE
            btnSignIn.setText(R.string.signIn)
            tvForgetPass.visibility = View.VISIBLE
            tvRegister.setText(R.string.signUpU)
        } else {
            llRePassword.visibility = View.VISIBLE
            btnSignIn.setText(R.string.signUp)
            tvForgetPass.visibility = View.GONE
            tvRegister.setText(R.string.signInU)
        }

        val views = arrayOf(logIn, password, rePassword)
        val viewsWidth = arrayOf(logInWidth, passwordWidth, rePasswordWidth)
        for (k in 0 until views.size) {
            val params = views[k].layoutParams
            params.width = viewsWidth[k]
            views[k].layoutParams = params
        }

        val minWidth = getMinWidthET(views)
        if (j == 0) {
            for ((k, view) in views.withIndex()) {
                setViewMargin(view, viewsWidth[k], minWidth)
            }
        }
    }

    override fun refreshError(newString: String?){

        if (newString == null){
            tvError.visibility = View.GONE
        } else {
            tvError.visibility = View.VISIBLE
            tvError.text = newString
        }
    }

    override fun clearET(logInB: Boolean, passwordB: Boolean, rePasswordB: Boolean){
        if (logInB){
            logIn.text.clear()
        }
        if (passwordB){
            password.text.clear()
        }
        if(rePasswordB){
            rePassword.text.clear()
        }
    }

    override fun eMailHasSent() {
        //val toast = Toast.makeText(activity, "На адрес ${user.user!!.email} отправлено письмо для подтверждения e-mail", Toast.LENGTH_LONG).show()

        val builder = AlertDialog.Builder(context)
        builder.setTitle(R.string.verifyEmail)
                .setMessage("На адрес ${user.user!!.email} отправлено письмо для подтверждения e-mail")
                .setPositiveButton(R.string.ok) { _, _ ->
                    isLoggingIn = true
                    refreshViews()
                }

        val alert = builder.create()
        alert.show()

    }

    override fun onClick(v: View?) {
        tvError.visibility = View.GONE
        if (v != null) {
            when (v.id) {
                R.id.fbTvRegister -> {
                    isLoggingIn = !isLoggingIn
                    refreshViews()
                }
                R.id.fbSignIn -> {
                    if (isLoggingIn) {
                    } else {
                        if (password.text.toString() == rePassword.text.toString()){
                            user.signUp(logIn.text.toString(), password.text.toString())
                        } else {
                            refreshError(resources.getString(R.string.passwordsNotSame))
                            clearET(false, true, true)
                        }
                    }
                }

            }
        }
    }


}