package com.example.won.plantswater;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

/**
 * Created by kjs38 on 2017-11-18.
 */

public class PlantsListAdapter extends BaseAdapter {
    private List plants;
    private Context context;



    

    public  PlantsListAdapter(List plants, Context context){
        this.plants = plants;
        this.context = context;
    }

    @Override
    public int getCount() { return this.plants.size(); }

    @Override
    public Object getItem(int position) { return this.plants.get(position);}

    @Override
    public long getItemId(int position) {return position;}


    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
        Holder holder = null;

        if (convertView == null) {
            convertView = new LinearLayout(context);
            ((LinearLayout) convertView).setOrientation(LinearLayout.HORIZONTAL);

            TextView tvName = new TextView(context);
            tvName.setPadding(10, 0, 20, 0);

            TextView tvPeriod = new TextView(context);
            tvPeriod.setPadding(20, 0, 20, 0);

            ImageView imPhoto = new ImageView(context);
            imPhoto.setPadding(20, 0, 20, 0);

            TextView tvRecent = new TextView(context);
            tvRecent.setPadding(20, 0, 20, 0);

            Button btWater = new Button(context);
            btWater.setPadding(10, 0, 10, 0);

            Button btDelete = new Button(context);
            btWater.setPadding(10, 0, 10, 0);

            ((LinearLayout) convertView).addView(tvName);
            ((LinearLayout) convertView).addView(tvPeriod);
            ((LinearLayout) convertView).addView(imPhoto);
            ((LinearLayout) convertView).addView(tvRecent);
            ((LinearLayout) convertView).addView(btWater);
            ((LinearLayout) convertView).addView(btDelete);

            holder = new Holder();
            holder.tvName = tvName;
            holder.tvPeriod = tvPeriod;
            holder.imPhoto = imPhoto;
            holder.tvRecent = tvRecent;
            holder.btWater = btWater;
            holder.btDelete = btDelete;

            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }

        final Plants plant = (Plants) getItem(position);
        holder.tvName.setText(plant.getName() + "");
        holder.tvPeriod.setText(plant.getWater_period() + "");
        holder.imPhoto.setImageBitmap(plant.getPhoto());
        holder.tvRecent.setText(plant.getRecent() + "");
        holder.btWater.setText(plant.getId() + "");

        holder.btWater.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String sql = "UPDATE " + PlantsDB.TABLE_NAME + " SET RECENT = CURRENT_TIMESTAMP WHERE _id =" + plant.getId();
                MainActivity.mDatabase.rawQuery(sql);
                Intent intent = new Intent(view.getContext(),MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                view.getContext().startActivity(intent);
                ((Activity)view.getContext()).overridePendingTransition(0,0);
            }
        });

        holder.btDelete.setText("삭제");

        return convertView;
    }

}

class Holder {
    public TextView tvName;
    public TextView tvPeriod;
    public ImageView imPhoto;
    public TextView tvRecent;
    public Button btWater;
    public Button btDelete;
}


