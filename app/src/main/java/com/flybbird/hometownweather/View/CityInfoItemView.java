package com.flybbird.hometownweather.View;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.flybbird.hometownweather.Data.CityListData;
import com.flybbird.hometownweather.R;

/**
 * Created by SuyoungKang on 2016. 1. 27..
 */
public class CityInfoItemView extends LinearLayout {
    private TextView title;
    private TextView description;


    public CityInfoItemView(Context context) {
        super(context);

        View view = inflate(context, R.layout.view_dbcity_list_item,this);
        title = (TextView)view.findViewById(R.id.TITLE_TEXTVIEW);
        description = (TextView)view.findViewById(R.id.DESC_TEXTVIEW);
    }


    public void bind(CityListData cityListData) {
        title.setText(cityListData.getName());

        String desc = String.format("%s %s",cityListData.get_id(), cityListData.getCountry() );
        description.setText(desc);
    }

}
