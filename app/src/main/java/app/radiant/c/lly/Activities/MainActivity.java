package app.radiant.c.lly.Activities;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;

import app.radiant.c.lly.R;
import app.radiant.c.lly.Utilities.Account;
import app.radiant.c.lly.Utilities.Constants;


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
        setContentView(R.layout.activity_splash_screen);

        Resources r = getResources();
        float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 150, r.getDisplayMetrics());
        imageView = ((ImageView) findViewById(R.id.imageView2));
        imageView.setImageBitmap(Constants.decodeBitmap(r, R.drawable.logo, (int) px, (int) px));

        SharedPreferences sp = getSharedPreferences("login_state", Activity.MODE_PRIVATE);
        final SharedPreferences.Editor editor = sp.edit();

        if(!sp.getString("email","").equals("") && !sp.getString("accessToken","").equals(""))
            account.loginWithAccessToken(this, sp.getString("email", ""), sp.getString("accessToken", ""));
        else {
            setContentView(R.layout.activity_main);

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
