package net.sytes.kai_soft.letsbuyka;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import net.sytes.kai_soft.letsbuyka.ProductModel.Product;

import java.util.Dictionary;
import java.util.Map;

public class FBActivity extends AppCompatActivity implements View.OnClickListener {
    EditText mEditText, mETLogin, mETPass;
    TextView mTextView, mHaveAnotherMessage;
    Button mButton, mBtnSignUp, mBtnSignIn;
    DatabaseReference messageRef, anotherMessageRef, productMessage;
    RadioButton mRBMess, mRBAnMess, mRBProduct;
    String messageValue, anotherMessageValue;
    FirebaseAuth mAuth;

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
                if (mRBMess.isChecked()) {
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
                if (mRBAnMess.isChecked()) {
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
                if (mRBProduct.isChecked()) {
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

        FirebaseUser currentUser = mAuth.getCurrentUser();
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
        mETLogin = findViewById(R.id.fbLogin);
        mETPass = findViewById(R.id.fbPassword);
        mBtnSignUp = findViewById(R.id.fbSignUp);
        mBtnSignIn = findViewById(R.id.fbSignIn);
        mButton.setOnClickListener(this);
        mBtnSignUp.setOnClickListener(this);
        mBtnSignIn.setOnClickListener(this);
        mRBMess.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    mTextView.setText(messageValue);
                    mHaveAnotherMessage.setText("");
                }
            }
        });
        mRBAnMess.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    mTextView.setText(anotherMessageValue);
                    mHaveAnotherMessage.setText("");
                }
            }
        });
        mAuth = FirebaseAuth.getInstance();

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fbBtn:
                if (mEditText.getText().length() != 0) {
                    if (mRBMess.isChecked()) {
                        messageRef.setValue(mEditText.getText().toString());
                    } else {
                        anotherMessageRef.setValue(mEditText.getText().toString());
                    }


                } else {
                    if (mRBMess.isChecked()) {
                        messageRef.setValue("Hello Kitty!");
                    } else {
                        anotherMessageRef.setValue("Hello Kitty!");
                    }
                }
                break;









            case R.id.fbSignIn:
                FirebaseUser user = mAuth.getCurrentUser();
                if (user == null){
                    mAuth.signInWithEmailAndPassword(mETLogin.getText().toString(),
                            mETPass.getText().toString())
                            .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        FirebaseUser user = mAuth.getCurrentUser();
                                    }
                                    else {
                                        // If sign in fails, display a message to the user.
                                        Log.w("asd", "signInWithEmail:failure", task.getException());
                                    }
                                }
                            });
                }
                user.getIdToken(true);
                break;










            case R.id.fbSignUp:
                mAuth.createUserWithEmailAndPassword(mETLogin.getText().toString(),
                        mETPass.getText().toString())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = mAuth.getCurrentUser();

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("asd", "signInWithEmail:failure", task.getException());

                        }
                    }
                });


                break;
        }
    }
}
