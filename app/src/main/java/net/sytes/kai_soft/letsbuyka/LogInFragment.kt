package net.sytes.kai_soft.letsbuyka

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup


class LogInFragment: Fragment() {

    companion object {
        const val myTag = "letsbuy_logInFragment"
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        Log.i(myTag, "onCreateView()")
        if (inflater != null){
            val rootView = inflater.inflate(R.layout.fragment_log_in, container, false)
            return rootView
        }
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
    }
}