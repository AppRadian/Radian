package com.lyl.radian.Activities;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import java.io.IOException;

import com.lyl.radian.Adapter.PlacesAutoCompleteAdapter;
import com.lyl.radian.R;
import com.lyl.radian.Utilities.Account;
import com.lyl.radian.Utilities.Constants;

/**
 * Created by Ludwig on 29.10.2016.
 */

public class SettingsActivity extends Activity {
    Button save;
    String city;
    Account account;
    AutoCompleteTextView location;
    EditText language;
    EditText currentPassword;
    EditText password;
    EditText passwordConfirm;
    ImageView profilePic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        account = (Account) getApplication();
        save = (Button) findViewById(R.id.Save);
        location = (AutoCompleteTextView) findViewById(R.id.changeLocation);
        language = (EditText) findViewById(R.id.changeLanguage);
        currentPassword = (EditText)findViewById(R.id.password);
        password = (EditText) findViewById(R.id.changePassword);
        passwordConfirm = (EditText) findViewById(R.id.ConfirmPassword);
        profilePic = (ImageView) findViewById(R.id.changeProfilePic);

        /**
        profilePic.setImageBitmap(account.getSelf().getProfilePic());
        location.setText(account.getSelf().getLocation());
        language.setText(account.getSelf().getLanguage());
        city = account.getSelf().getLocation();
         **/

        final AutoCompleteTextView autocompleteView = (AutoCompleteTextView) findViewById(R.id.changeLocation);
        autocompleteView.setAdapter(new PlacesAutoCompleteAdapter(this, R.layout.autocomplete_list_item));

        autocompleteView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                city = parent.getItemAtPosition(position).toString();
            }
        });

        profilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), 1);
            }
        });


            save.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    String loc = location.getText().toString();
                    String lang = language.getText().toString();
                    String currentPw = currentPassword.getText().toString();
                    String pw = password.getText().toString();
                    String pwConfirm = passwordConfirm.getText().toString();

                    if (loc.length() > 0 && city.equals(location.getText().toString())) {

                    } else
                            location.setError("Select existing location");

                    if (lang.length() > 0) {

                    }

                    if (currentPw.length() > 0 && pw.length() > 0 && pwConfirm.length() > 0) {
                        if (pw.equals(pwConfirm)) {

                        } else
                                passwordConfirm.setError("Passwords do not match");
                    }

                    if(city.equals(location.getText().toString()) && pw.equals(pwConfirm))
                        finish();
                }
            });
        };

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == 1) {
                Uri uri = data.getData();

                Resources r = this.getResources();
                int height = r.getDisplayMetrics().heightPixels / 2;
                int width = r.getDisplayMetrics().widthPixels;

                
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
