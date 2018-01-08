package com.example.won.plantswater;

import android.graphics.Bitmap;

/**
 * Created by kjs38 on 2017-11-18.
 */

public class Plants {
    private String name;
    private int water_period;
    private Bitmap photo;
    private String recent;
    private int _id;


    public String getName(){return name;}
    public int getWater_period(){return water_period;}
    public Bitmap getPhoto(){return photo;}
    public String getRecent(){return recent;}
    public int getId(){return _id;}

    public void setName(String name){this.name = name;}
    public void setWater_period(int period) { this.water_period = period;}
    public void setPhoto(Bitmap photo){this.photo = photo;}
    public void setRecent(String recent){this.recent = recent;}
    public void setId(int id){this._id = id;}

}
