package com.example.sujon335.loginregister;

import android.content.Intent;
import android.view.Window;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    SQLiteDatabase AirBnbDb=null;
    EditText useremail,password;
    UserSessionManager session;
    CheckBox hostchecked;
    int id;
    String name,email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        CreateAllDatabaseTables();

        session=new UserSessionManager(this);

        if(session.IsLoggedIn())  //if already looged in
        {
            if(session.isUserHost()) startActivity(new Intent(MainActivity.this,hosthome.class));
            else startActivity(new Intent(MainActivity.this,homeActivity.class));
            finish();
        }


        useremail=(EditText) findViewById(R.id.email);
        password=(EditText) findViewById(R.id.pass);
        hostchecked=(CheckBox) findViewById(R.id.logincheckbox);
        final CardView loginbutton=(CardView) findViewById(R.id.loginbutton);
        final TextView register=(TextView) findViewById(R.id.register);

        loginbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(hostchecked.isChecked())
                {
                    if (IsValidHost()) {
                    session.SetLoggedIn(true);
                    session.SetUserData(id, name, email,true);
                    startActivity(new Intent(MainActivity.this, hosthome.class));
                    finish();
                    } else {
                    Toast.makeText(MainActivity.this, "Username and Password Mismatch!! try again", Toast.LENGTH_SHORT).show();
                    }

                }
                else {
                    if (IsValidUser()) {
                        session.SetLoggedIn(true);
                        session.SetUserData(id, name, email,false);
                        startActivity(new Intent(MainActivity.this, homeActivity.class));
                        finish();
                    } else {
                        Toast.makeText(MainActivity.this, "Username and Password Mismatch!! try again", Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,Register.class));
                finish();
            }
        });


    }



    public void CreateAllDatabaseTables()
    {
        AirBnbDb=this.openOrCreateDatabase("AirbnbDB",MODE_PRIVATE,null);
        //Creating Guest table
        AirBnbDb.execSQL("CREATE TABLE IF NOT EXISTS guest " +"(id integer primary key AUTOINCREMENT, name VARCHAR, " +
                "email VARCHAR,password VARCHAR,ishost integer Default 0,birthdate DATE);");

        //Creating place table
        AirBnbDb.execSQL("CREATE TABLE IF NOT EXISTS place "
                +"(placeid integer primary key AUTOINCREMENT, title TEXT, location TEXT,longitude REAL, latitude REAL," +
                "roomtype VARCHAR,propertytype VARCHAR,totalguests integer," +
                "beds integer,baths integer,minstay integer,contact integer,price integer," +
                "isavailable integer,hostid integer);");

        //creating booking request table

        AirBnbDb.execSQL("CREATE TABLE IF NOT EXISTS bookingRequest " +"(reqid integer primary key AUTOINCREMENT,guestid int," +
                "placeid integer,bookingreqTime DATE);");

        AirBnbDb.execSQL("CREATE TABLE IF NOT EXISTS bookingRecord " +"(recordid integer primary key AUTOINCREMENT,placeid int," +
                "guestid integer);");

    }

     public boolean IsValidUser()
     {
         try{
             AirBnbDb=this.openOrCreateDatabase("AirbnbDB",MODE_PRIVATE,null);

             String guestemail=useremail.getText().toString();
             String guestpassword=password.getText().toString();
             String query="SELECT * FROM guest WHERE email = ? AND password = ?";
             Cursor c=AirBnbDb.rawQuery(query, new String[] {guestemail,guestpassword});
             if(c!=null && c.getCount()>0)
             {
                 c.moveToFirst();
                 id=c.getInt(c.getColumnIndex("id"));
                 name=c.getString(c.getColumnIndex("name"));
                 email=c.getString(c.getColumnIndex("email"));
                 return true;
             }
             c.close();
         }
         catch (Error e){
             Log.e("creation error", "database error");
             return false;
         }
         return false;
     }

    public boolean IsValidHost()
    {
        try{
            AirBnbDb=this.openOrCreateDatabase("AirbnbDB",MODE_PRIVATE,null);

            String guestemail=useremail.getText().toString();
            String guestpassword=password.getText().toString();
            String query="SELECT * FROM guest WHERE email = ? AND password = ? AND ishost=1";
            Cursor c=AirBnbDb.rawQuery(query, new String[] {guestemail,guestpassword});
            if(c!=null && c.getCount()>0)
            {
                c.moveToFirst();
                id=c.getInt(c.getColumnIndex("id"));
                name=c.getString(c.getColumnIndex("name"));
                email=c.getString(c.getColumnIndex("email"));
                return true;
            }

        }
        catch (Error e){
            Log.e("creation error", "database error");
            return false;
        }
        return false;
    }

}
