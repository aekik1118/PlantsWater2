package com.example.won.plantswater;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by kjs38 on 2017-11-18.
 */

public class PlantsListAdapter extends BaseAdapter {
    private List plants;
    private Context context;
    private int id;

    public static final String TAG = "PlantsListAdapter";

    public  PlantsListAdapter(List plants, Context context, int id){
        this.plants = plants;
        this.context = context;
        this.id = id;
    }

    @Override
    public int getCount() { return this.plants.size(); }

    @Override
    public Object getItem(int position) { return this.plants.get(position);}

    @Override
    public long getItemId(int position) {return position;}


    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {

        ItemView itemView = null;

        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.item_view,null);
            itemView = new ItemView(context);
            itemView.imPhoto = (ImageView)convertView.findViewById(R.id.imageView);
            itemView.tvName = (TextView)convertView.findViewById(R.id.textView2);
            itemView.tvRecent = (TextView)convertView.findViewById(R.id.textView3);
            itemView.bt = (ImageButton)convertView.findViewById(R.id.button2);
            convertView.setTag(itemView);
        }
        else
        {
            itemView = (ItemView)convertView.getTag();
        }

        final Plants plant = (Plants) getItem(position);

        itemView.setName(plant.getName() + "");
        //itemView.imPhoto.setImageURI(plant.getPhoto());
        Log.d(TAG, " uri 테스트 "+plant.getName() +plant.getPhoto());

        itemView.setImPhoto(plant.getPhoto());
        itemView.setTvRecent(plant.getRecent() + "");
        itemView.setBt(plant.getId(),plant.getWater_period(), id);

        return convertView;
    }

}
//
//class Holder {
//    public TextView tvName;
//    public ImageView imPhoto;
//    public TextView tvRecent;
//    public Button btWater;
//    public Button btDelete;
//}


