package net.sytes.kai_soft.letsbuyka.ProductModel

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import net.sytes.kai_soft.letsbuyka.*

import net.sytes.kai_soft.letsbuyka.Lists.List

class DetailFragment: Fragment(), IMenuContract {


    companion object {
        private const val myTag = "letsbuy_PRdetFragment"
    }

    private lateinit var etName: EditText
    private lateinit var etDesct: EditText
    private lateinit var list: List
    private lateinit var iActivityContract: IActivityContract
    private lateinit var iPRContract: IPRContract

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        Log.i(myTag, "onCreateView()")
        val rootView = inflater!!.inflate(R.layout.fragment_detail, container, false)
        etName = rootView.findViewById(R.id.etName)
        etDesct = rootView.findViewById(R.id.etDescr)
        return rootView
    }

    override fun onResume() {
        Log.i(myTag, "onResume()")
        super.onResume()

        emptyEditText()

        val bundle = arguments
        if (!bundle.getBoolean("editable")){
            val product = bundle.getSerializable(Constants.PRODUCT) as Product

            etName.setText(product.name)
            etDesct.setText(product.description)
        }
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (context is IActivityContract){
            iActivityContract = context
        }
        if (context is IPRContract){
            iPRContract = context
        }
    }

    private fun emptyEditText(){
        etName.text.clear()
        etDesct.text.clear()
    }

    fun isNewElement(): Boolean{
        val bundle = arguments
        return bundle.getBoolean(Constants.EDITABLE)
    }

    private fun toUpdate(): Product {
        val bundle = arguments
        val fromBundle = bundle.getSerializable(Constants.PRODUCT) as Product
        return Product(fromBundle.id, etName.text.toString(), etDesct.text.toString())
    }

    override fun savePressed() {
        if (!isNewElement()){
            val updatable = toUpdate()
            CRUDdb.updateTableProducts(updatable)
            iActivityContract.onDetailFragmentButtonClick()
        } else {
            CRUDdb.insertToTableProducts(etName.text.toString(),
                    etDesct.text.toString())
            iActivityContract.onDetailFragmentButtonClick()
        }
    }

    override fun deletePressed() {
        val bundle = arguments
        CRUDdb.deleteItemProducts(bundle.getSerializable(Constants.PRODUCT) as Product)
        iActivityContract.onDetailFragmentButtonClick()
    }

    override fun hideKeyboard() {

        Log.i(myTag, "hideKeyboard()")
        try{
            val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(etName.windowToken, 0)
        }
        catch (e: NullPointerException){}

    }

}