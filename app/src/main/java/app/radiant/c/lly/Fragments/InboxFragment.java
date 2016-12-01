package app.radiant.c.lly.Fragments;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

import app.radiant.c.lly.Activities.ChatActivity;
import app.radiant.c.lly.R;
import app.radiant.c.lly.Utilities.Account;


/**
 * Created by len13 on 11.11.2016.
 *
 * https://www.firebase.com/docs/android/quickstart.html
 */

public class InboxFragment extends Fragment {

    View view;
    Activity callingActivity;
    Account account;
    ListView chats;

    ArrayList<String> exampleContent = new ArrayList<>();
    ArrayAdapter<String> adapter;

    // Firebase instance variables
    /*private static final String TAG = "EmailPassword";
    private FirebaseAuth userAuth;
    private FirebaseAuth.AuthStateListener userAuthListener;
    String username;*/

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_inbox, container, false);
        callingActivity = getActivity();
        account = (Account) callingActivity.getApplication();

        // Code
        chats = (ListView) view.findViewById(R.id.userChatList);

        TabLayout tabLayout = (TabLayout) getActivity().findViewById(R.id.tab_layout);
        tabLayout.setVisibility(TabLayout.GONE);
        ((FloatingActionButton) getActivity().findViewById(R.id.fab)).setVisibility(View.GONE);

        // Add example content to array
        exampleContent.add("Chat 1");
        exampleContent.add("Chat 2");
        //exampleContent.add(username);

        adapter = new ArrayAdapter<String>(callingActivity, android.R.layout.simple_list_item_1, exampleContent);
        chats.setAdapter(adapter);

        chats.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(getActivity(), ChatActivity.class);
                startActivity(i);
            }
        });

        return view;
    }

    public void refresh(){
        RelativeLayout content = (RelativeLayout) getActivity().findViewById(R.id.content_main_app);
        CoordinatorLayout.LayoutParams p = (CoordinatorLayout.LayoutParams) content.getLayoutParams();
        p.setBehavior(new AppBarLayout.ScrollingViewBehavior());
        content.setLayoutParams(p);

        ((CollapsingToolbarLayout)getActivity().findViewById(R.id.collapsing_toolbar)).setTitleEnabled(false);
        ((TextView)getActivity().findViewById(R.id.toolbar_title)).setText("Chats");
        ((ImageView)getActivity().findViewById(R.id.ownProfilePic)).setImageBitmap(null);
    }

    @Override
    public void onResume() {
        super.onResume();
        refresh();
    }
}
