package net.sytes.kai_soft.letsbuyka.Lists;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import net.sytes.kai_soft.letsbuyka.Application;
import net.sytes.kai_soft.letsbuyka.CRUDdb;
import net.sytes.kai_soft.letsbuyka.ProductModel.IProductListActivityContract;
import net.sytes.kai_soft.letsbuyka.ProductModel.Product;
import net.sytes.kai_soft.letsbuyka.R;

/**
 * Created by Лунтя on 06.06.2018.
 */

public class DetailFragmentList extends Fragment implements IListsDetailContract {

    //Button insertBtn, backToListBtn, deleteBtn;
    EditText etName;
    List list;
    //DataBase dbProduct;
    IListsListActivityContract iListsListActivityContract;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_lists_detail, container, false);

        /*insertBtn = rootView.findViewById(R.id.insertListBtn);
        backToListBtn = rootView.findViewById(R.id.backToListListBtn);
        deleteBtn = rootView.findViewById(R.id.deleteListBtn);*/

        etName = rootView.findViewById(R.id.etNameList);


        //dbProduct = Application.getDB(); //new DataBase(this.getContext());

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();

        makeEditable(true);

        emptyEditText();

        /*backToListBtn.setOnClickListener(this);
        insertBtn.setOnClickListener(this);
        deleteBtn.setVisibility(View.GONE);*/

        Bundle bundle = getArguments();

        if (bundle.getBoolean("editable")) {
            //insertBtn.setText(R.string.save);

        } else {
            list = (List) bundle.getSerializable("list");

            etName.setText(list.getItemName());


            makeEditable(true);

            /*insertBtn.setText(R.string.edit);

            deleteBtn.setOnClickListener(this);
            deleteBtn.setVisibility(View.VISIBLE);*/
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof IListsListActivityContract) {
            iListsListActivityContract = (IListsListActivityContract) context;
        }
    }


    @Override
    public void onDetach() {
        iListsListActivityContract = null;
        super.onDetach();
    }

    /*@Override
    public void onClick(View v) {
        switch (v.getId()) {
           case (R.id.insertListBtn):
                //insertBtn.setText(R.string.save);
                if (isEditable() == false) {
                    makeEditable(true);
                } else {
                    if (isNewElement() == false) {
                        List updateble = toUpdate();
                        CRUDdb.updateTableLists(updateble);
                        iListsListActivityContract.onListDetailFragmentButtonClick();
                    } else {
                        CRUDdb.insertToTableLists(etName.getText().toString());
                        iListsListActivityContract.onListDetailFragmentButtonClick();
                    }
                }
                break;
            case (R.id.backToListListBtn):
                iListsListActivityContract.onListDetailFragmentButtonClick();
                break;
            case (R.id.deleteListBtn):
                Bundle bundle = getArguments();
                CRUDdb.deleteItemLists((List) bundle.getSerializable("list"));
                iListsListActivityContract.onListDetailFragmentButtonClick();
                break;
        }
    }*/

    private boolean isEditable() {
        return etName.isEnabled();
    }

    private void makeEditable(boolean state) {
        etName.setEnabled(state);
    }

    private void emptyEditText() {
        etName.setText("");
    }

    private boolean isNewElement() {
        Bundle bundle = getArguments();
        return bundle.getBoolean("editable") == true;
    }

    private List toUpdate() {
        Bundle bundle = getArguments();
        List fromBundle = (List) bundle.getSerializable("list");
        List toUpdate = new List(fromBundle.getId(),
                etName.getText().toString());
        return toUpdate;
    }

    @Override
    public void savePressed() {
        if (isNewElement() == false) {
            List updateble = toUpdate();
            CRUDdb.updateTableLists(updateble);
            iListsListActivityContract.onListDetailFragmentButtonClick();
        } else {
            CRUDdb.insertToTableLists(etName.getText().toString());
            iListsListActivityContract.onListDetailFragmentButtonClick();
        }
    }
    @Override
    public void deletePressed(){
        Bundle bundle = getArguments();
        CRUDdb.deleteItemLists((List) bundle.getSerializable("list"));
        iListsListActivityContract.onListDetailFragmentButtonClick();
    }

    @Override
    public String getListName(){
        return list.getItemName();
    }
}
