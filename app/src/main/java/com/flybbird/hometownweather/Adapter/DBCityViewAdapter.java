package com.flybbird.hometownweather.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
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

    public DBCityViewAdapter(   Context context,
                                Realm realm,
                                String filterColumnName) {

        super(context, realm, filterColumnName);
    }

    @Override
    public DBCityHolder onCreateRealmViewHolder(ViewGroup viewGroup, int i) {
        DBCityHolder dbCityHolder = new DBCityHolder(new CityInfoItemView(viewGroup.getContext()));
        return dbCityHolder;
    }

    @Override
    public void onBindRealmViewHolder(DBCityHolder dbCityHolder, int i) {
        final CityListData cityListData = realmResults.get(i);
        dbCityHolder.cityInfoItemView.bind(cityListData);
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
