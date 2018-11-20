package com.example.sujon335.loginregister;

import android.app.VoiceInteractor;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class BookingRequestsActivity extends AppCompatActivity {
    SQLiteDatabase AirBnbDb=null;
    List<BookingRequest> requests=new ArrayList<BookingRequest>();
    UserSessionManager session;
    int userid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_requests);

        session=new UserSessionManager(this);
        if(!session.IsLoggedIn())
        {
            logout();
        }
        userid=session.getUserId();
        requests=GetRequestsFromDatabase();

        TextView no_data=(TextView) findViewById(R.id.no_requests);
        if(requests.size()==0)
            no_data.setVisibility(View.VISIBLE);

        ListView booking_list=(ListView) findViewById(R.id.booking_req_list);
        final BookingRequestListAdapter adapter=new BookingRequestListAdapter(BookingRequestsActivity.this,requests);
        booking_list.setAdapter(adapter);



    }




    public List<BookingRequest> GetRequestsFromDatabase()
    {
        List<BookingRequest> reqs=new ArrayList<BookingRequest>();
        try{
            AirBnbDb=this.openOrCreateDatabase("AirbnbDB",MODE_PRIVATE,null);
            Cursor c=AirBnbDb.rawQuery("SELECT * FROM bookingRequest as br JOIN place as p ON br.placeid=p.placeid " +
                    "JOIN guest as g ON br.guestid=g.id WHERE p.hostid="+userid,null);


            if(c!=null && c.getCount()>0)
            {
                while (c.moveToNext()){
                    int reqid=c.getInt(c.getColumnIndex("reqid"));
                    String title=c.getString(c.getColumnIndex("title"));
                    String location=c.getString(c.getColumnIndex("location"));
                    String Guest=c.getString(c.getColumnIndex("name"));
                    String email=c.getString(c.getColumnIndex("email"));
                    String time=c.getString(c.getColumnIndex("bookingreqTime"));
                    //Date date = new SimpleDateFormat("EEE, MMM d, ''yy h:mm a").parse(time);
                    //time=date.toString();

                   // SimpleDateFormat dateformat=new SimpleDateFormat("EEE, MMM d, ''yy h:mm a",new Locale("en_US"));


                    BookingRequest req=new BookingRequest(reqid,title,location,Guest,email,time);
                    reqs.add(req);
                }
            }
            c.close();


        }
        catch (Error e){

            Log.e("AirBNB", "exception", e);
        }
        return reqs;
    }

    public void revomeBookingRequest(int reqid)
    {

        try {
            AirBnbDb = this.openOrCreateDatabase("AirbnbDB", MODE_PRIVATE, null);

            AirBnbDb.execSQL("DELETE from bookingRequest WHERE reqid=" + reqid);
        }
        catch (Exception e)
        {

            Log.e("AirBNB", "exception", e);

        }
    }

    public void confirmBooking(int reqid)
    {
        int placeid=0,guestid=0;
        String email="";
        try {
            AirBnbDb = this.openOrCreateDatabase("AirbnbDB", MODE_PRIVATE, null);
            Cursor c=AirBnbDb.rawQuery("SELECT * FROM bookingRequest as br JOIN guest as g on br.guestid=g.id  WHERE reqid="+reqid,null);
            if(c!=null && c.getCount()>0)
            {
                while (c.moveToNext()){
                    placeid=c.getInt(c.getColumnIndex("placeid"));
                    guestid=c.getInt(c.getColumnIndex("guestid"));
                    email=c.getString(c.getColumnIndex("email"));
                }
            }
            AirBnbDb.execSQL("INSERT INTO bookingRecord (placeid,guestid) VALUES ('"+placeid+"', '"+guestid+"');" );
            AirBnbDb.execSQL("UPDATE place SET isavailable=0 WHERE placeid="+placeid);
            AirBnbDb.execSQL("DELETE from bookingRequest WHERE placeid=" + placeid);
           sendConfirmationEmail(email);
        }
        catch (Exception e)
        {

            Log.e("AirBNB", "exception", e);

        }
    }

    public void sendConfirmationEmail(String email)
    {
        Intent i = new Intent(Intent.ACTION_SEND);
        i.setType("message/rfc822");
        i.putExtra(Intent.EXTRA_EMAIL, new String[]{ email});
        i.putExtra(Intent.EXTRA_SUBJECT, "AirBNB: Booking Request Of Your Room Confirmed");
        i.putExtra(Intent.EXTRA_TEXT, "Your room booking request in AirBNB has been confirmed check the app for details");
        try {
            startActivity(Intent.createChooser(i, "Send mail..."));
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(BookingRequestsActivity.this, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
        }
    }

    public void logout()
    {
        session.SetLoggedIn(false);
        startActivity(new Intent(BookingRequestsActivity.this,MainActivity.class));
        finish();
    }
}
