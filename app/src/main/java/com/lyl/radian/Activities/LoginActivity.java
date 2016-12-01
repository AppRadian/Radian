package com.lyl.radian.Activities;

import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.lyl.radian.R;
import com.lyl.radian.Utilities.Constants;

public class LoginActivity extends FirebaseActivity {
    //Push
    Button loginBtn;
    EditText email, password;
    ImageView imageView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Resources r = getResources();
        float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 150, r.getDisplayMetrics());
        imageView = ((ImageView) findViewById(R.id.imageView2));
        imageView.setImageBitmap(Constants.decodeBitmap(r, R.drawable.logo, (int)px, (int)px));

        loginBtn = (Button) findViewById(R.id.login);
        email = (EditText) findViewById(R.id.emailTxt);
        password = (EditText) findViewById(R.id.passwordTxt);

        loginBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                String e = email.getText().toString();
                String pw = password.getText().toString();

                if(validateEmail(e) == true && pw.length() > 0)
                    signInUser(e, Constants.DEFAULTPASSWORD);
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
