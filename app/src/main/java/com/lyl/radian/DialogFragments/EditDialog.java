package com.lyl.radian.DialogFragments;

import android.app.Activity;
import android.app.DatePickerDialog;

import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import java.io.IOException;
import java.nio.DoubleBuffer;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.lyl.radian.Adapter.SpinnerAdapter;
import com.lyl.radian.Adapter.*;
import com.lyl.radian.Fragments.OwnSearchItemFragment;
import com.lyl.radian.R;
import com.lyl.radian.Utilities.Account;
import com.lyl.radian.DBObjects.Bid;
import com.lyl.radian.Utilities.Constants;

/**
 * Created by Ludwig on 20.11.2016.
 */

public class EditDialog extends DialogFragment {
    AutoCompleteTextView location;
    Account account;
    OwnSearchItemFragment callingFragment;
    Button dateButton;
    Button timeButton;
    Button done;
    Calendar myCalendar;
    EditText participants;
    String city;
    String bid;
    Spinner bidTypes;
    TextView description;
    TextView date;
    TextView time;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.dialog_add_bid, container, false);

        callingFragment = (OwnSearchItemFragment) getParentFragment();
        account = (Account) callingFragment.getActivity().getApplication();

        bidTypes = (Spinner) rootView.findViewById(R.id.bidSpinner);

        SpinnerAdapter adapter = new SpinnerAdapter(getActivity(), android.R.layout.simple_list_item_1);


        adapter.addAll(getResources().getStringArray(R.array.bid_arrays));
        adapter.add("Was wollen Sie anbieten?");
        bidTypes.setAdapter(adapter);
        bidTypes.setSelection(adapter.getCount());

        bidTypes.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                // TODO Auto-generated method stub

                if(bidTypes.getSelectedItem() == "Was wollen Sie anbieten?")
                {

                }
                else{
                    bid = bidTypes.getSelectedItem().toString();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub

            }
        });


        location = (AutoCompleteTextView) rootView.findViewById(R.id.location);
        time = (TextView) rootView.findViewById(R.id.time);
        timeButton = (Button) rootView.findViewById(R.id.timeButton);
        date = (TextView) rootView.findViewById(R.id.date);
        dateButton = (Button) rootView.findViewById(R.id.dateButton);
        participants = (EditText) rootView.findViewById(R.id.participants);
        description = (TextView) rootView.findViewById(R.id.description);



        final AutoCompleteTextView autocompleteView = (AutoCompleteTextView) rootView.findViewById(R.id.location);
        autocompleteView.setAdapter(new PlacesAutoCompleteAdapter(getActivity(), R.layout.autocomplete_list_item)); // vorher getActivity() anstelle von this

        autocompleteView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(parent.getItemAtPosition(position) != null)
                    city = parent.getItemAtPosition(position).toString();
            }
        });

        myCalendar = Calendar.getInstance();
        final DatePickerDialog.OnDateSetListener dateSetter = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }

        };

        dateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(getActivity(), dateSetter, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });


        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(getActivity(), dateSetter, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        timeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myCalendar = Calendar.getInstance();
                int hour = myCalendar.get(Calendar.HOUR_OF_DAY);
                int minute = myCalendar.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        String h = hourOfDay < 10 ? "0" + hourOfDay : String.valueOf(hourOfDay);
                        String m = minute < 10 ? "0" + minute : String.valueOf(minute);
                        time.setText( h + ":" + m);
                    }
                }, hour, minute, true);
                mTimePicker.show();

            }
        });

        time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myCalendar = Calendar.getInstance();
                int hour = myCalendar.get(Calendar.HOUR_OF_DAY);
                int minute = myCalendar.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        String h = hourOfDay < 10 ? "0" + hourOfDay : String.valueOf(hourOfDay);
                        String m = minute < 10 ? "0" + minute : String.valueOf(minute);
                        time.setText( h + ":" + m);
                    }
                }, hour, minute, true);
                mTimePicker.show();

            }
        });

        description = (TextView) rootView.findViewById(R.id.description);

        done = (Button) rootView.findViewById(R.id.doneBtn);
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (city != null && description.getText().toString().length() != 0 && location.getText().toString().length() != 0 &&
                        city.equals(location.getText().toString()) && time.getText().toString().length() != 0 && date.getText().toString().length() != 0 && participants.getText().toString().length() != 0) {
                    Double[] latLong = getLocationFromAddress(autocompleteView.getText().toString());

                    DatabaseReference bids = FirebaseDatabase.getInstance().getReference("Bids").child(account.getClickedBid().getId());
                    bids.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            Bid data = dataSnapshot.getValue(Bid.class);
                            double averageRating = data.getAverageRating();
                            long count = data.getCount();
                            long participants = data.getParticipants();
                            DatabaseReference ownBids = FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("ownBids").push();
                            ownBids.setValue(account.getClickedBid().getId());

                            Location bidLocation = Constants.getLocationFromAddress(getActivity(), location.getText().toString());
                            Bid bidToInsert = new Bid(ownBids.getKey(), FirebaseAuth.getInstance().getCurrentUser().getUid(), FirebaseAuth.getInstance().getCurrentUser().getDisplayName(), bid, description.getText().toString(),
                                    location.getText().toString(), bidLocation.getLatitude(), bidLocation.getLongitude(), averageRating, count, date.getText().toString(), time.getText().toString(), participants, Long.parseLong(EditDialog.this.participants.getText().toString()));
                            DatabaseReference bids = FirebaseDatabase.getInstance().getReference("Bids");
                            bids.child(account.getClickedBid().getId()).setValue(bidToInsert);
                            account.setClickedBid(bidToInsert);
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

                    getDialog().dismiss();
                }
                else
                    location.setError("Location doesn't exist");
            }
        });

        return rootView;
    }

    private void updateLabel(){

        String myFormat = "dd/MM/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.GERMAN);

        date.setText(sdf.format(myCalendar.getTime()));
    }

    public Double[] getLocationFromAddress(String strAddress){

        Double[] latLong = new Double[2];
        Geocoder coder = new Geocoder(getActivity());
        List<Address> address;

        try {
            address = coder.getFromLocationName(strAddress,1);
            if (address==null) {
                return null;
            }
            Address location=address.get(0);
            latLong[0] = location.getLatitude();
            latLong[1] = location.getLongitude();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return latLong;
    }

    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);

        Activity activity = getActivity();
        if(callingFragment instanceof MyDialogCloseListener)
            ((MyDialogCloseListener)callingFragment).handleDialogClose(dialog);
        else if(activity instanceof MyDialogCloseListener)
            ((MyDialogCloseListener)activity).handleDialogClose(dialog);
    }
}
