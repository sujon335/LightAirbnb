package com.example.sujon335.loginregister;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by sujon335 on 2/6/18.
 */

public class RoomListAdapter extends BaseAdapter {

    private Context gcontext;
    List<Room> rooms;

    public RoomListAdapter(Context hcontext, List<Room> rooms) {
        this.gcontext = hcontext;
        this.rooms = rooms;
    }

    @Override

    public int getCount() {
        return rooms.size();
    }

    @Override
    public Object getItem(int position) {
        return rooms.get(position);
    }

    @Override
    public long getItemId(int position) {
        return rooms.get(position).placeid;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v=View.inflate(gcontext,R.layout.room_list_view,null);

        TextView title=(TextView) v.findViewById(R.id.room_title);
        TextView location=(TextView) v.findViewById(R.id.room_location);
        TextView price=(TextView) v.findViewById(R.id.room_price);
        TextView roomtype=(TextView) v.findViewById(R.id.room_type2);
        title.setText(rooms.get(position).title);
        location.setText(rooms.get(position).location);
        price.setText(rooms.get(position).price+"$ per day");
        roomtype.setText(rooms.get(position).room_type+" in a "+rooms.get(position).property_type);
        return v;
    }
}
