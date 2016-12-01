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

import com.lyl.radian.NetworkUtilities.GetBitmap;
import com.lyl.radian.R;
import com.lyl.radian.Utilities.Account;

/**
 * Created by Yannick on 10.11.2016.
 */

public class CustomRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    Account account;
    Activity activity;
    ArrayList<String[]> data;
    ViewGroup parent;
    ProfileHeaderViewHolder headerHolder;
    ProfileInfoViewHolder infoHolder;
    private final int TYPE_HEADER = 0;
    private final int TYPE_ITEM = 1;

    public CustomRecyclerViewAdapter(Activity activity, ArrayList<String[]> data) {

        this.account = (Account)activity.getApplication();
        this.activity = activity;
        this.data = data;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        this.parent = parent;
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

            holder.tag.setText(data.get(position)[2]);
            holder.location.setText(data.get(position)[4]);
            holder.distance.setText("<=" + data.get(position)[7] + "km");
            holder.time.setText(data.get(position)[8] + " - " + data.get(position)[9] + " Uhr");
            holder.ratingBar.setRating(Float.parseFloat(data.get(position)[5]));
            holder.count.setText(data.get(position)[6] + " Bewertungen");
            holder.maxPart.setText(data.get(position)[10] + "/" + data.get(position)[11]);

            Bitmap pic = account.getBitmapFromCache(account.getSearchedItem().getEmail());
            if(pic != null)
                holder.profilePic.setImageBitmap(pic);
            else
                new GetBitmap(activity, holder.profilePic, data.get(position)[1]).execute();

        } else if (viewHolder instanceof ProfileHeaderViewHolder) {
            ProfileHeaderViewHolder holder = (ProfileHeaderViewHolder) viewHolder;

            holder.location.setText(account.getSearchedUser().getLocation());
            holder.language.setText(account.getSearchedUser().getLanguage());
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

    public String[] getItem(int position){ return data.get(position - 1); }

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
