package com.lyl.radian.Adapter;

import android.support.v4.app.Fragment;
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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.lyl.radian.DBObjects.Bid;
import com.lyl.radian.R;
import com.lyl.radian.Utilities.Account;
import com.lyl.radian.Utilities.Constants;

/**
 * Created by Yannick on 10.11.2016.
 */

public class CustomRecyclerViewAdapterOwnProfile extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    Account account;
    Fragment context;
    ArrayList<Bid> data;
    CustomRecyclerViewAdapterOwnProfile.ProfileHeaderViewHolder headerHolder;
    CustomRecyclerViewAdapterOwnProfile.ProfileInfoViewHolder infoHolder;
    private final int TYPE_HEADER = 0;
    private final int TYPE_ITEM = 1;

    public CustomRecyclerViewAdapterOwnProfile(Fragment context, ArrayList<Bid> data) {

        account = (Account) context.getActivity().getApplication();
        this.context = context;
        this.data = data;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        RecyclerView.ViewHolder holder = null;

        if (viewType == TYPE_ITEM) {
            //inflate your layout and pass it to view holder
            View itemView = LayoutInflater.from(parent.getContext()).
                    inflate(R.layout.own_profile_list_item, parent, false);
            infoHolder = new CustomRecyclerViewAdapterOwnProfile.ProfileInfoViewHolder(itemView);
            return infoHolder;
        } else{
            //inflate your layout and pass it to view holder
            View itemView = LayoutInflater.from(parent.getContext()).
                    inflate(R.layout.profile_list_header, parent, false);
            headerHolder = new CustomRecyclerViewAdapterOwnProfile.ProfileHeaderViewHolder(itemView);
            return headerHolder;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int pos) {
        if (viewHolder instanceof ProfileInfoViewHolder) {
            final ProfileInfoViewHolder holder = (ProfileInfoViewHolder) viewHolder;
            int position = pos - 1;

            holder.tag.setText(data.get(position).getTag());
            holder.location.setText(data.get(position).getLocation());
            holder.time.setText(data.get(position).getDate() + " - " + data.get(position).getTime() + " Uhr");
            holder.ratingBar.setRating((float)data.get(position).getAverageRating());
            holder.count.setText(data.get(position).getCount() + " Bewertungen");
            holder.maxPart.setText(data.get(position).getParticipants() + "/" + data.get(position).getMaxParticipants());

            FirebaseStorage storage = FirebaseStorage.getInstance();
            StorageReference storageRef = storage.getReferenceFromUrl("gs://radian-eb422.appspot.com/" + data.get(position).profilePic);
            Glide.with(context)
                    .using(new FirebaseImageLoader())
                    .load(storageRef)
                    .placeholder(R.drawable.blank_profile_pic)
                    .dontAnimate()
                    .into(holder.profilePic);
        }
        else if (viewHolder instanceof ProfileHeaderViewHolder) {
            ProfileHeaderViewHolder holder = (ProfileHeaderViewHolder) viewHolder;
            holder.location.setText(account.getSelf().getLocation());
            holder.language.setText(account.getSelf().getLanguage());
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0)
            return TYPE_HEADER;
        else
            return TYPE_ITEM;
    }

    @Override
    public int getItemCount() {
        return data.size() + 1;
    }

    public Bid getItem(int position){
        
        return data.get(position - 1);
    }

    public class ProfileInfoViewHolder extends RecyclerView.ViewHolder{

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

    public class ProfileHeaderViewHolder extends RecyclerView.ViewHolder{

        TextView location;
        TextView language;

        public ProfileHeaderViewHolder(View vi){

            super(vi);
            location = (TextView) vi.findViewById(R.id.ownProfileLocation);
            language = (TextView) vi.findViewById(R.id.ownProfileLanguage);
        }
    }
}
