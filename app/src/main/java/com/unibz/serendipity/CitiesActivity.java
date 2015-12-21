package com.unibz.serendipity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.AbsListView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.unibz.serendipity.utilities.SoundList;

public class CitiesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cities);

        LinearLayout listView = (LinearLayout) findViewById(R.id.cities_list_view);

        for (String city : SoundList.cityList) {
            TextView view = new TextView(this);
            view.setTextColor(Color.WHITE);
            AbsListView.LayoutParams params = new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT, AbsListView.LayoutParams.WRAP_CONTENT);
            view.setLayoutParams(params);
            view.setText(city);
            listView.addView(view);
        }
    }

}
