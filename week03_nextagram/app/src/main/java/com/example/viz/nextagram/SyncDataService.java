package com.example.viz.nextagram;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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
    private ProviderDao dao;
    // private Handler handler;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(TAG, "onCreate");

        proxy = new Proxy(getApplicationContext());
        dao = new ProviderDao(getApplicationContext());
        // handler = HomeView.getHandler();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(TAG, "onStartCommand");

        mTask = new TimerTask() {
            @Override
            public void run() {

                if (isOnline()) {

                    String jsonData = proxy.getJSON();
                    JSONArray jsonArray = null;
                    try {
                        jsonArray = new JSONArray(jsonData);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

//                TODO: 여기 오류남 -> 해결할 것
//                java.lang.NullPointerException: Attempt to invoke virtual method 'int java.lang.String.length()' on a null object reference
//                at org.json.JSONTokener.nextCleanInternal(JSONTokener.java:116)
//                at org.json.JSONTokener.nextValue(JSONTokener.java:94)
//                at org.json.JSONArray.<init>(JSONArray.java:92)
//                at org.json.JSONArray.<init>(JSONArray.java:108)
//                at com.example.viz.nextagram.SyncDataService$1.run(SyncDataService.java:53)
//                at java.util.Timer$TimerImpl.run(Timer.java:284)

                    // 요청이 200대가 아니거나, 200대여도 보내줄 내용 없을 경우 -> 여기서 처리하고 있음
                    if (jsonArray == null || jsonArray.length() == 0) {
                        Log.i(TAG, "nothing to update!");
                    } else {
                        dao.insertJsonData(jsonArray);
                        // CursorAdapter로 대체
                        // handler.sendEmptyMessage(1); // what이 1번인 빈 메시지를 보낸다
                        // 화면 갱신해달라고 요청
                        // Log.e(TAG, "message has sent!");
                    }
                } else {
                    Log.e("Network State", "not connected to internet");
                }
            }
        };

        mTimer = new Timer();
        mTimer.schedule(mTask, 0, 1000 * 5); // 바로 시작, 5초마다 주기적으로 실행

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "onDestroy");
    }

    private boolean isOnline() {
        try {
            ConnectivityManager conMan = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);

            NetworkInfo.State wifi = conMan.getNetworkInfo(1).getState(); // wifi
            if (wifi == NetworkInfo.State.CONNECTED || wifi == NetworkInfo.State.CONNECTING) {
                return true;
            }

            NetworkInfo.State mobile = conMan.getNetworkInfo(0).getState(); // mobile ConnectivityManager.TYPE_MOBILE
            if (mobile == NetworkInfo.State.CONNECTED || mobile == NetworkInfo.State.CONNECTING) {
                return true;
            }

        } catch (NullPointerException e) {
            return false;
        }

        return false;
    }
}