package app.radiant.c.lly.Adapter;

import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.ArrayList;

import app.radiant.c.lly.R;
import app.radiant.c.lly.Utilities.Account;

/**
 * Created by Yannick on 10.11.2016.
 */

public class CustomRecyclerViewAdapterBiete extends RecyclerView.Adapter<CustomRecyclerViewAdapterBiete.ProfileInfoViewHolder> {

    Account account;
    Fragment context;
    ArrayList<String[]> data;
    ProfileInfoViewHolder infoHolder;

    public CustomRecyclerViewAdapterBiete(Fragment context, ArrayList<String[]> data) {

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

        holder.tag.setText(data.get(position)[2]);
        holder.location.setText(data.get(position)[4]);
        holder.time.setText(data.get(position)[7] + " - " + data.get(position)[8] + " Uhr");
        holder.ratingBar.setRating(Float.parseFloat(data.get(position)[5]));
        holder.count.setText(data.get(position)[6] + " Bewertungen");
        holder.maxPart.setText(data.get(position)[9] + "/" + data.get(position)[10]);
        holder.profilePic.setImageBitmap(account.getSelf().getProfilePic());
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public String[] getItem(int position) {

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
