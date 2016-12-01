package app.radiant.c.lly.Adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.ArrayList;

import app.radiant.c.lly.R;
import app.radiant.c.lly.Utilities.Account;

/**
 * Created by Yannick on 18.10.2016.
 */

public class CustomAdapterBiete extends BaseAdapter {

    Account account;
    Context context;
    ArrayList<String[]> data;
    private static LayoutInflater inflater = null;

    public CustomAdapterBiete(Activity context, ArrayList<String[]> data) {
        // TODO Auto-generated constructor stub
        account = (Account) context.getApplication();
        this.context = context;
        this.data = data;
        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return data.size();
    }

    @Override
    public String[] getItem(int position) {
        // TODO Auto-generated method stub
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        View vi = convertView;
        if (vi == null)
            vi = inflater.inflate(R.layout.own_profile_list_item, null);

        TextView tag = (TextView) vi.findViewById(R.id.tag);
        TextView location = (TextView) vi.findViewById(R.id.location);
        TextView time = (TextView) vi.findViewById(R.id.time);
        ImageView profilePic = (ImageView) vi.findViewById(R.id.profilePic);
        RatingBar ratingBar = (RatingBar) vi.findViewById(R.id.ratingBar4);
        TextView count = (TextView) vi.findViewById(R.id.count);
        TextView maxPart = (TextView) vi.findViewById(R.id.maxPart);

        tag.setText(data.get(position)[2]);
        location.setText(data.get(position)[4]);
        time.setText(data.get(position)[7] + " - " + data.get(position)[8] + " Uhr");
        ratingBar.setRating(Float.parseFloat(data.get(position)[5]));
        count.setText(data.get(position)[6] + " Bewertungen");
        maxPart.setText(data.get(position)[9] + "/" + data.get(position)[10]);
        profilePic.setImageBitmap(account.getSelf().getProfilePic());

        return vi;
    }
}