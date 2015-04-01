package com.example.viz.nextagram;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import java.io.File;
import java.io.FileNotFoundException;

/**
 * Created by viz on 2015. 3. 30..
 */
public class ProxyUp {

    private static AsyncHttpClient client = new AsyncHttpClient();

    public static void uploadArticle(Article article, String filePath, AsyncHttpResponseHandler responseHandler) {
        RequestParams params = new RequestParams();
        params.put("title", article.getTitle());
        params.put("writer", article.getWriter());
        params.put("id", article.getId());
        params.put("content", article.getContent());
        params.put("writeDate", article.getWriteDate());
        params.put("imgName", article.getImgName());

        try {
            params.put("uploadedfile", new File(filePath));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        client.post("http://192.168.56.1:7646/upload", params, responseHandler); // TODO: 싱글톤으로 주소 빼기
    }
}
