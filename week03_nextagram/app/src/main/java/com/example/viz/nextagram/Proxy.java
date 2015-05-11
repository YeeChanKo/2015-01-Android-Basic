package com.example.viz.nextagram;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.apache.http.Header;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by viz on 2015. 3. 25..
 */
public class Proxy {

    private static AsyncHttpClient client = new AsyncHttpClient();

    private Context context;
    private SharedPreferences pref;
    private String serverIP;

    public Proxy(Context context) {
        this.context = context;
        pref = context.getSharedPreferences(context.getString(R.string.pref_name), context.MODE_PRIVATE);
        serverIP = pref.getString(context.getString(R.string.server_ip), "");
    }

    // 허니콤 이후로 메인쓰레드에서 네트워크 작업하면 오류 난다
    // 별도 쓰레드 만들어서 작업 처리해줘야 함
    public String getJSON() {

        try {
            int articleNumber = pref.getInt(context.getString(R.string.last_update_article_number_key), 0);

            URL url = new URL(serverIP + "/loadData?ArticleNumber=" + articleNumber);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setConnectTimeout(10 * 1000);
            conn.setReadTimeout(10 * 1000);

            conn.setRequestMethod("GET");
            conn.setRequestProperty("Connection", "Keep-Alive");
            conn.setRequestProperty("Accept-Charset", "UTF-8");
            conn.setRequestProperty("Cache-Control", "no-cache");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoInput(true);

            conn.connect();

            int status = conn.getResponseCode();
            Log.i("test", "ProxyResponseCode: " + status);

            switch (status) {
                case 200:
                case 201:
                    BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    StringBuilder sb = new StringBuilder();
                    String line;
                    while ((line = br.readLine()) != null) {
                        sb.append(line + "\n");
                    }
                    br.close();
                    return sb.toString();
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.i("test", "NETWORK ERROR: " + e);
        }

        return null;
    }
}