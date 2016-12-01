package app.radiant.c.lly.Activities;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import java.io.IOException;

import app.radiant.c.lly.Adapter.PlacesAutoCompleteAdapter;
import app.radiant.c.lly.R;
import app.radiant.c.lly.Utilities.Account;
import app.radiant.c.lly.Utilities.Constants;

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

        profilePic.setImageBitmap(account.getSelf().getProfilePic());
        location.setText(account.getSelf().getLocation());
        language.setText(account.getSelf().getLanguage());
        city = account.getSelf().getLocation();

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
                            account.getSelf().setLocation(loc);
                            account.editLocation(SettingsActivity.this, loc);
                    } else
                            location.setError("Select existing location");

                    if (lang.length() > 0) {
                            account.getSelf().setLanguage(lang);
                            account.editLanguage(SettingsActivity.this, lang);
                    }

                    if (currentPw.length() > 0 && pw.length() > 0 && pwConfirm.length() > 0) {
                        if (pw.equals(pwConfirm)) {
                                account.editPassword(SettingsActivity.this, currentPw, pw);
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

                try {
                    account.getSelf().getProfilePic().recycle();
                    account.getSelf().setProfilePic(null);
                    account.getSelf().setProfilePic(Constants.decodeBitmap(this, uri, width, (int)height));
                    profilePic.setImageBitmap(account.getSelf().getProfilePic());
                    account.uploadProfilePic(this, account.getSelf().getProfilePic());
                } catch (IOException e) {
                    e.printStackTrace();
                    return;
                }
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
