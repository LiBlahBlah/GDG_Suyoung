package com.flybbird.hometownweather.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.flybbird.hometownweather.Data.StatiscData;
import com.flybbird.hometownweather.R;
import java.util.ArrayList;


/**
 * Created by SuyoungKang on 2016. 1. 20..
 */
public class ListViewAdapter extends BaseAdapter {
    private Context mContext;

    private ArrayList<StatiscData> statiscDataArrayList ;

    public ListViewAdapter(Context context) {
        super();

        this.mContext = context;
    }

    public void setStatiscDataArrayList( ArrayList<StatiscData> dataArrayList) {
        this.statiscDataArrayList = dataArrayList;
    }



    @Override
    public int getCount() {
        if ( statiscDataArrayList == null )
            return  0;


        return statiscDataArrayList.size();
    }

    @Override
    public StatiscData getItem(int position) {
        if (statiscDataArrayList == null)
            return  null;

        return statiscDataArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;

        if (v == null) {
            LayoutInflater vi = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = vi.inflate(R.layout.view_list_item, null);
        }

        StatiscData data = statiscDataArrayList.get(position);

        if ( data != null ){
            TextView placeTextView = (TextView) v.findViewById(R.id.DATA_PLACE_NAME);
            TextView avgTemp  = (TextView) v.findViewById(R.id.DATA_PLACE_AVG_TEMP);

            placeTextView.setText( data.getCityName() );

            String avgTempstr = String.format("%.2f", data.getAvgWeather());
            avgTemp.setText( avgTempstr);
        }

        return v;
    }
}
