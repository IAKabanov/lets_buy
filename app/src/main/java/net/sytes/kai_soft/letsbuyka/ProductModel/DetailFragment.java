package net.sytes.kai_soft.letsbuyka.ProductModel;

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

/**
 * Created by Лунтя on 30.04.2018.
 */

public class DetailFragment extends Fragment implements IMenuContract {

    //Button insertBtn, backToListBtn, deleteBtn;
    EditText etName, etDescr;//, etPhoto;

    IProductListActivityContract IProductListActivityContract;



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);

        //insertBtn = rootView.findViewById(R.id.insertBtn);
        //backToListBtn = rootView.findViewById(R.id.backToListBtn);
        //deleteBtn = rootView.findViewById(R.id.deleteBtn);

        etName = rootView.findViewById(R.id.etName);
        etDescr = rootView.findViewById(R.id.etDescr);
//        etPhoto = rootView.findViewById(R.id.etPhoto);




        //dbProduct = Application.getDB(); //new DataBase(this.getContext());

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();

        makeEditable(true);

        emptyEditText();

        //backToListBtn.setOnClickListener(this);
        //insertBtn.setOnClickListener(this);
        //deleteBtn.setVisibility(View.GONE);

        Bundle bundle = getArguments();

        if (bundle.getBoolean("editable")) {
            //insertBtn.setText(R.string.save);

        } else {
            Product product = (Product) bundle.getSerializable("product");

            etName.setText(product.getName());
            etDescr.setText(product.getDescription());
  //          etPhoto.setText(product.getFirstImagePath());

            //makeEditable(false);

            //insertBtn.setText(R.string.edit);

            //deleteBtn.setOnClickListener(this);
            //deleteBtn.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof IProductListActivityContract) {
            IProductListActivityContract = (IProductListActivityContract) context;
        }
    }

    @Override
    public void onDetach() {
        IProductListActivityContract = null;
        super.onDetach();
    }

    private boolean isEditable() {
        return etName.isEnabled();
    }

    private boolean isNewProduct() {
        Bundle bundle = getArguments();
        if (bundle.getBoolean("editable") == true) {
            return true;
        }
        return false;
    }

    private void makeEditable(boolean state) {
        etName.setEnabled(state);
        etDescr.setEnabled(state);
//        etPhoto.setEnabled(state);
    }

    private void emptyEditText() {
        etName.setText("");
        etDescr.setText("");
//        etPhoto.setText("");
    }

    private Product toUpdate(){
        Bundle bundle = getArguments();
        Product fromBundle = (Product) bundle.getSerializable("product");
        Product toUpdate = new Product(fromBundle.getId(),
                etName.getText().toString(),
                etDescr.getText().toString());
        return toUpdate;
    }


    @Override
    public void savePressed() {

        if (isNewProduct() == false) {
            Product updateble = toUpdate();
            CRUDdb.Companion.updateTableProducts(updateble);
            //IProductListActivityContract.onDetailFragmentButtonClick();
        } else {
            CRUDdb.Companion.insertToTableProducts(etName.getText().toString(),
                    etDescr.getText().toString());
            //IProductListActivityContract.onDetailFragmentButtonClick();
        }
    }

    @Override
    public void deletePressed(){
        Bundle bundle = getArguments();
        if (isNewProduct() == false) {
            CRUDdb.Companion.deleteItemProducts((Product) bundle.getSerializable("product"));
            //IProductListActivityContract.onDetailFragmentButtonClick();
        }
    }

    public boolean isNew(){
        return isNewProduct();
    }
}