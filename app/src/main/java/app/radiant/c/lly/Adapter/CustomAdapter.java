package app.radiant.c.lly.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.ArrayList;

import app.radiant.c.lly.R;

/**
 * Created by Yannick on 18.10.2016.
 */

public class CustomAdapter extends BaseAdapter {
    Context context;
    ArrayList<String[]> data;
    private static LayoutInflater inflater = null;

    public CustomAdapter(Context context, ArrayList<String[]> data) {
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
    public Object getItem(int position) {
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

        profileName.setText(data.get(position)[0]);
        text.setText(data.get(position)[1]);

        RatingBar ratingBar = (RatingBar) vi.findViewById(R.id.ratingBar2);
        ratingBar.setRating(Float.parseFloat(data.get(position)[2]));
        return vi;
    }
}
