package com.lyl.radian.Activities;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.bumptech.glide.Glide;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.lyl.radian.Adapter.CustomAdapterSearch;
import com.lyl.radian.DialogFragments.BidDialog;
import com.lyl.radian.Fragments.BieteFragment;
//import comhelpingandchanging.facebook.httpswww.changetogether.Fragments.HelpingLocationsFragment;
import com.lyl.radian.Fragments.HomeFragment;
import com.lyl.radian.Fragments.InboxFragment;
import com.lyl.radian.Fragments.OwnBidsFragment;
import com.lyl.radian.Fragments.SearchFragment;
import com.lyl.radian.Fragments.OwnProfileFragment;
import com.lyl.radian.R;
import com.lyl.radian.Utilities.Account;
import com.lyl.radian.Utilities.Constants;
import com.lyl.radian.Utilities.RequestHandler;

public class MainAppActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    Account account;
    HomeFragment homeFragment;
    SearchFragment searchFragment;
    BieteFragment bieteFragment;
    OwnBidsFragment ownProfileFragment;
    OwnProfileFragment tab;
    InboxFragment inboxFragment;
    public View header;
    public NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_app);

        account = (Account) getApplication();
        account.fm = getSupportFragmentManager();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.nav_home);

        header = navigationView.getHeaderView(0);
        ((TextView) header.findViewById(R.id.profEmail)).setText(FirebaseAuth.getInstance().getCurrentUser().getEmail());
        ((TextView) header.findViewById(R.id.profLocation)).setText("");

        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReferenceFromUrl("gs://radian-eb422.appspot.com/" + FirebaseAuth.getInstance().getCurrentUser().getPhotoUrl());

        Glide.with(MainAppActivity.this)
                .using(new FirebaseImageLoader())
                .load(storageRef)
                .placeholder(R.drawable.blank_profile_pic)
                .dontAnimate()
                .into(((ImageView) header.findViewById(R.id.profPic)));

        if (savedInstanceState != null) {
            searchFragment = (SearchFragment) account.fm.getFragment(savedInstanceState, "search");
            bieteFragment = (BieteFragment) account.fm.getFragment(savedInstanceState, "biete");
            ownProfileFragment = (OwnBidsFragment) account.fm.getFragment(savedInstanceState, "ownprofile");
            inboxFragment = (InboxFragment) account.fm.getFragment(savedInstanceState, "inbox");
        }
        else {
            homeFragment = new HomeFragment();
            searchFragment = new SearchFragment();
            bieteFragment = new BieteFragment();
            tab = new OwnProfileFragment();
            inboxFragment = new InboxFragment();
        }

        ((FloatingActionButton)findViewById(R.id.fab2)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BidDialog add = new BidDialog();
                add.show(account.fm, "Biete Dialog");
            }
        });

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        account.fm.beginTransaction().replace(R.id.content_frame, homeFragment, "home").commit();
        Log.e("id", FirebaseInstanceId.getInstance().getToken());
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }
        else if (account.fm.getBackStackEntryCount() > 0) {
            account.fm.popBackStack();
        } else {
            //super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.search_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_search) {

            account.fm.beginTransaction().replace(R.id.content_frame, searchFragment, "search").addToBackStack("search").commit();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if(id == R.id.nav_home){
            account.fm.beginTransaction().replace(R.id.content_frame, homeFragment, "home").addToBackStack("home").commit();
        }
        /**else if (id == R.id.nav_biete) {
            account.fm.beginTransaction().replace(R.id.content_frame, bieteFragment, "biete").addToBackStack("biete").commit();
        } **/else if (id == R.id.nav_own_profile) {
            tab = new OwnProfileFragment();
            account.fm.beginTransaction().replace(R.id.content_frame, tab, "ownprofile").addToBackStack("ownprofile").commit();
        } else if (id == R.id.nav_inbox) {
            account.fm.beginTransaction().replace(R.id.content_frame, inboxFragment, "helping").addToBackStack("helping").commit();
        }
        else if(id == R.id.nav_settings){
            Intent i = new Intent(this, SettingsActivity.class);
            startActivity(i);
        }
        else if (id == R.id.nav_logout){
            DatabaseReference regId = FirebaseDatabase.getInstance().getReference(Constants.USER_DB).child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("registrationId");
            regId.setValue(null);

            FirebaseAuth.getInstance().signOut();
            Intent i = new Intent(this, MainActivity.class);
            finishAffinity();
            startActivity(i);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public Double[] getLocationFromAddress(String strAddress){

        Double[] latLong = new Double[2];
        Geocoder coder = new Geocoder(this);
        List<Address> address;

        try {
            address = coder.getFromLocationName(strAddress,1);
            if (address==null) {
                return null;
            }
            Address location=address.get(0);
            latLong[0] = location.getLatitude();
            latLong[1] = location.getLongitude();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return latLong;
    }

    @Override
    protected void onResume() {
        super.onResume();

        FirebaseStorage storage = FirebaseStorage.getInstance();
        //Log.e("url", FirebaseAuth.getInstance().getCurrentUser().getPhotoUrl().toString());
        StorageReference storageRef = storage.getReferenceFromUrl("gs://radian-eb422.appspot.com/" + FirebaseAuth.getInstance().getCurrentUser().getPhotoUrl());
        Glide.with(MainAppActivity.this)
                .using(new FirebaseImageLoader())
                .load(storageRef)
                .placeholder(R.drawable.blank_profile_pic)
                .dontAnimate()
                .into(((ImageView) header.findViewById(R.id.profPic)));
        ((TextView) header.findViewById(R.id.profEmail)).setText(FirebaseAuth.getInstance().getCurrentUser().getEmail());
        ((TextView) header.findViewById(R.id.profLocation)).setText("");
    }
}
