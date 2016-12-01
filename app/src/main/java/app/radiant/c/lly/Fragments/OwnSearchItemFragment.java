package app.radiant.c.lly.Fragments;

import android.app.Activity;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import app.radiant.c.lly.DialogFragments.EditDialog;
import app.radiant.c.lly.Activities.ShowBidFeedbackActivity;
import app.radiant.c.lly.R;
import app.radiant.c.lly.Utilities.Account;

/**
 * Created by Ludwig on 20.11.2016.
 */

public class OwnSearchItemFragment extends Fragment {

        View view;
        Activity callingActivity;
        Account account;
        Button edit;
        ImageView userProfile;
        TextView userEmail;
        TextView userBid;
        TextView timenDate;
        TextView userDescription;
        TextView ratings;
        RatingBar ratingBar;

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            view = inflater.inflate(R.layout.fragment_own_search_item, container, false);
            callingActivity = getActivity();

            AppBarLayout mToolbar = (AppBarLayout) getActivity().findViewById(R.id.app_bar_layout);
            mToolbar.setTranslationY(0);

            ((FloatingActionButton) getActivity().findViewById(R.id.fab)).setVisibility(View.GONE);

            TabLayout tabLayout = (TabLayout) getActivity().findViewById(R.id.tab_layout);
            tabLayout.setVisibility(TabLayout.GONE);

            RelativeLayout content = (RelativeLayout) getActivity().findViewById(R.id.content_main_app);
            CoordinatorLayout.LayoutParams p = (CoordinatorLayout.LayoutParams) content.getLayoutParams();
            p.setBehavior(new AppBarLayout.ScrollingViewBehavior());
            content.setLayoutParams(p);

            account = (Account) callingActivity.getApplication();
            userProfile = (ImageView) view.findViewById(R.id.userProfile);
            userEmail = (TextView) view.findViewById(R.id.userEmail);
            userBid = (TextView) view.findViewById(R.id.userBid);
            userDescription = (TextView) view.findViewById(R.id.userDescription);
            timenDate = (TextView) view.findViewById(R.id.timenDate);
            ratingBar = (RatingBar) view.findViewById(R.id.avergageRating);
            ratings = (TextView) view.findViewById(R.id.rezensionen);
            edit = (Button) view.findViewById(R.id.editButton);

            userProfile.setImageBitmap(account.getSelf().getProfilePic());
            userEmail.setText(account.getSelf().getEmail());
            userBid.setText(account.getSearchedItem().getTag());
            timenDate.setText(account.getSearchedItem().getDate() + " - " + account.getSearchedItem().getTime() + " Uhr");
            userDescription.setText(account.getSearchedItem().getDescription());
            ratingBar.setRating(account.getSearchedItem().getAverageRating());
            ratings.setText(account.getSearchedItem().getCount() + " Rezensionen");

            ((CollapsingToolbarLayout)getActivity().findViewById(R.id.collapsing_toolbar)).setTitleEnabled(false);
            ((TextView)getActivity().findViewById(R.id.toolbar_title)).setText("Angebot von " + account.getSearchedItem().getEmail());
            ((ImageView)getActivity().findViewById(R.id.ownProfilePic)).setImageBitmap(null);


            edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    EditDialog add = new EditDialog();
                    add.show(getChildFragmentManager(), "Edit Dialog");
                }
            });

            ratingBar.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (event.getAction() == MotionEvent.ACTION_DOWN) {
                        Intent intent = new Intent(getActivity(), ShowBidFeedbackActivity.class);
                        startActivity(intent);
                    }
                    return true;
                }
            });

            ratings.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), ShowBidFeedbackActivity.class);
                    startActivity(intent);
                }
            });

            return view;
        }


        public boolean listContainsId(String id){

            for (String[] s : account.getSelf().getParticipations()){
                if(s[0].equals(id))
                    return true;
            }
            return false;
        }
    }

