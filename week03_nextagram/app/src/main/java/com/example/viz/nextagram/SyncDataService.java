package com.example.viz.nextagram;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by viz on 15. 5. 11..
 */
public class SyncDataService extends Service {

    private static final String TAG = SyncDataService.class.getSimpleName();

    private Timer mTimer;
    private TimerTask mTask;
    private Proxy proxy;
    private DAO dao;
    private Handler handler;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(TAG, "onCreate");

        proxy = new Proxy(getApplicationContext());
        dao = new DAO(getApplicationContext());
        handler = HomeView.getHandler();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(TAG, "onStartCommand");

        mTask = new TimerTask() {
            @Override
            public void run() {
                String jsonData = proxy.getJSON();
                JSONArray jsonArray = null;
                try {
                    jsonArray = new JSONArray(jsonData);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                // 요청이 200대가 아니거나, 200대여도 보내줄 내용 없을 경우 -> 여기서 처리하고 있음
                if (jsonArray == null || jsonArray.length() == 0) {
                    Log.i(TAG, "nothing to update!");
                } else {
                    dao.insertJsonData(jsonArray);
                    handler.sendEmptyMessage(1); // what이 1번인 빈 메시지를 보낸다
                    // 화면 갱신해달라고 요청
                    Log.e(TAG, "message has sent!");
                }
            }
        };

        mTimer = new Timer();
        mTimer.schedule(mTask, 1000 * 5, 1000 * 5); // 5초 후 시작, 5초마다 주기적으로 실행

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "onDestroy");
    }
}