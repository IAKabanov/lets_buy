package net.sytes.kai_soft.letsbuyka;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import net.sytes.kai_soft.letsbuyka.ProductModel.Product;

import java.util.Dictionary;
import java.util.Map;

public class FBActivity extends AppCompatActivity {
    EditText mEditText;
    TextView mTextView, mHaveAnotherMessage;
    Button mButton;
    DatabaseReference messageRef, anotherMessageRef, productMessage;
    RadioButton mRBMess, mRBAnMess, mRBProduct;
    String messageValue, anotherMessageValue;

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        messageRef = database.getReference("message");
        messageRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
// This method is called once with the initial value and again
                // whenever data at this location is updated.
                messageValue = dataSnapshot.getValue(String.class);
                if (mRBMess.isChecked()){
                    mTextView.setText(messageValue);
                } else {
                    mHaveAnotherMessage.setText("Есть непрочитанная реклама в \"Message\"");
                }
                String value = dataSnapshot.getValue(String.class);
                mTextView.setText(value);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                mTextView.setText("Не удалось прочитать данные. " + error.toException());
            }
        });

        anotherMessageRef = database.getReference("anotherMessage");
        anotherMessageRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                anotherMessageValue = dataSnapshot.getValue(String.class);
                if (mRBAnMess.isChecked()){
                    mTextView.setText(anotherMessageValue);
                } else {
                    mHaveAnotherMessage.setText("Есть непрочитанная реклама в \"AnotherMessage\"");
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                mTextView.setText("Не удалось прочитать данные. " + error.toException());
            }
        });

        productMessage = database.getReference("product");
        Product product = new Product(1, "Какой-то", "продукт!");
        productMessage.setValue(product);
        productMessage.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Product product = dataSnapshot.getValue(Product.class);
                if (mRBProduct.isChecked()){
                    assert product != null;
                    mTextView.setText(product.toString());
                } else {
                    mHaveAnotherMessage.setText("Есть непрочитанная реклама в \"AnotherMessage\"");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                mTextView.setText("Не удалось прочитать данные. " + databaseError.toException());
            }
        });
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_firebase);
        mEditText = findViewById(R.id.fbET);
        mTextView = findViewById(R.id.fbTV);
        mHaveAnotherMessage = findViewById(R.id.fbTVStatus);
        mButton = findViewById(R.id.fbBtn);
        mRBMess = findViewById(R.id.fbRadioBtnMess);
        mRBAnMess = findViewById(R.id.fbRadioBtnAnothMess);
        mRBProduct = findViewById(R.id.fbRadioBtnProduct);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mEditText.getText().length() != 0) {
                    if (mRBMess.isChecked()){
                        messageRef.setValue(mEditText.getText().toString());
                    } else {
                        anotherMessageRef.setValue(mEditText.getText().toString());
                    }


                } else {
                    if (mRBMess.isChecked()){
                        messageRef.setValue("Hello Kitty!");
                    } else {
                        anotherMessageRef.setValue("Hello Kitty!");
                    }
                }

            }
        });
        mRBMess.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    mTextView.setText(messageValue);
                    mHaveAnotherMessage.setText("");
                }
            }
        });
        mRBAnMess.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    mTextView.setText(anotherMessageValue);
                    mHaveAnotherMessage.setText("");
                }
            }
        });


    }
}
