package com.example.won.plantswater;

import android.net.Uri;
import android.util.Log;

/**
 * Created by kjs38 on 2017-11-18.
 */

public class Plants {
    private String name;
    private int water_period;
    private Uri photo;
    private String recent;
    private int _id;
    public static final String TAG = "Plants";

    public String getName(){return name;}
    public int getWater_period(){return water_period;}
    public Uri getPhoto(){return photo;}
    public String getRecent(){return recent;}
    public int getId(){return _id;}

    public void setName(String name){this.name = name;}
    public void setWater_period(int period) { this.water_period = period;}
    public void setPhoto(String photo){ //null 들어감 왜 null 인지 확인필요

        Log.d(TAG, " uri 테스트11 "+photo);
        if(photo != null)
        {
            this.photo = Uri.parse(photo);
            Log.d(TAG, " uri 테스트22 "+this.photo);
        }

    }
    public void setRecent(String recent){this.recent = recent;}
    public void setId(int id){this._id = id;}

}
