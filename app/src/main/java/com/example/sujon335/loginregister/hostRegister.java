package com.example.sujon335.loginregister;

import android.content.Intent;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;

public class hostRegister extends AppCompatActivity {

    int PLACE_REQUEST=1;
    Spinner spinner_room_type,property_type;
    EditText totalGuest,beds,baths,price,minStay,title,contact;
    EditText location;
    double longitude,latitude;
    CardView hostregisterbutton;
    SQLiteDatabase AirBnbDb=null;
    UserSessionManager session;
    int userid;
    String roomtypeval,propertytypeval;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_host_register);


        session=new UserSessionManager(this);
        if(!session.IsLoggedIn())
        {
            logout();
        }
        userid=session.getUserId();

        spinner_room_type = (Spinner) findViewById(R.id.room_type);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.room_array, R.layout.white_spinner);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_room_type.setAdapter(adapter);
        spinner_room_type.setSelection(0);





        property_type = (Spinner) findViewById(R.id.property_type);
        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(this,R.array.property_array, R.layout.white_spinner);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        property_type.setAdapter(adapter2);
        property_type.setSelection(0);



        location=(EditText) findViewById(R.id.location);
        location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PlacePicker.IntentBuilder builder=new PlacePicker.IntentBuilder();
                Intent intent;
                Context context = getApplicationContext();
                try {
                    intent= builder.build(hostRegister.this);
                    startActivityForResult(intent,PLACE_REQUEST);
                } catch (GooglePlayServicesRepairableException e) {
                    e.printStackTrace();
                } catch (GooglePlayServicesNotAvailableException e) {
                    e.printStackTrace();
                }
            }
        });

        title=(EditText) findViewById(R.id.title);
        totalGuest=(EditText) findViewById(R.id.tot_guest);
        beds=(EditText)findViewById(R.id.No_of_beds);
        baths=(EditText) findViewById(R.id.No_of_baths);
        price=(EditText) findViewById(R.id.price);
        minStay=(EditText) findViewById(R.id.min_stay);
        contact=(EditText) findViewById(R.id.Phonenumber);

        hostregisterbutton=(CardView) findViewById(R.id.cardView2);
        hostregisterbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if( title.getText().toString().length() == 0 )
                    title.setError( "title is required!" );
                else if( totalGuest.getText().toString().length() == 0 )
                    totalGuest.setError( "totalGuest is required!" );
                else if( beds.getText().toString().length() == 0 )
                    beds.setError( "No. Of beds is required!" );
                else if( minStay.getText().toString().length() == 0 )
                    minStay.setError( "minStay is required!" );
                else if( baths.getText().toString().length() == 0 )
                    baths.setError( "No. of baths is required!" );
                else if( price.getText().toString().length() == 0 )
                    price.setError( "price is required!" );
                else if(location.getText().toString().length()==0)
                    location.setError( "location is required!" );
                else if(contact.getText().toString().length()==0)
                    contact.setError( "contact is required!" );
                else {

                    if (insertHostRegisterInfo()) {
                        if (session.isUserHost()) {
                            Toast.makeText(hostRegister.this, "Successfully Added a new room", Toast.LENGTH_LONG).show();
                            startActivity(new Intent(hostRegister.this, hosthome.class));
                            finish();
                        } else {
                            Toast.makeText(hostRegister.this, "Successfully registered as a host now try Login as host", Toast.LENGTH_LONG).show();
                            logout();
                        }

                    } else {
                        Toast.makeText(hostRegister.this, "Something went wrong try again", Toast.LENGTH_SHORT).show();
                    }
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
                latitude = place.getLatLng().latitude;
                longitude = place.getLatLng().longitude;
                String address=place.getAddress().toString();
                location.setText(address);
            }
        }
    }

    public boolean insertHostRegisterInfo(){
        try{
            AirBnbDb=this.openOrCreateDatabase("AirbnbDB",MODE_PRIVATE,null);

            AirBnbDb.execSQL("UPDATE guest SET ishost=1 WHERE id="+userid);



            roomtypeval=spinner_room_type.getSelectedItem().toString();
            propertytypeval=property_type.getSelectedItem().toString();
            String titleval=title.getText().toString();
            String totalguestsval=totalGuest.getText().toString();
            String bedsval=beds.getText().toString();
            String bathsval=baths.getText().toString();
            String minstayval=minStay.getText().toString();
            String locationval=location.getText().toString();
            String contactval=contact.getText().toString();

            String priceval =price.getText().toString();


            AirBnbDb.execSQL("INSERT INTO place (title,location,longitude,latitude,roomtype," +
                    "propertytype,totalguests,beds,baths,minstay,contact,price,isavailable,hostid) " +
                    "VALUES ('"+titleval+"', '"+locationval+"',"+longitude+"," +
                    ""+latitude+", '"+roomtypeval+"','"+propertytypeval+"'" +
                    ","+totalguestsval+", "+bedsval+","+bathsval+","+minstayval+", " +
                    ""+contactval+","+priceval+",1,"+userid+");" );

            return true;

        }
        catch (Exception e)
        {
            Toast.makeText(this,"Exception occured while inserting",Toast.LENGTH_SHORT).show();
            Log.e("AirBNB", "exception", e);
            return false;
        }
    }

    public void logout()
    {
        session.SetLoggedIn(false);
        startActivity(new Intent(hostRegister.this,MainActivity.class));
        finish();
    }

}
