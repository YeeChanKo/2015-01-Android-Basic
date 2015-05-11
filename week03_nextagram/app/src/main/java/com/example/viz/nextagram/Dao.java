package com.example.viz.nextagram;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by viz on 2015. 3. 16..
 */
public class DAO {
    private Context context;
    private SQLiteDatabase database;
    private SharedPreferences pref;
    private String serverIP;

    public DAO(Context context) {
        this.context = context;

        database = context.openOrCreateDatabase("LocalDATA.db", SQLiteDatabase.CREATE_IF_NECESSARY, null);

        try {
            String sql = "CREATE TABLE IF NOT EXISTS Articles (ID integer primary key autoincrement," +
                    "ArticleNumber integer UNIQUE not null," +
                    "Title text not null," +
                    "WriterName text not null," +
                    "WriterID text not null," +
                    "Content text not null," +
                    "WriteDate text not null," +
                    "ImgName text not null);";
            database.execSQL(sql);
        } catch (Exception e) {
            Log.e("test", "CREATE TABLE FAILED! - " + e);
            e.printStackTrace();
        }

        pref = context.getSharedPreferences(context.getString(R.string.pref_name), context.MODE_PRIVATE);
        serverIP = pref.getString(context.getString(R.string.server_ip), "");
    }

    // 서버에서 받아온 JSON 데이터 DB에 넣어주고 해당되는 이미지 파일 다운 받음
    public void insertJsonData(JSONArray jsonArray) {
        int articleNumber = 0; // 별 의미는 없음... 초기화 경고 떠서
        String title;
        String writer;
        String id;
        String content;
        String writeDate;
        String imgName;

        try {

            for (int i = 0; i < jsonArray.length(); ++i) {
                JSONObject jObj = jsonArray.getJSONObject(i);

                articleNumber = jObj.getInt("ArticleNumber");
                title = jObj.getString("Title");
                writer = jObj.getString("Writer");
                id = jObj.getString("Id");
                content = jObj.getString("Content");
                writeDate = jObj.getString("WriteDate");
                imgName = jObj.getString("ImgName");

                Log.i("test", "ArticleNumber: " + articleNumber + " Title: " + title);

                String sql = "INSERT OR REPLACE INTO Articles(ArticleNumber, Title, WriterName, WriterID,Content, WriteDate, ImgName)" + " VALUES(" + articleNumber + ",'" +
                        title + "','" + writer + "','" + id + "','" + content + "','" + writeDate + "','" + imgName + "');";

                try {
                    database.execSQL(sql);
                    Log.i("insertJSonData: ", "success");
                } catch (Exception e) {
                    Log.i("insertJSonData: ", "fail");
                    e.printStackTrace();
                }
                FileDownloader.downFile(context, serverIP + "/image/" + imgName, imgName); // 확장자 써줘야 하는거 아닌가?
            }

            // for 루프 끝나고 나면 articleNumber에 가장 마지막 데이터의 번호가 저장된다
            // 받아온 새로운 데이터가 있을 때만 insertJsonData 실행됨
            SharedPreferences.Editor editor = pref.edit();
            editor.putInt(context.getString(R.string.last_update_article_number_key), articleNumber);
            editor.apply();

        } catch (JSONException e) {
            Log.e("test", "JSON ERROR! - " + e);
            e.printStackTrace();
        }
    }

    public ArrayList<ArticleDTO> getArticleList() {

        ArrayList<ArticleDTO> articleList = new ArrayList<ArticleDTO>();

        int articleNumber;
        String title;
        String writer;
        String id;
        String content;
        String writeDate;
        String imgName;

        String sql = "SELECT * FROM Articles;";
        Cursor cursor = database.rawQuery(sql, null);

        while (cursor.moveToNext()) {
            articleNumber = cursor.getInt(1);
            title = cursor.getString(2);
            writer = cursor.getString(3);
            id = cursor.getString(4);
            content = cursor.getString(5);
            writeDate = cursor.getString(6);
            imgName = cursor.getString(7);

            ArticleDTO article = new ArticleDTO(articleNumber, title, writer, id, content, writeDate, imgName);
            articleList.add(article);
        }

        cursor.close();

        return articleList;
    }

    public ArticleDTO getArticleByArticleNumber(int articleNumber) {
        ArticleDTO article = null;

        String title;
        String writer;
        String id;
        String content;
        String writeDate;
        String imgName;

        String sql = "SELECT * FROM Articles WHERE ArticleNumber = " + articleNumber + ";";
        Cursor cursor = database.rawQuery(sql, null);

        cursor.moveToNext();
        articleNumber = cursor.getInt(1);
        title = cursor.getString(2);
        writer = cursor.getString(3);
        id = cursor.getString(4);
        content = cursor.getString(5);
        writeDate = cursor.getString(6);
        imgName = cursor.getString(7);

        article = new ArticleDTO(articleNumber, title, writer, id, content, writeDate, imgName);

        cursor.close();
        return article;
    }


    /**
     * JSON파싱을 위한 테스트 문자열입니다.
     * 각 데이터는 다음과 같습니다.
     * ArticleNumber - 글번호 중복X 숫자
     * Title - 글제목 문자열
     * Writer - 작성자
     * Id - 작성자ID
     * Content - 글내용
     * WriteDate - 작성일
     * ImgName - 사진명
     */
    public String getJsonTestData() {

        StringBuilder sb = new StringBuilder();
        sb.append("");

        sb.append("[");

        sb.append("      {");
        sb.append("         'ArticleNumber':'1',");
        sb.append("         'Title':'오늘도 좋은 하루',");
        sb.append("         'Writer':'학생1',");
        sb.append("         'Id':'6613d02f3e2153283f23bf621145f877',");
        sb.append("         'Content':'하지만 곧 기말고사지...',");
        sb.append("         'WriteDate':'2013-09-23-10-10',");
        sb.append("         'ImgName':'photo1.jpg'");
        sb.append("      },");
        sb.append("      {");
        sb.append("         'ArticleNumber':'2',");
        sb.append("         'Title':'대출 최고 3000만원',");
        sb.append("         'Writer':'김미영 팀장',");
        sb.append("         'Id':'6326d02f3e2153266f23bf621145f734',");
        sb.append("         'Content':'김미영팀장입니다. 고갱님께서는 최저이율로 최고 3000만원까지 30분 이내 통장입금가능합니다.',");
        sb.append("         'WriteDate':'2013-09-24-11-22',");
        sb.append("         'ImgName':'photo2.jpg'");
        sb.append("      },");
        sb.append("      {");
        sb.append("         'ArticleNumber':'3',");
        sb.append("         'Title':'MAC등록신청',");
        sb.append("         'Writer':'학생2',");
        sb.append("         'Id':'8426d02f3e2153283246bf6211454262',");
        sb.append("         'Content':'1a:2b:3c:4d:5e:6f',");
        sb.append("         'WriteDate':'2013-09-25-12-33',");
        sb.append("         'ImgName':'photo3.jpg'");
        sb.append("      }");

        sb.append("]");

        return sb.toString();
    }
}
