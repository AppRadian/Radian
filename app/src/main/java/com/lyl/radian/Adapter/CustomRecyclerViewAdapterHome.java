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

public class CustomRecyclerViewAdapterHome extends RecyclerView.Adapter<CustomRecyclerViewAdapterHome.ProfileInfoViewHolder> {

    Account account;
    Activity activity;
    ArrayList<String[]> data;
    ViewGroup parent;
    ProfileInfoViewHolder holder;

    public CustomRecyclerViewAdapterHome(Activity activity, ArrayList<String[]> data) {

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
    public void onBindViewHolder(ProfileInfoViewHolder holder, int position) {

        holder.tag.setText(data.get(position)[2]);
        holder.location.setText(data.get(position)[4]);
        holder.distance.setText("<=" + data.get(position)[7] + "km");
        holder.time.setText(data.get(position)[8] + " - " + data.get(position)[9] + " Uhr");
        holder.ratingBar.setRating(Float.parseFloat(data.get(position)[5]));
        holder.count.setText(data.get(position)[6] + " Bewertungen");
        holder.maxPart.setText(data.get(position)[10] + "/" + data.get(position)[11]);

        Bitmap pic = account.getBitmapFromCache(data.get(position)[1]);
        if(pic != null)
            holder.profilePic.setImageBitmap(pic);
        else
            new GetBitmap(activity, holder.profilePic, data.get(position)[1]).execute();
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public String[] getItem(int position){ return data.get(position); }

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
