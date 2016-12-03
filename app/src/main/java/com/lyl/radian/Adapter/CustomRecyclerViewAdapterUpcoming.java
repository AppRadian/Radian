package com.lyl.radian.Adapter;

import android.app.Activity;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.ArrayList;

import com.lyl.radian.DBObjects.Bid;
import com.lyl.radian.R;
import com.lyl.radian.Utilities.Account;

/**
 * Created by Yannick on 10.11.2016.
 */

public class CustomRecyclerViewAdapterUpcoming extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    Account account;
    Activity activity;
    ArrayList<Bid> data;
    ProfileHeaderViewHolder headerHolder;
    ProfileInfoViewHolder infoHolder;
    private final int TYPE_HEADER = 0;
    private final int TYPE_ITEM = 1;

    public CustomRecyclerViewAdapterUpcoming(Activity activity, ArrayList<Bid> data) {

        account = (Account) activity.getApplication();
        this.activity = activity;
        this.data = data;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        RecyclerView.ViewHolder holder = null;

        if (viewType == TYPE_ITEM) {
            //inflate your layout and pass it to view holder
            View itemView = LayoutInflater.from(parent.getContext()).
                    inflate(R.layout.profile_list_item, parent, false);
            infoHolder = new ProfileInfoViewHolder(itemView);
            return infoHolder;
        } else{
            //inflate your layout and pass it to view holder
            View itemView = LayoutInflater.from(parent.getContext()).
                    inflate(R.layout.profile_list_header, parent, false);
            headerHolder = new ProfileHeaderViewHolder(itemView);
            return headerHolder;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int pos) {
        if (viewHolder instanceof ProfileInfoViewHolder) {
            ProfileInfoViewHolder holder = (ProfileInfoViewHolder) viewHolder;
            int position = pos - 1;

            holder.tag.setText(data.get(position).getTag());
            holder.location.setText(data.get(position).getLocation());
            holder.distance.setText("<=" + /*data.get(position)*/ "XX" + "km");
            holder.time.setText(data.get(position).getDate() + " - " + data.get(position).getTime()+ " Uhr");
            holder.ratingBar.setRating((float)data.get(position).getAverageRating());
            holder.count.setText(data.get(position).getCount() + " Bewertungen");
            holder.maxPart.setText(data.get(position).getParticipants() + "/" + data.get(position).getMaxParticipants());

            Bitmap pic = account.getBitmapFromCache(data.get(position)[1]);
            if(pic != null) {
                holder.profilePic.setImageBitmap(pic);
            }
            else {

            }
        }
        else if (viewHolder instanceof ProfileHeaderViewHolder) {
            ProfileHeaderViewHolder holder = (ProfileHeaderViewHolder) viewHolder;


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

        return data.get(position);
    }

    public class ProfileInfoViewHolder extends RecyclerView.ViewHolder{

        TextView tag;
        TextView location;
        TextView distance;
        TextView time;
        ImageView profilePic;
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
