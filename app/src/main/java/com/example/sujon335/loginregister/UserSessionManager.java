package com.example.sujon335.loginregister;

import android.content.SharedPreferences;
import android.content.Context;

/**
 * Created by sujon335 on 2/5/18.
 */

public class UserSessionManager {
    SharedPreferences pref;

    SharedPreferences.Editor editor;

    Context ctx;


    public UserSessionManager(Context ctx)
    {
        this.ctx=ctx;
        pref=ctx.getSharedPreferences("airbnb",Context.MODE_PRIVATE);
        editor=pref.edit();
    }
    public void SetLoggedIn(boolean loggedin)
    {
        editor.putBoolean("loggedInMode",loggedin);
        if(!loggedin) editor.clear();
        editor.commit();
    }

    public boolean IsLoggedIn()
    {
        return pref.getBoolean("loggedInMode",false);
    }

    public void SetUserData(int id, String name,String email,boolean ishost)
    {
        editor.putString("UserName",name);
        editor.putString("UserEmail",email);
        editor.putInt("UserID",id);
        if(ishost) editor.putBoolean("IsHost",true);
        editor.commit();
    }

    public int getUserId()
    {
        return pref.getInt("UserID",0);
    }
    public String getUserName()
    {
        return pref.getString("UserName","");
    }
    public String getUserEmail()
    {
        return pref.getString("UserEmail","");
    }
    public boolean isUserHost(){ return  pref.getBoolean("IsHost",false);}

}
