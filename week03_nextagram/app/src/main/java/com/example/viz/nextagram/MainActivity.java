package com.example.viz.nextagram;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;


public class MainActivity extends Activity implements AdapterView.OnItemClickListener {

    private ArrayList<ListData> listDataArrayList = new ArrayList<ListData>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //ListData data = new ListData();
        //listDataArrayList.add(data);

        ListView listView = (ListView) findViewById(R.id.customlist_listview);
        CustomAdapter customAdapter = new CustomAdapter(this, R.layout.custom_list_row, listDataArrayList);
        listView.setAdapter(customAdapter);
        listView.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {

    }
}
