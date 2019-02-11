package net.sytes.kai_soft.letsbuyka.Lists

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText

import net.sytes.kai_soft.letsbuyka.CRUDdb
import net.sytes.kai_soft.letsbuyka.IMenuContract
import net.sytes.kai_soft.letsbuyka.R
import net.sytes.kai_soft.letsbuyka.IActivityContract

/*  Detail fragment for selected list   */
class DetailFragmentList: Fragment(), IMenuContract {
    companion object {
        private const val myTag = "letsbuy_LDetailFragment"
    }

    private lateinit var etName: EditText
    private lateinit var list: List
    private lateinit var iActivityContract: IActivityContract

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        Log.i(myTag, "onCreateView()")
        val rootView = inflater!!.inflate(R.layout.fragment_lists_detail, container, false)
        etName = rootView.findViewById(R.id.etNameList)

        return rootView
    }

    override fun onResume() {
        Log.i(myTag, "onResume()")
        super.onResume()
        emptyEditText()

        val bundle = arguments
        if (!bundle.getBoolean("editable")){
            list = bundle.getSerializable("list") as List
            etName.setText(list.itemName)
        }
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (context is IActivityContract){
            iActivityContract = context
        }
    }

    private fun emptyEditText(){
        etName.text.clear()
    }

    private fun isNewElement(): Boolean{
        val bundle = arguments
        return bundle.getBoolean("editable")
    }

    private fun toUpdate(): List {
        val bundle = arguments
        val fromBundle = bundle.getSerializable("list") as List
        return List(fromBundle.id, etName.text.toString())
    }

    override fun savePressed() {
        if (!isNewElement()){
            val updatable = toUpdate()
            CRUDdb.updateTableLists(updatable)
            iActivityContract.onDetailFragmentButtonClick()
        } else {
            CRUDdb.insertToTableLists(etName.text.toString())
            iActivityContract.onDetailFragmentButtonClick()
        }
    }

    override fun deletePressed() {
        val bundle = arguments
        CRUDdb.deleteItemLists(bundle.getSerializable("list") as List)
        iActivityContract.onDetailFragmentButtonClick()
    }

    override fun hideKeyboard() {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(etName.windowToken, 0)
    }
}