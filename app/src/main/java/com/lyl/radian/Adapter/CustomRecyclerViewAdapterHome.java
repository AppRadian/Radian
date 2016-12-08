package com.lyl.radian.Adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.ArrayList;

import com.bumptech.glide.Glide;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.lyl.radian.R;
import com.lyl.radian.Utilities.Account;
import com.lyl.radian.DBObjects.Bid;
import com.lyl.radian.Utilities.Constants;

/**
 * Created by Yannick on 10.11.2016.
 */

public class CustomRecyclerViewAdapterHome extends RecyclerView.Adapter<CustomRecyclerViewAdapterHome.ProfileInfoViewHolder> {

    Account account;
    Activity activity;
    ArrayList<Bid> data;
    ViewGroup parent;
    ProfileInfoViewHolder holder;

    public CustomRecyclerViewAdapterHome(Activity activity, ArrayList<Bid> data) {

        this.account = (Account)activity.getApplication();
        this.activity = activity;
        this.data = data;
    }

    @Override
    public ProfileInfoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        this.parent = parent;
        //inflate your layout and pass it to view holder
        View itemView = LayoutInflater.from(parent.getContext()).
                inflate(R.layout.home_list_item, parent, false);
        holder = new ProfileInfoViewHolder(itemView);
        return holder;
    }

    @Override
    public void onBindViewHolder(final ProfileInfoViewHolder holder, int position) {

        holder.profileName.setText(data.get(position).getEmail());
        holder.tag.setText(data.get(position).getTag());
        holder.location.setText(data.get(position).getLocation());
        holder.distance.setText("<=" + Math.round(data.get(position).distance/1000) + "km");
        holder.time.setText(data.get(position).getDate() + " - " + data.get(position).getTime() + " Uhr");
        holder.ratingBar.setRating((float)data.get(position).getAverageRating());
        holder.count.setText(data.get(position).getCount() + " Bewertungen");
        holder.maxPart.setText(data.get(position).getParticipants() + "/" + data.get(position).getMaxParticipants());

        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReferenceFromUrl("gs://radian-eb422.appspot.com/" + data.get(position).profilePic);
        Glide.with(activity)
                .using(new FirebaseImageLoader())
                .load(storageRef)
                .placeholder(R.drawable.blank_profile_pic)
                .dontAnimate()
                .into(holder.profilePic);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public Bid getItem(int position){ return data.get(position); }

    public class ProfileInfoViewHolder extends RecyclerView.ViewHolder{

        TextView tag;
        TextView location;
        TextView distance;
        TextView time;
        ImageView profilePic;
        TextView profileName;
        RatingBar ratingBar;
        TextView count;
        TextView maxPart;

        public ProfileInfoViewHolder(View vi) {

            super(vi);
            tag = (TextView) vi.findViewById(R.id.tag);
            location = (TextView) vi.findViewById(R.id.location);
            distance = (TextView) vi.findViewById(R.id.distance);
            time = (TextView) vi.findViewById(R.id.time);
            profilePic = (ImageView) vi.findViewById(R.id.profilePic);
            profileName = (TextView) vi.findViewById(R.id.profileNameSearch);
            ratingBar = (RatingBar) vi.findViewById(R.id.ratingBar4);
            count = (TextView) vi.findViewById(R.id.count);
            maxPart = (TextView) vi.findViewById(R.id.maxPart);
        }
    }
}
