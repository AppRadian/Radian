package com.lyl.radian.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.ArrayList;

import com.lyl.radian.DBObjects.Feedback;
import com.lyl.radian.R;

/**
 * Created by Yannick on 18.10.2016.
 */

public class CustomAdapter extends BaseAdapter {
    Context context;
    ArrayList<Feedback> data;
    private static LayoutInflater inflater = null;

    public CustomAdapter(Context context, ArrayList<Feedback> data) {
        // TODO Auto-generated constructor stub
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
    public Feedback getItem(int position) {
        // TODO Auto-generated method stub (ignore this text in brackets, habe ich nur geschrieben um was comitten zu können)
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
            vi = inflater.inflate(R.layout.feedback_list_item, null);

        TextView profileName = (TextView) vi.findViewById(R.id.profileNameListItem);
        TextView text = (TextView) vi.findViewById(R.id.text);

        profileName.setText(data.get(position).getFromUserMail());
        text.setText(data.get(position).getText());

        RatingBar ratingBar = (RatingBar) vi.findViewById(R.id.ratingBar2);
        ratingBar.setRating((float)data.get(position).getRating());
        
        return vi;
    }
}
