package com.lyl.radian.Adapter;

import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.ArrayList;

import com.bumptech.glide.Glide;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.lyl.radian.R;
import com.lyl.radian.Utilities.Account;
import com.lyl.radian.DBObjects.Bid;

/**
 * Created by Yannick on 10.11.2016.
 */

public class CustomRecyclerViewAdapterBiete extends RecyclerView.Adapter<CustomRecyclerViewAdapterBiete.ProfileInfoViewHolder> {

    Account account;
    Fragment context;
    ArrayList<Bid> data;
    ProfileInfoViewHolder infoHolder;

    public CustomRecyclerViewAdapterBiete(Fragment context, ArrayList<Bid> data) {
        account = (Account) context.getActivity().getApplication();
        this.context = context;
        this.data = data;
    }

    @Override
    public ProfileInfoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        //inflate your layout and pass it to view holder
        View itemView = LayoutInflater.from(parent.getContext()).
                inflate(R.layout.own_profile_list_item, parent, false);
        infoHolder = new ProfileInfoViewHolder(itemView);
        return infoHolder;
    }

    @Override
    public void onBindViewHolder(ProfileInfoViewHolder holder, int position) {

        holder.tag.setText(data.get(position).getTag());
        holder.location.setText(data.get(position).getLocation());
        holder.time.setText(data.get(position).getDate() + " - " + data.get(position).getTime() + " Uhr");
        holder.ratingBar.setRating((float)data.get(position).getAverageRating());
        holder.count.setText(data.get(position).getCount() + " Bewertungen");
        holder.maxPart.setText(data.get(position).getParticipants() + "/" + data.get(position).getMaxParticipants());

        String profilePic = data.get(position).getProfilePic();
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReferenceFromUrl("gs://radian-eb422.appspot.com/" + profilePic);
        Glide.with(context)
                .using(new FirebaseImageLoader())
                .load(storageRef)
                .placeholder(R.drawable.blank_profile_pic)
                .into(holder.profilePic);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public Bid getItem(int position) {

        return data.get(position);
    }

    public class ProfileInfoViewHolder extends RecyclerView.ViewHolder {

        TextView tag;
        TextView location;
        TextView time;
        ImageView profilePic;
        RatingBar ratingBar;
        TextView count;
        TextView maxPart;

        public ProfileInfoViewHolder(View vi) {

            super(vi);
            tag = (TextView) vi.findViewById(R.id.tag);
            location = (TextView) vi.findViewById(R.id.location);
            time = (TextView) vi.findViewById(R.id.time);
            profilePic = (ImageView) vi.findViewById(R.id.profilePic);
            ratingBar = (RatingBar) vi.findViewById(R.id.ratingBar4);
            count = (TextView) vi.findViewById(R.id.count);
            maxPart = (TextView) vi.findViewById(R.id.maxPart);
        }
    }
}
