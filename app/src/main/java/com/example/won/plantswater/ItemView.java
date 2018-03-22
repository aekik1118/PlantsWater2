package com.example.won.plantswater;

import android.app.Activity;

import android.content.Context;

import android.content.ContextWrapper;

import android.content.DialogInterface;
import android.content.Intent;

import android.graphics.drawable.Drawable;
import android.net.Uri;

import android.os.Build;
import android.support.v7.app.AlertDialog;
import android.support.v7.view.menu.MenuView;
import android.view.LayoutInflater;

import android.view.MenuItem;

import android.view.View;

import android.widget.Button;

import android.widget.ImageButton;
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

    private Activity getActivity(View view) {

        Context context = view.getContext();

        while (context instanceof ContextWrapper) {

            if (context instanceof Activity) {

                return (Activity)context;

            }

            context = ((ContextWrapper)context).getBaseContext();

        }

        return null;

    }

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

        bt = (Button) findViewById(R.id.button2);

    }

    private Activity getActivity() {

        Context context = getContext();

        while (context instanceof ContextWrapper) {

            if (context instanceof Activity) {

                return (Activity)context;

            }

            context = ((ContextWrapper)context).getBaseContext();

        }

        return null;

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

        if(mid == 0)

        {

            Drawable d = getResources().getDrawable(R.drawable.selected);

            bt.setBackgroundDrawable(d);

            final Button 변수명 = (Button)findViewById(R.id.button2); // 이미지 버튼을 찾는다.



            bt.setOnClickListener(new OnClickListener() { // 이미지 버튼 이벤트 정의

                @Override

                public void onClick(View v) { //클릭했을경우



//버튼 클릭 시 발생할 이벤트내용



                }

            });

            // Drawable d = getResources().getDrawable(R.drawable.waterdrop2);

            /*if(Build.VERSION.SDK_INT >= 16) {
                bt.setBackground(d);
            } else {
                bt.setBackgroundDrawable(d);
            }*/


            bt.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {

                    String sql = "UPDATE " + PlantsDB.TABLE_NAME + " SET RECENT = CURRENT_TIMESTAMP WHERE _id =" + id;

                    MainActivity.mDatabase.rawQuery(sql);

                    myAM = myAlarmManager.getInstance(view.getContext());

                    myAM.setAlarm(id,water_period,tvName.getText().toString());

                    Intent intent = new Intent(view.getContext(),MainActivity.class);

                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);

                    view.getContext().startActivity(intent);

                    getActivity(view).overridePendingTransition(0,0);

                }
            });

        }

        if(mid == 1)

        {

            Drawable d = getResources().getDrawable(R.drawable.selectedtrashcan);

            bt.setBackgroundDrawable(d);



            /*if(Build.VERSION.SDK_INT >= 16) {
                bt.setBackground(d);
            } else {
                bt.setBackgroundDrawable(d);
            }*/



            bt.setOnClickListener(new OnClickListener() {

                @Override

                public void onClick(final View view) {

                    AlertDialog.Builder alert_confirm = new AlertDialog.Builder(ItemView.this.getContext());
                    alert_confirm.setMessage("식물을 삭제하시겠습니까?").setCancelable(false).setPositiveButton("확인",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    String sql = "DELETE FROM " + PlantsDB.TABLE_NAME + " WHERE _id =" +id;

                                    MainActivity.mDatabase.rawQuery(sql);

                                    Intent intent = new Intent(view.getContext(),MainActivity.class);

                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                                    view.getContext().startActivity(intent);

                                    getActivity(view).overridePendingTransition(0,0);


                                }
                            }).setNegativeButton("취소",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    // 'No'
                                    return;
                                }
                            });
                    AlertDialog alert = alert_confirm.create();
                    alert.show();





                }
            });
        }
    }

}
