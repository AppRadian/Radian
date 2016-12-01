package app.radiant.c.lly.Activities;

import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import app.radiant.c.lly.Adapter.CustomAdapterSearch;
import app.radiant.c.lly.Fragments.BieteFragment;
//import comhelpingandchanging.facebook.httpswww.changetogether.Fragments.HelpingLocationsFragment;
import app.radiant.c.lly.Fragments.HomeFragment;
import app.radiant.c.lly.Fragments.InboxFragment;
import app.radiant.c.lly.Fragments.OwnBidsFragment;
import app.radiant.c.lly.Fragments.SearchFragment;
import app.radiant.c.lly.Fragments.OwnProfileFragment;
import app.radiant.c.lly.NetworkUtilities.MyFirebaseInstanceIDService;
import app.radiant.c.lly.R;
import app.radiant.c.lly.Utilities.Account;
import app.radiant.c.lly.Utilities.Constants;

public class MainAppActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    Account account;
    HomeFragment homeFragment;
    SearchFragment searchFragment;
    BieteFragment bieteFragment;
    OwnBidsFragment ownProfileFragment;
    OwnProfileFragment tab;
    InboxFragment inboxFragment;
    View header;


    public NavigationView navigationView;

    public ListView searches;
    public ArrayList<String[]> listItems = new ArrayList<String[]>();
    public CustomAdapterSearch adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_app);

        account = (Account) getApplication();
        account.fm = getSupportFragmentManager();

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

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.nav_home);

        header = navigationView.getHeaderView(0);
        ((ImageView) header.findViewById(R.id.profPic)).setImageBitmap(account.getSelf().getProfilePic());
        ((TextView) header.findViewById(R.id.profEmail)).setText(account.getSelf().getEmail());
        ((TextView) header.findViewById(R.id.profLocation)).setText(account.getSelf().getLocation());

        account.fm.beginTransaction().replace(R.id.content_frame, homeFragment, "home").addToBackStack("home").commit();
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
        } else if (id == R.id.nav_biete) {
            account.fm.beginTransaction().replace(R.id.content_frame, bieteFragment, "biete").addToBackStack("biete").commit();
        } else if (id == R.id.nav_own_profile) {
            tab = new OwnProfileFragment();
            account.fm.beginTransaction().replace(R.id.content_frame, tab, "ownprofile").addToBackStack("ownprofile").commit();
        } else if (id == R.id.nav_inbox) {
            account.fm.beginTransaction().replace(R.id.content_frame, inboxFragment, "helping").addToBackStack("helping").commit();
        }
        else if (id == R.id.nav_logout){
            account.logout(this);
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

        ((ImageView) header.findViewById(R.id.profPic)).setImageBitmap(account.getSelf().getProfilePic());
        ((TextView) header.findViewById(R.id.profEmail)).setText(account.getSelf().getEmail());
        ((TextView) header.findViewById(R.id.profLocation)).setText(account.getSelf().getLocation());
    }
}
