package com.lyl.radian.Activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnPausedListener;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.lyl.radian.Adapter.PlacesAutoCompleteAdapter;
import com.lyl.radian.R;
import com.lyl.radian.Utilities.Account;
import com.lyl.radian.Constants.Constant;

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
    ImageView profilePicView;
    FirebaseStorage storage;
    StorageReference storageRef;
    String profilePic;
    ProgressDialog barProgressDialog;

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
        profilePicView = (ImageView) findViewById(R.id.changeProfilePic);

        location.setText(account.getSelf().getLocation());
        language.setText(account.getSelf().getLanguage());

        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReferenceFromUrl("gs://radian-eb422.appspot.com/" + FirebaseAuth.getInstance().getCurrentUser().getPhotoUrl());
        Glide.with(SettingsActivity.this)
                .using(new FirebaseImageLoader())
                .load(storageRef)
                .placeholder(R.drawable.blank_profile_pic)
                .dontAnimate()
                .into(profilePicView);

        //location.setText(account.get.getLocation());
        //language.setText(account.getSelf().getLanguage());
        city = account.getSelf().getLocation();

        final AutoCompleteTextView autocompleteView = (AutoCompleteTextView) findViewById(R.id.changeLocation);
        autocompleteView.setAdapter(new PlacesAutoCompleteAdapter(this, R.layout.autocomplete_list_item));

        autocompleteView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                city = parent.getItemAtPosition(position).toString();
            }
        });

        profilePicView.setOnClickListener(new View.OnClickListener() {
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
                        FirebaseDatabase.getInstance().getReference(Constant.USER_DB)
                                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                .child("location").setValue(location.getText().toString()).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                account.getSelf().setLocation(location.getText().toString());
                                final Location ownLocation = Constant.getLocationFromAddress(SettingsActivity.this, location.getText().toString());
                                account.getSelf().setLatitude(ownLocation.getLatitude());
                                account.getSelf().setLongitude(ownLocation.getLongitude());

                                FirebaseDatabase.getInstance().getReference(Constant.USER_DB)
                                        .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                        .child("latitude").setValue(ownLocation.getLatitude()).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        FirebaseDatabase.getInstance().getReference(Constant.USER_DB)
                                                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                                .child("longitude").setValue(ownLocation.getLongitude()).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                finish();
                                            }
                                        });
                                    }
                                });
                            }
                        });
                    } else
                            location.setError("Select existing location");

                    if (lang.length() > 0) {
                        FirebaseDatabase.getInstance().getReference(Constant.USER_DB)
                                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                .child("language").setValue(language.getText().toString()).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                account.getSelf().setLanguage(language.getText().toString());
                            }
                        });
                    }

                    if (currentPw.length() > 0 && pw.length() > 0 && pwConfirm.length() > 0) {
                        if (pw.equals(pwConfirm)) {
                            //TODO Ersetzen mit echtem Passwot
                            FirebaseAuth.getInstance().getCurrentUser().updatePassword(Constant.DEFAULTPASSWORD);
                        } else
                                passwordConfirm.setError("Passwords do not match");
                    }

                    if(city.equals(location.getText().toString()) && pw.equals(pwConfirm))
                        ;//finish();
                }
            });
        };

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == 1) {
                Uri uri = data.getData();
                uploadImage(uri);
            }
        }
    }

    public void uploadImage(final Uri uri) {
        barProgressDialog = new ProgressDialog(SettingsActivity.this);

        barProgressDialog.setTitle("Uploading Image ...");
        barProgressDialog.setProgressStyle(barProgressDialog.STYLE_HORIZONTAL);
        barProgressDialog.setCancelable(false);
        barProgressDialog.setProgress(0);
        barProgressDialog.show();

        // Create the file metadata
        final StorageMetadata metadata = new StorageMetadata.Builder()
                .setContentType("image/jpeg")
                .build();

        final String profilePic = "images/" + FirebaseAuth.getInstance().getCurrentUser().getEmail() + System.currentTimeMillis();
        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setPhotoUri(Uri.parse(profilePic))
                .build();
        FirebaseAuth.getInstance().getCurrentUser().updateProfile(profileUpdates).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                FirebaseDatabase.getInstance().getReference(Constant.USER_DB).child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("profilePic").setValue(profilePic);

                FirebaseStorage storage = FirebaseStorage.getInstance();
                final StorageReference storageRef = storage.getReferenceFromUrl("gs://radian-eb422.appspot.com/" + profilePic);
                // Upload file and metadata to the path 'images/mountains.jpg'
                UploadTask uploadTask = storageRef.putFile(uri, metadata);

                // Listen for state changes, errors, and completion of the upload.
                uploadTask.addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                        double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                        barProgressDialog.setProgress((int)progress);
                        System.out.println("Upload is " + progress + "% done");
                    }
                }).addOnPausedListener(new OnPausedListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onPaused(UploadTask.TaskSnapshot taskSnapshot) {
                        System.out.println("Upload is paused");
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle unsuccessful uploads
                    }
                }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        // Handle successful uploads on complete
                        barProgressDialog.dismiss();
                        SettingsActivity.this.storageRef.delete();
                        Glide.with(SettingsActivity.this)
                                .using(new FirebaseImageLoader())
                                .load(storageRef)
                                .placeholder(R.drawable.blank_profile_pic)
                                .dontAnimate()
                                .into(profilePicView);
                    }
                });
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
