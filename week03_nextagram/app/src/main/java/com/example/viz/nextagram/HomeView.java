package com.example.viz.nextagram;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;


public class HomeView extends Activity implements AdapterView.OnItemClickListener, OnClickListener {

    private Button button1;
    private Button button2;
    private ArrayList<ArticleDTO> articleList;
    private SharedPreferences pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        pref = getSharedPreferences(getString(R.string.pref_name), MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(getString(R.string.server_ip), getString(R.string.server_ip_value));
        editor.apply(); // 리턴값 확인 안할거면 apply가 commit보다 더 빠르다
        // getResources.getString = (context/activity.getString)
        // 근데 굳이 필요할 때마다 context 받아와서 sharedpreference 여는 거면
        // R.string 테이블도 일종의 key-value인데 그럴 필요 있나?
        // ip String 값과 같이 객체라고 하기 뭐할 정도로 간단하고
        // 그다지 보안이 필요하지 않은 정보는 그냥 싱글톤으로 따로 만드는게 나은듯

        button1 = (Button) findViewById(R.id.button1);
        button2 = (Button) findViewById(R.id.button2);
        button1.setOnClickListener(this);
        button2.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();

        refresh(); // 서버에서 데이터 가져와서 db에 넣어주고 없는 이미지 파일 비동기 다운로드
        listView(); // 서버로부터 가져온 db와 이미지로 리스트뷰 만들어줌
        // View 작업은 꼭 메인쓰레드에서만 진행되어야 한다
    }

    public void refresh() {
        final Context context = getApplicationContext();
        final String[] resultFromThread = new String[1]; // non-primitive type으로 쓰레드간 데이터 통신
        Thread t = new Thread() {
            @Override
            public void run() {
                // 서버에서 데이터를 가져와서 db에 넣는 부분
                resultFromThread[0] = new Proxy(context).getJSON();
                Log.e("getJsonData", "success: " + resultFromThread[0]);
            }
        };
        t.start();

        String jsonData = null;
        try {
            t.join();
            jsonData = resultFromThread[0];
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        DAO dao = new DAO(context);
        // 쓰레드로부터 값이 제대로 들어왔는지 확인
        if (jsonData != null)
            dao.insertJsonData(jsonData);
        else
            Log.e("getJsonData", "fail: jsonData is null!");
    }

    public void listView() {
        DAO dao = new DAO(getApplicationContext());
        articleList = dao.getArticleList();
        HomeViewAdapter homeViewAdapter = new HomeViewAdapter(this, R.layout.custom_list_row, articleList);
        ListView listView = (ListView) findViewById(R.id.customlist_listview);
        listView.setAdapter(homeViewAdapter);
        listView.setOnItemClickListener(this);
    }

    @Override
    public void onClick(View arg0) {
        switch (arg0.getId()) {
            case R.id.button1:
                Intent intentWrite = new Intent(arg0.getContext(), WritingArticleView.class);
                startActivity(intentWrite);
                break;
            case R.id.button2:
                final Context context = getApplicationContext();
                refresh();
                listView();
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intentView = new Intent(".ArticleView");
        int articleNumber = articleList.get(position).getArticleNumber();
        intentView.putExtra("ArticleNumber", articleNumber);
        startActivity(intentView);
    }
}