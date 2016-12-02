package com.lyl.radian.Activities;

import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.lyl.radian.R;
import com.lyl.radian.Utilities.Constants;

/**
 * Created by Yannick on 28.10.2016.
 */

public class RegisterActivity extends FirebaseActivity {

    EditText email;
    EditText password;
    EditText passwordConfirm;
    Button register;
    ImageView imageView;

    private static final String TAG = "RegisterActivity";
    String userEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        Resources r = getResources();
        float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 150, r.getDisplayMetrics());
        imageView = ((ImageView) findViewById(R.id.imageView3));
        imageView.setImageBitmap(Constants.decodeBitmap(r, R.drawable.logo, (int)px, (int)px));

        email = (EditText) findViewById(R.id.emailTxt2);
        password = (EditText) findViewById(R.id.passwordTxt2);
        passwordConfirm = (EditText) findViewById(R.id.passwordConfirmTxt);
        register = (Button) findViewById(R.id.register);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validateEmail(email.getText().toString())) {
                    if (password.getText().toString().length() != 0 && passwordConfirm.getText().toString().length() != 0) {
                        if (password.getText().toString().equals(passwordConfirm.getText().toString()))
                            createUser(email.getText().toString(), Constants.DEFAULTPASSWORD);
                        else
                            Toast.makeText(RegisterActivity.this, "Passwords do not match", Toast.LENGTH_SHORT).show();
                    } else
                        Toast.makeText(RegisterActivity.this, "Password can not be empty", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(RegisterActivity.this, "E-Mail can not be empty / or incorrect form", Toast.LENGTH_SHORT).show();
                    Log.e("FUCK: ", email.toString());
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        imageView.setImageBitmap(null);
    }

    public final static boolean validateEmail(String email) {
        CharSequence cs = email;
        if (cs == null) {
            Log.d("FUCK validate if", cs.toString());
            return false;
        }
        else {
            Log.d("FUCK validate else", cs.toString());
            return android.util.Patterns.EMAIL_ADDRESS.matcher(cs).matches();
        }
    }
}
