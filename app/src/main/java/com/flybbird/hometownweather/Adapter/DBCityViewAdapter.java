package com.flybbird.hometownweather.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.flybbird.hometownweather.Data.CityListData;
import com.flybbird.hometownweather.View.CityInfoItemView;

import co.moonmonkeylabs.realmsearchview.RealmSearchAdapter;
import co.moonmonkeylabs.realmsearchview.RealmSearchViewHolder;
import io.realm.Realm;
import io.realm.RealmViewHolder;

/**
 * Created by SuyoungKang on 2016. 1. 27..
 */
// RealmSearchAdapter<T extends RealmObject, VH extends RealmSearchViewHolder>
public class DBCityViewAdapter extends RealmSearchAdapter< CityListData , DBCityViewAdapter.DBCityHolder> {
    private DBCityViewOnItemClickListener dbCityViewClickListener;


    public DBCityViewAdapter(   Context context,
                                Realm realm,
                                String filterColumnName,
                                DBCityViewOnItemClickListener clickListener
    ) {

        super(context, realm, filterColumnName);

        this.dbCityViewClickListener = clickListener;
    }

    @Override
    public DBCityHolder onCreateRealmViewHolder(ViewGroup viewGroup, int i) {
        DBCityHolder dbCityHolder = new DBCityHolder(new CityInfoItemView(viewGroup.getContext()));
        return dbCityHolder;
    }

    @Override
    public void onBindRealmViewHolder(DBCityHolder dbCityHolder, int i) {
        final int selectIndex = i;
        final CityListData cityListData = realmResults.get(i);
        dbCityHolder.cityInfoItemView.bind(cityListData);
        dbCityHolder.cityInfoItemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ( dbCityViewClickListener != null){
                    dbCityViewClickListener.onItemClick(cityListData);
                }
            }
        });
    }

    // View holder
    public class DBCityHolder extends RealmSearchViewHolder
    {
        private CityInfoItemView cityInfoItemView;

        public DBCityHolder(CityInfoItemView itemView) {
            super(itemView);

            this.cityInfoItemView = itemView;
        }
    }

}
