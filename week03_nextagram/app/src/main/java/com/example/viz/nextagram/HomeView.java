package com.example.viz.nextagram;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import org.json.JSONArray;


public class HomeView extends Activity implements OnClickListener {

    private Button button1;
    private Button button2;
    private SharedPreferences pref;
    // private static Handler handler; // singleton instance

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

//        // 핸들러 생성해주고 메시지 처리 어떻게 해야하는지 명시
//        handler = new Handler(Looper.getMainLooper()) {
//            @Override
//            public void handleMessage(Message msg) {
//                super.handleMessage(msg);
//                Log.e("HomeView", "i got the message!");
//                switch (msg.what) {
//                    case 1:
//                        listView();
//                        break;
//                }
//            }
//        };

        // 핸들러 넣어주고 나서 실행해야 함 - 서비스 onCreate에서 싱글턴 인스턴스 받아와서 초기화해준다
        // 백그라운드 동기화 서비스 실행
        // 서비스는 보안 문제 때문에 명시적 인텐트만 허용하는 걸로 버전업 되면서부터 변경됨
        Intent intentSync = new Intent(this, SyncDataService.class);
        startService(intentSync);

        listView(); // 화면 셋팅
    }

//    public static Handler getHandler() {
//        return handler;
//    }

// legacy code - listview()하니까 화면이 계속 맨 위로 올라감...;;
//    // 주기적으로 뷰 갱신해줌
//    // 액티비티가 화면에 안 떠있을 때 실행되도 되는건가?
//    public void periodicRefresh() {
//        Timer mTimer = new Timer(true); // daemon thread로 만들어줌, non-daemon thread들 끝나면 알아서 종료됨
//        mTimer.schedule(new TimerTask() {
//            @Override
//            public void run() {
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        listView();
//                    }
//                });
//            }
//        }, 1000 * 3, 1000 * 3); // 너무 자주 refresh하는 거 아닌가?
//    }

// legacy code - service로 대체
//    public void refresh() {
//        final Context context = getApplicationContext();
//        final String[] resultFromThread = new String[1]; // non-primitive type으로 쓰레드간 데이터 통신
//        Thread t = new Thread() {
//            @Override
//            public void run() {
//                // 서버에서 데이터를 가져와서 db에 넣는 부분
//                resultFromThread[0] = new Proxy(context).getJSON();
//                Log.e("getJsonData", "success: " + resultFromThread[0]);
//            }
//        };
//        t.start();
//
//        String jsonData = null;
//        try {
//            t.join();
//            jsonData = resultFromThread[0];
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//        ProviderDao dao = new ProviderDao(context);
//        // 쓰레드로부터 값이 제대로 들어왔는지 확인
//        if (jsonData != null)
//            dao.insertJsonData(jsonData); // 요것도 비동기로 이미지파일 다운로드하는 부분 있기 때문에 메인쓰레드로 빼줘야한다
//        else
//            Log.e("getJsonData", "fail: jsonData is null!");
//    }

    public void listView() {
        ListView listView = (ListView) findViewById(R.id.customlist_listview);

        Cursor cursor = getContentResolver().query(
                NextagramContract.Article.CONTENT_URI,
                NextagramContract.Article.PROJECTION_ALL, null, null,
                NextagramContract.Article.SORT_ORDER_DEFAULT
        );

        HomeViewAdapter homeViewAdapter = new HomeViewAdapter(this, cursor, R.layout.custom_list_row);

        listView.setAdapter(homeViewAdapter);
        listView.setOnItemClickListener(itemClickListener);
    }

    @Override
    public void onClick(View arg0) {
        switch (arg0.getId()) {
            case R.id.button1:
                Intent intentWrite = new Intent(arg0.getContext(), WritingArticleView.class);
                startActivity(intentWrite);
                break;
            case R.id.button2:
                try {
                    new ProviderDao(this).insertJsonData(new JSONArray(new Proxy(this).getJSON()));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    public AdapterView.OnItemClickListener itemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Intent intent = new Intent(".ArticleView");
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK); // TODO: 뭘까?
            intent.putExtra("ArticleNumber",
                    ((HomeViewAdapter.ViewHolderItem) view.getTag()).articleNumber);

            startActivity(intent);
        }
    };
}