package com.example.viz.week02_listview;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;


public class CustomListActivity extends Activity implements AdapterView.OnItemClickListener {

    private ArrayList<ListData> listDataArrayList = new ArrayList<ListData>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_list);

        for (int i = 1; i < 10; i++) {
            ListData data = new ListData(i + " - 첫번째 줄", i + " - 두번째 줄", "0" + i + ".jpg");
            listDataArrayList.add(data);
        }

        for (int i = 10; i < 23; i++) {
            ListData data = new ListData(i + " - 첫번째 줄", i + " - 두번째 줄", i + ".jpg");
            listDataArrayList.add(data);
        }

        ListView listView = (ListView) findViewById(R.id.customlist_listview);
        CustomAdapter customAdapter = new CustomAdapter(this, R.layout.custom_list_row, listDataArrayList);
        listView.setAdapter(customAdapter);
        listView.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
        Log.e("TEST", position + "번 리스트 선택됨");
        Log.e("TEST", "리스트 내용" + listDataArrayList.get(position).getText1());

    }
}
