package com.example.sujon335.loginregister;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sujon335 on 2/6/18.
 */

public class BookingRequestListAdapter extends BaseAdapter {

    private Context rcontext;
    List<BookingRequest> requests;
    SQLiteDatabase AirBnbDb=null;
    public BookingRequestListAdapter(Context rcontext, List<BookingRequest> requests) {
        this.rcontext = rcontext;
        this.requests = requests;
    }

    @Override

    public int getCount() {
        return requests.size();
    }

    @Override
    public Object getItem(int position) {
        return requests.get(position);
    }

    @Override
    public long getItemId(int position) {
        return requests.get(position).getRequestID();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View v=View.inflate(rcontext,R.layout.booking_req_list,null);

        TextView reqrroomtitle=(TextView) v.findViewById(R.id.req_room_title);
        TextView reqroomlocation=(TextView) v.findViewById(R.id.req_room_location);
        TextView requestedby=(TextView) v.findViewById(R.id.req_guest);
        TextView guestemail=(TextView) v.findViewById(R.id.req_guest_email);
        TextView reqtime=(TextView) v.findViewById(R.id.req_time);
        reqrroomtitle.setText(requests.get(position).getReqRoomTitle());
        reqroomlocation.setText(requests.get(position).getReqRoomLocation());
        requestedby.setText("Requested By: "+requests.get(position).getRequestedby());
        guestemail.setText("Email: "+requests.get(position).getGuestemail());
        reqtime.setText("Requested at: "+requests.get(position).getRequestingTime());

        CardView btnaccept=(CardView) v.findViewById(R.id.cardViewaccept);
        CardView btnreject=(CardView) v.findViewById(R.id.cardViewdiscard);

        final int reqid=requests.get(position).getRequestID();

        btnaccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(rcontext instanceof BookingRequestsActivity){
                    ((BookingRequestsActivity) rcontext).confirmBooking(reqid);
                }
                requests.clear();
                List<BookingRequest> reqs=new ArrayList<BookingRequest>();
                if(rcontext instanceof BookingRequestsActivity){
                    reqs=((BookingRequestsActivity) rcontext).GetRequestsFromDatabase();
                }
                requests.addAll(reqs);
                notifyDataSetChanged();

                Toast.makeText((BookingRequestsActivity)rcontext,"Booking Confirmed",Toast.LENGTH_LONG).show();
            }
        });

        btnreject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(rcontext instanceof BookingRequestsActivity){
                    ((BookingRequestsActivity) rcontext).revomeBookingRequest(reqid);
                }
                requests.clear();
                List<BookingRequest> reqs=new ArrayList<BookingRequest>();
                if(rcontext instanceof BookingRequestsActivity){
                    reqs=((BookingRequestsActivity) rcontext).GetRequestsFromDatabase();
                }
                requests.addAll(reqs);
                notifyDataSetChanged();
            }
        });

        return v;
    }




}
