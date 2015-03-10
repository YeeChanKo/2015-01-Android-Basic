package com.example.viz.week02_listview;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;


public class SimpleList1Activity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simplist1);

        ListView listView = (ListView) findViewById(R.id.listView);
        ArrayList<String> arrayList = new ArrayList<String>();

        arrayList.add("데이터1");
        arrayList.add("데이터2");
        arrayList.add("데이터3");
        arrayList.add("데이터4");
        arrayList.add("데이터5");
        arrayList.add("데이터6");
        arrayList.add("데이터7");
        arrayList.add("데이터8");
        arrayList.add("데이터9");

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, arrayList);
        listView.setAdapter(arrayAdapter);
    }
}