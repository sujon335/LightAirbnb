package com.example.sujon335.loginregister;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.webkit.GeolocationPermissions;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


public class RoomDetailsActivity extends AppCompatActivity implements OnMapReadyCallback {

    MapView locationMapView;
    GoogleMap map;
    LatLng latlng;
    Room room;
    String des, hostname;
    int hostid;

    TextView totalGuest, beds, baths, price, minStay, title, location, roomtype, host, reqbookingtext, calltext,sendmsgview;
    double longitude, latitude;
    CardView bookingbutton;
    SQLiteDatabase AirBnbDb = null;
    UserSessionManager session;
    int userid, isavailable;
    final int CALL_PERMISSION = 1;
    String dial;
    String sms;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room_details);

        session = new UserSessionManager(this);
        if (!session.IsLoggedIn()) {
            logout();
        }
        userid = session.getUserId();

        Intent intent = getIntent();
        room = (Room) intent.getSerializableExtra("roomObject");

        setValues();

        locationMapView = (MapView) findViewById(R.id.locationmapview);
        if (locationMapView != null) {
            locationMapView.onCreate(null);
            locationMapView.onResume();
            locationMapView.getMapAsync(this);
        }







        bookingbutton = (CardView) findViewById(R.id.book);
        reqbookingtext = (TextView) findViewById(R.id.req_booking_text);
        if (isavailable == 0) {
            reqbookingtext.setText("Booked By: "+getBookingData());
        } else if (AlreadySentBookingRequest()&& isavailable==1) {
            reqbookingtext.setText("Booking Requested");
        }

        if(session.isUserHost())
        {
            //bookingbutton.setVisibility(bookingbutton.GONE);
            bookingbutton.setClickable(false);
        }

        bookingbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isavailable == 1 && !AlreadySentBookingRequest() && !session.isUserHost()) {
                    if (sendBookingRequest()) {
                        Toast.makeText(RoomDetailsActivity.this, "Booking Request Has been sent", Toast.LENGTH_LONG).show();
                        //startActivity(new Intent(RoomDetailsActivity.this, homeActivity.class));
                        //finish();
                        finish();
                        startActivity(getIntent());
                    }
                } else {
                    Toast.makeText(RoomDetailsActivity.this, "You can not send booking request", Toast.LENGTH_LONG).show();
                }
            }
        });


        calltext = (TextView) findViewById(R.id.callhost);
        dial = "tel:" + room.contact;
        calltext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(RoomDetailsActivity.this,"Phone number: "+dial,Toast.LENGTH_LONG).show();


                if (ActivityCompat.checkSelfPermission(RoomDetailsActivity.this, Manifest.permission.CALL_PHONE)
                        != PackageManager.PERMISSION_GRANTED) {

                    ActivityCompat.requestPermissions(RoomDetailsActivity.this, new String[]{Manifest.permission.CALL_PHONE},
                            CALL_PERMISSION);

                } else startActivity(new Intent(Intent.ACTION_CALL, Uri.parse(dial)));


            }
        });

        sendmsgview=(TextView) findViewById(R.id.texthost);
        sms="smsto:"+room.contact;
        sendmsgview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent smsintent = new Intent(Intent.ACTION_SENDTO, Uri.parse(sms));
                smsintent.putExtra("sms_body","From AirBnB: "+session.getUserName()+", ");
                startActivity(smsintent);
            }
        });

    }


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[],
                                           @NonNull int[] grantResults) {
        // Make sure it's our original READ_CONTACTS request
        if (requestCode == CALL_PERMISSION) {
            if (grantResults.length == 1 &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE)
                        != PackageManager.PERMISSION_GRANTED) {

                    Toast.makeText(this, "Call permission denied no call can be made", Toast.LENGTH_LONG).show();
                }
                startActivity(new Intent(Intent.ACTION_CALL, Uri.parse(dial)));

            } else {

                    Toast.makeText(this, "Call permission denied no call can be made", Toast.LENGTH_LONG).show();

            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    public boolean AlreadySentBookingRequest()
    {
        boolean value;
        try{
            AirBnbDb=this.openOrCreateDatabase("AirbnbDB",MODE_PRIVATE,null);

            String query="SELECT * FROM bookingRequest WHERE guestid ="+userid+" AND placeid="+room.placeid;
            Cursor c=AirBnbDb.rawQuery(query, null);
            if(c!=null && c.getCount()>0)
            {
                c.moveToFirst();
                return true;
            }
            c.close();
        }
        catch (Error e){
            Log.e("AirBNB", "exception", e);
        }
        return false;
    }


    public String getBookingData()
    {
        String bookedby="";
        try{
            AirBnbDb=this.openOrCreateDatabase("AirbnbDB",MODE_PRIVATE,null);

            String query="SELECT * FROM bookingRecord as br JOIN guest as g on br.guestid=g.id WHERE br.placeid="+room.placeid;
            Cursor c=AirBnbDb.rawQuery(query, null);
            if(c!=null && c.getCount()>0)
            {
                c.moveToFirst();
                bookedby=c.getString(c.getColumnIndex("name"));
            }
            c.close();
        }
        catch (Error e){
            Log.e("AirBNB", "exception", e);
        }

        return bookedby;
    }

    public boolean sendBookingRequest()
    {

        Date currentDate = new Date();
        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        String currenttime=dateFormatter.format(currentDate);


        try {
            AirBnbDb=this.openOrCreateDatabase("AirbnbDB",MODE_PRIVATE,null);
            AirBnbDb.execSQL("INSERT INTO bookingRequest (guestid,placeid,bookingreqTime) " +
                    "VALUES (" + userid + "," + room.placeid + ",'" + currenttime + "');");
            return true;
        }
        catch (Exception e)
        {
            Toast.makeText(this,"Exception occured while inserting",Toast.LENGTH_SHORT).show();
            Log.e("AirBNB", "exception", e);
            return false;
        }
    }

    public void setValues()
    {
        title=(TextView) findViewById(R.id.roomviewtitle);
        roomtype=(TextView) findViewById(R.id.roomviewtype);
        host=(TextView) findViewById(R.id.hosname);
        totalGuest=(TextView) findViewById(R.id.totalguestsnum);
        beds=(TextView)findViewById(R.id.bedsnum);
        baths=(TextView) findViewById(R.id.bathsnum);
        price=(TextView) findViewById(R.id.priceofroom);
        minStay=(TextView) findViewById(R.id.minimumstay);
        location=(TextView) findViewById(R.id.locationroomview);

        title.setText(room.title);
        roomtype.setText(room.room_type+": "+room.property_type);
        totalGuest.setText("Total Guests: "+room.totalguests);
        beds.setText("Bedrooms: "+room.beds);
        baths.setText("Bathrooms: "+room.baths);
        price.setText(room.price+" per day");
        minStay.setText("Minimum stay: "+room.minstay+" days");
        location.setText("Location: "+room.location);

        isavailable=room.isavailable;
        hostid=room.hostid;
        hostname=getHostName(hostid);
        host.setText("Hosted by: "+hostname);
        longitude=room.longitude;
        latitude=room.latitude;
        //Log.e("AirBNB", "Long : " +longitude+ " lat: "+latitude);
        latlng=new LatLng(latitude,longitude);
        des=room.location;
    }

    public String getHostName(int id)
    {
        String HostName="";
        try{
            AirBnbDb=this.openOrCreateDatabase("AirbnbDB",MODE_PRIVATE,null);

            String query="SELECT * FROM guest WHERE id ="+id;
            Cursor c=AirBnbDb.rawQuery(query, null);
            if(c!=null && c.getCount()>0)
            {
                c.moveToFirst();
                id=c.getInt(c.getColumnIndex("id"));
                HostName=c.getString(c.getColumnIndex("name"));

            }
            c.close();
        }
        catch (Error e){
            Log.e("AirBNB", "exception", e);
        }
        return HostName;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        MapsInitializer.initialize(getBaseContext());
        map=googleMap;
        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        map.addMarker(new MarkerOptions().position(latlng).title(des).icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_map_icon)));
        map.moveCamera(CameraUpdateFactory.newLatLng(latlng));
        map.animateCamera(CameraUpdateFactory.zoomTo(14));
        map.setMinZoomPreference(6.0f);
        map.setMaxZoomPreference(14.0f);
    }

    public void logout()
    {
        session.SetLoggedIn(false);
        startActivity(new Intent(RoomDetailsActivity.this,MainActivity.class));
        finish();
    }

}
