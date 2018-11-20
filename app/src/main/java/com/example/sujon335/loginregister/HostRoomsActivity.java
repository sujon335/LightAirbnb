package com.example.sujon335.loginregister;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class HostRoomsActivity extends AppCompatActivity {

    SQLiteDatabase AirBnbDb=null;
    List<Room> hostrooms=new ArrayList<Room>();
    UserSessionManager session;
    int userid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_host_rooms);

        session=new UserSessionManager(this);
        if(!session.IsLoggedIn())
        {
            logout();
        }
        userid=session.getUserId();

        GetHostRoomsfromDatabase();

        ListView hostroom_list=(ListView) findViewById(R.id.hostroom_list);
        RoomListAdapter adapter=new RoomListAdapter(this,hostrooms);
        hostroom_list.setAdapter(adapter);

        hostroom_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Room room=hostrooms.get(position);
                Intent intent = new Intent(HostRoomsActivity.this, RoomDetailsActivity.class);
                intent.putExtra("roomObject", room);
                startActivity(intent);
                //Toast.makeText(homeActivity.this,"You clicked placeid: "+id,Toast.LENGTH_LONG).show();
            }
        });
    }



    public void GetHostRoomsfromDatabase()
    {
        try{
            AirBnbDb=this.openOrCreateDatabase("AirbnbDB",MODE_PRIVATE,null);
            Cursor c=AirBnbDb.rawQuery("SELECT * FROM place WHERE hostid="+userid,null);

            if(c!=null && c.getCount()>0)
            {
                while (c.moveToNext()){
                    int placeid=c.getInt(c.getColumnIndex("placeid"));
                    String title=c.getString(c.getColumnIndex("title"));
                    String room_type=c.getString(c.getColumnIndex("roomtype"));
                    String property_type=c.getString(c.getColumnIndex("propertytype"));
                    String location=c.getString(c.getColumnIndex("location"));
                    double longitude=c.getDouble(c.getColumnIndex("longitude"));
                    double latitude=c.getDouble(c.getColumnIndex("latitude"));
                    int totalguests=c.getInt(c.getColumnIndex("totalguests"));
                    int beds=c.getInt(c.getColumnIndex("beds"));
                    int baths=c.getInt(c.getColumnIndex("baths"));
                    int minstay=c.getInt(c.getColumnIndex("minstay"));
                    String contact=c.getString(c.getColumnIndex("contact"));
                    int price=c.getInt(c.getColumnIndex("price"));
                    int isavailable=c.getInt(c.getColumnIndex("isavailable"));
                    int hostid=c.getInt(c.getColumnIndex("hostid"));

                    Room r=new Room(placeid,title,room_type,property_type,
                            location,longitude,latitude,totalguests,beds,baths,
                            minstay,contact,price,isavailable,hostid);
                    hostrooms.add(r);
                }
            }
            c.close();


        }
        catch (Error e){

            Log.e("AirBNB", "exception", e);
        }
    }


    public void logout()
    {
        session.SetLoggedIn(false);
        startActivity(new Intent(HostRoomsActivity.this,MainActivity.class));
        finish();
    }
}
