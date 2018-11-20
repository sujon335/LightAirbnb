package com.example.sujon335.loginregister;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.util.DisplayMetrics;

import java.util.ArrayList;
import java.util.List;

public class hosthome extends AppCompatActivity {

    UserSessionManager session;
    int userid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hosthome);

        session=new UserSessionManager(this);
        if(!session.IsLoggedIn())
        {
            logout();
        }
        userid=session.getUserId();


        List<String> hostOptions=new ArrayList<String>();
        hostOptions.add("List a new room");
        hostOptions.add("Your rooms");
        hostOptions.add("Booking Requests");
        hostOptions.add("LogOut");

        ListView hostoptions=(ListView) findViewById(R.id.host_options);
        HostOptionsListAdapter adapter=new HostOptionsListAdapter(this,hostOptions);
        //ArrayAdapter<String> adapter=new ArrayAdapter<String>(this,R.layout.host_home_list_text_view,hostOptions);
        hostoptions.setAdapter(adapter);

        hostoptions.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch(position){
                    case 0:
                        startActivity(new Intent(hosthome.this,hostRegister.class));
                        //finish();
                        break;
                    case 1:
                        startActivity(new Intent(hosthome.this,HostRoomsActivity.class));
                        //Toast.makeText(hosthome.this,"Your rooms",Toast.LENGTH_SHORT).show();
                        break;
                    case 2:
                        startActivity(new Intent(hosthome.this,BookingRequestsActivity.class));
                        //Toast.makeText(hosthome.this,"Booking Requests",Toast.LENGTH_SHORT).show();
                        break;
                    case 3:
                        logout();
                        break;
                }

            }
        });


    }




    public void logout()
    {
        session.SetLoggedIn(false);
        startActivity(new Intent(hosthome.this,MainActivity.class));
        finish();
    }
}
