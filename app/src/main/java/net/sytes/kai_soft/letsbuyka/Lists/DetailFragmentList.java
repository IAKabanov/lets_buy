package net.sytes.kai_soft.letsbuyka.Lists;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import net.sytes.kai_soft.letsbuyka.CRUDdb;
import net.sytes.kai_soft.letsbuyka.IMenuContract;
import net.sytes.kai_soft.letsbuyka.R;
import net.sytes.kai_soft.letsbuyka.IListActivityContract;

/**
 * Created by Лунтя on 06.06.2018.
 */

public class DetailFragmentList extends Fragment implements IMenuContract {

    //Button insertBtn, backToListBtn, deleteBtn;
    EditText etName;
    List list;
    //DataBase dbProduct;
    IListActivityContract iListActivityContract;

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
        if (context instanceof IListActivityContract) {
            iListActivityContract = (IListActivityContract) context;
        }
    }


    @Override
    public void onDetach() {
        iListActivityContract = null;
        super.onDetach();
    }
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
            CRUDdb.Companion.updateTableLists(updateble);
            iListActivityContract.onDetailFragmentButtonClick();
        } else {
            CRUDdb.Companion.insertToTableLists(etName.getText().toString());
            iListActivityContract.onDetailFragmentButtonClick();
        }
    }
    @Override
    public void deletePressed(){
        Bundle bundle = getArguments();
        CRUDdb.Companion.deleteItemLists((List) bundle.getSerializable("list"));
        iListActivityContract.onDetailFragmentButtonClick();
    }
}
