package com.lyl.radian.Activities;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.lyl.radian.R;
import com.lyl.radian.Utilities.Constants;


/**
 * Created by Yannick on 28.10.2016.
 */

public class MainActivity extends FirebaseActivity {

    Button login;
    Button register;
    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Resources r = getResources();
        float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 150, r.getDisplayMetrics());

        setContentView(R.layout.activity_main);
        imageView = ((ImageView) findViewById(R.id.imageView2));
        imageView.setImageBitmap(Constants.decodeBitmap(r, R.drawable.logo, (int) px, (int) px));

        ((ImageView) findViewById(R.id.imageView2)).setImageBitmap(Constants.decodeBitmap(r, R.drawable.logo, (int) px, (int) px));

        login = (Button) findViewById(R.id.loginBtn);
        register = (Button) findViewById(R.id.registerBtn);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent login = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(login);
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent register = new Intent(MainActivity.this, RegisterActivity.class);
                startActivity(register);
            }
        });

    }

    @Override
    public void onBackPressed() {
        return;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        imageView.setImageBitmap(null);
    }
}
