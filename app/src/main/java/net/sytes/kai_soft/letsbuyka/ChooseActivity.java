package net.sytes.kai_soft.letsbuyka;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import net.sytes.kai_soft.letsbuyka.Lists.ListsListActivity;
import net.sytes.kai_soft.letsbuyka.ProductModel.ProductsActivity;

/**
 * Created by Лунтя on 12.06.2018.
 */

public class ChooseActivity extends AppCompatActivity implements View.OnClickListener{

    Button listBtn, productBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose);
        listBtn = findViewById(R.id.Lists);
        productBtn = findViewById(R.id.Products);
        listBtn.setOnClickListener(this);
        productBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent = null;
        switch (v.getId()){

            case R.id.Lists:
                intent = new Intent(ChooseActivity.this, ListsListActivity.class);
                break;
            case R.id.Products:
                intent = new Intent(ChooseActivity.this, ProductsActivity.class);
                break;
        }
        startActivity(intent);

    }
}
