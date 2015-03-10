package com.example.viz.week02_listview;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;


public class SimpleList2Activity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simplelist2);

        ListView listView = (ListView) findViewById(R.id.listView2);
        ArrayList<HashMap<String, String>> hashMapList = new ArrayList<HashMap<String, String>>(2);

        for (int i = 0; i < 10; i++) {
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("line1", "첫번째 줄의" + i + "번");
            map.put("line2", "두번째 줄의" + i + "번");
            hashMapList.add(map);
        }
        String[] from = {"line1", "line2"};
        int[] to = {android.R.id.text1, android.R.id.text2};

        SimpleAdapter simpleAdapter = new SimpleAdapter(this, hashMapList, android.R.layout.simple_list_item_2, from, to);
        listView.setAdapter(simpleAdapter);
    }
}
