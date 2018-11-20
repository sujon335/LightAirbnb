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

public class HostOptionsListAdapter extends BaseAdapter {

    private Context hcontext;
    List<String> hostOptions;

    public HostOptionsListAdapter(Context hcontext, List<String> hostOptions) {
        this.hcontext = hcontext;
        this.hostOptions = hostOptions;
    }

    @Override

    public int getCount() {
        return hostOptions.size();
    }

    @Override
    public Object getItem(int position) {
        return hostOptions.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v=View.inflate(hcontext,R.layout.host_home_list_text_view,null);
        TextView option=(TextView) v.findViewById(R.id.host_home_list_text);
        option.setText(hostOptions.get(position));
        return v;
    }
}
