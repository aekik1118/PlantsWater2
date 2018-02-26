package com.example.won.plantswater;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import static com.example.won.plantswater.MainActivity.myAM;

/**
 * Created by aekik on 2017-12-29.
 */

public class ItemView extends LinearLayout{
    ImageView imPhoto;
    TextView tvName;
    TextView tvRecent;
    Button bt;

    ItemView(Context context)
    {
        super(context);
        init(context);
    }

    public void init(Context context)
    {
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.item_view,this,true);

        imPhoto = (ImageView)findViewById(R.id.imageView);
        tvName = (TextView)findViewById(R.id.textView2);
        tvRecent = (TextView)findViewById(R.id.textView3);
        bt = (Button)findViewById(R.id.button2);
    }

    public void setName(String name)
    {
        tvName.setText(name);
    }

    public void setImPhoto(Uri photo)
    {
        imPhoto.setImageURI(photo);
    }

    public void setTvRecent(String Recent)
    {
        tvRecent.setText(Recent);
    }

    public void setBt(final int id, final int water_period, int mid)
    {
        if(mid == 1)
        {
            bt.setText("물주기");

            bt.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    String sql = "UPDATE " + PlantsDB.TABLE_NAME + " SET RECENT = CURRENT_TIMESTAMP WHERE _id =" + id;
                    MainActivity.mDatabase.rawQuery(sql);

                    myAM = myAlarmManager.getInstance(view.getContext());
                    myAM.setAlarm(id,water_period,tvName.getText().toString());

                    Intent intent = new Intent(view.getContext(),MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    view.getContext().startActivity(intent);
                    ((Activity)view.getContext()).overridePendingTransition(0,0);

                }
            });
        }

        if(mid == 2)
        {
            bt.setText("삭제");

            bt.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    String sql = "DELETE FROM " + PlantsDB.TABLE_NAME + " WHERE _id =" +id;
                    MainActivity.mDatabase.rawQuery(sql);
                    Intent intent = new Intent(view.getContext(),MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    view.getContext().startActivity(intent);
                    ((Activity)view.getContext()).overridePendingTransition(0,0);
                }
            });
        }
    }

}
