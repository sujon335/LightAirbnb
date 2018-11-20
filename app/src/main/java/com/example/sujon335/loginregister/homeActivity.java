package com.example.sujon335.loginregister;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SlidingDrawer;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;

import java.util.ArrayList;
import java.util.List;

public class homeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    UserSessionManager session;
    List<Room> rooms=new ArrayList<Room>();
    SQLiteDatabase AirBnbDb=null;
    int PLACE_REQUEST=2;
    TextView searchbutton;
    double longitude_search,latitude_search;
    RoomListAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_drawer);

        session=new UserSessionManager(this);
        if(!session.IsLoggedIn())
        {
            logout();
        }


        rooms=GetRoomsfromDatabase();

        TextView no_data=(TextView) findViewById(R.id.no_rooms);
        if(rooms.size()==0)
            no_data.setVisibility(View.VISIBLE);

        ListView room_list=(ListView) findViewById(R.id.room_list);
        adapter=new RoomListAdapter(this,rooms);
        room_list.setAdapter(adapter);

        room_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Room room=rooms.get(position);
                Intent intent = new Intent(homeActivity.this, RoomDetailsActivity.class);
                intent.putExtra("roomObject", room);
                startActivity(intent);
                //Toast.makeText(homeActivity.this,"You clicked placeid: "+id,Toast.LENGTH_LONG).show();
            }
        });

        NavigationHanldling();


        searchbutton=(TextView) findViewById(R.id.search_room);
        searchbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PlacePicker.IntentBuilder builder=new PlacePicker.IntentBuilder();
                Intent intent;
                Context context = getApplicationContext();
                try {
                    intent= builder.build(homeActivity.this);
                    startActivityForResult(intent,PLACE_REQUEST);
                } catch (GooglePlayServicesRepairableException e) {
                    e.printStackTrace();
                } catch (GooglePlayServicesNotAvailableException e) {
                    e.printStackTrace();
                }
            }
        });


    }


    protected void onActivityResult(int requestcode,int resultcode,Intent data)
    {
        if(requestcode==PLACE_REQUEST)
        {
            if(resultcode==RESULT_OK)
            {
                Place place=PlacePicker.getPlace(data,this);
                latitude_search = place.getLatLng().latitude;
                longitude_search = place.getLatLng().longitude;

                GetSearchedPlaces();
                String address=place.getAddress().toString();
                searchbutton.setText(address);
                //Toast.makeText(this,"Long,Lat: " +longitude+" ,"+latitude+" Address: "+address,Toast.LENGTH_LONG).show();
            }
        }
    }


    public  void GetSearchedPlaces()
    {
        EditText radiustxt=(EditText) findViewById(R.id.radius);
        String radiusval=radiustxt.getText().toString();
        double radius=10.0;
        if(!radiusval.isEmpty()) {
            radius = Double.parseDouble(radiusval);
        }
        else {
            radius=50000;
        }
        rooms=GetRoomsfromDatabase();
        List<Room> searched_rooms=new ArrayList<Room>();
        double long_rad_search=(Math.PI * longitude_search) / 180;
        double lat_rad_search=(Math.PI * latitude_search) / 180;
        for(Room r : rooms)
        {

            double long_rad_db=(Math.PI * r.getLongitude()) / 180;
            double lat_rad_db=(Math.PI * r.getLatitude())/180;
            double dist=Math.acos(Math.sin(lat_rad_db) * Math.sin(lat_rad_search)
                    + Math.cos(lat_rad_db) * Math.cos(lat_rad_search) * Math.cos(long_rad_db - long_rad_search)) * 6371;
            if(dist<=radius)
            {
                searched_rooms.add(r);
            }
        }
        adapter.rooms.clear();
        adapter.rooms.addAll(searched_rooms);
        adapter.notifyDataSetChanged();
        if(searched_rooms.size()<1)
        {
            Toast.makeText(this,"No Rooms found with the search result",Toast.LENGTH_LONG).show();
        }
    }

    public List<Room> GetRoomsfromDatabase()
    {
        List<Room> dbrooms=new ArrayList<Room>();
        try{
            AirBnbDb=this.openOrCreateDatabase("AirbnbDB",MODE_PRIVATE,null);
            Cursor c=AirBnbDb.rawQuery("SELECT * FROM place",null);

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
                    dbrooms.add(r);
                }
            }
            c.close();


        }
        catch (Error e){

            Log.e("AirBNB", "exception", e);
        }
        return dbrooms;
    }

    public void NavigationHanldling()
    {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_home);
        setSupportActionBar(toolbar);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout_home);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close){
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
            }

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);

                TextView UserHeadername=(TextView) findViewById(R.id.UserHeaderName);
                TextView UserHeaderemail=(TextView) findViewById(R.id.UserHeaderEmail);

                UserHeadername.setText(session.getUserName());
                UserHeaderemail.setText(session.getUserEmail());
            }
        };

        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view_home);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout_home);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {



        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.become_host) {
            //Toast.makeText(this,"Wanna be a host",Toast.LENGTH_SHORT).show();
            startActivity(new Intent(homeActivity.this,hostRegister.class));
            //finish();

        } else if (id == R.id.see_meesage) {
            Toast.makeText(this,"Not applicable",Toast.LENGTH_SHORT).show();
        } else if (id == R.id.clear_search) {
            finish();
            startActivity(getIntent());
            //Toast.makeText(this,"Wanna set profile",Toast.LENGTH_SHORT).show();
        } else if (id == R.id.logout_button) {

            logout();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout_home);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    public void logout()
    {
        session.SetLoggedIn(false);
        startActivity(new Intent(homeActivity.this,MainActivity.class));
        finish();
    }
}
