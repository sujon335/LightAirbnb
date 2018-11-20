package com.example.sujon335.loginregister;

import android.app.DatePickerDialog;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

public class Register extends AppCompatActivity {

    SQLiteDatabase AirBnbDb=null;
    CardView registerbutton;
    EditText name,email,password,birthdate;
    int yearval,monthval,dayval;
    static final int dilog_id=0;
    final Calendar cal=Calendar.getInstance();

    private DatePickerDialog.OnDateSetListener datepickerListener=new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                yearval=year;
                monthval=month+1;
                dayval=dayOfMonth;
                String selectedDate=yearval+"-"+monthval+"-"+dayval;
                birthdate.setText(selectedDate);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        yearval=cal.get(Calendar.YEAR);
        monthval=cal.get(Calendar.MONTH);
        dayval=cal.get(Calendar.DAY_OF_MONTH);

        name=(EditText) findViewById(R.id.editText3);
        email=(EditText) findViewById(R.id.editText4);
        password=(EditText) findViewById(R.id.editText5);
        birthdate=(EditText) findViewById(R.id.editText7);
        registerbutton=(CardView) findViewById(R.id.cardView);



        registerbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if( name.getText().toString().length() == 0 )
                    name.setError( "name is required!" );
                else if( email.getText().toString().length() == 0 )
                    email.setError( "email is required!" );
                else if(!isEmailValid(email.getText().toString()))
                    email.setError( "valid email is required!" );
                else if( password.getText().toString().length() == 0 )
                    password.setError( "password is required!" );
                else if(birthdate.getText().toString().length()==0)
                    birthdate.setError( "birthdate is required!" );
                else {
                    if (insertRegisterInfo()) {
                        Toast.makeText(Register.this, "Successfully registered now try Login", Toast.LENGTH_LONG).show();

                        startActivity(new Intent(Register.this, MainActivity.class));
                        finish();
                    } else {
                        Toast.makeText(Register.this, "Something went wrong try again", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });


        birthdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(dilog_id);
            }
        });
    }

    @Override
    protected Dialog onCreateDialog(int id)
    {
        if(id==dilog_id)
            return new DatePickerDialog(this,AlertDialog.THEME_DEVICE_DEFAULT_DARK,datepickerListener,yearval,monthval,dayval);
        return null;
    }

    public boolean insertRegisterInfo(){
        try{
            AirBnbDb=this.openOrCreateDatabase("AirbnbDB",MODE_PRIVATE,null);
            AirBnbDb.execSQL("CREATE TABLE IF NOT EXISTS guest " +"(id integer primary key AUTOINCREMENT, name VARCHAR, email VARCHAR,password VARCHAR,ishost integer Default 0,birthdate DATE);");

            String guestname=name.getText().toString();
            String guestpassword=password.getText().toString();
            String guestemail=email.getText().toString();
            String guestbirthdate=birthdate.getText().toString();
            AirBnbDb.execSQL("INSERT INTO guest (name,email,password,ishost,birthdate) " +
                    "VALUES ('"+guestname+"', '"+guestemail+"','"+guestpassword+"',0,'"+guestbirthdate+"');" );

            return true;

        }
        catch (Exception e)
        {
            Log.e("creation error", "database error");
            return false;
        }
    }

    public boolean isEmailValid(String email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email)
                .matches();
    }

}
