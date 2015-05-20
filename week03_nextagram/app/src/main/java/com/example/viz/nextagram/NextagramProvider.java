package com.example.viz.nextagram;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.text.TextUtils;

import java.util.Locale;

/**
 * Created by viz on 15. 5. 12..
 */
public class NextagramProvider extends ContentProvider {

    private Context context;
    private SQLiteDatabase database;
    private final String TABLE_NAME = "Article";

    private static final int ARTICLE_LIST = 1;
    private static final int ARTICLE_ID = 2;
    private static final UriMatcher URI_MATCHER; // query, insert에서 사용할 URI 정보를 관리

    static {
        URI_MATCHER = new UriMatcher(UriMatcher.NO_MATCH);
        URI_MATCHER.addURI(NextagramContract.AUTHORITY, "Article", ARTICLE_LIST);
        URI_MATCHER.addURI(NextagramContract.AUTHORITY, "Article/#", ARTICLE_ID);
    }

    // db - create or open db, & settings
    private void SQLiteInitialize() {
        database = context.openOrCreateDatabase("nextagram.db", SQLiteDatabase.CREATE_IF_NECESSARY, null);
        database.setLocale(Locale.getDefault());
        database.setVersion(1);
    }

    // db - create article table
    private void createTable() {
        String sql = "CREATE TABLE IF NOT EXISTS " +
                TABLE_NAME +
                "(ID integer primary key autoincrement," +
                "_id integer UNIQUE not null," +
                "Title text not null," +
                "WriterName text not null," +
                "WriterID text not null," +
                "Content text not null," +
                "WriteDate text not null," +
                "ImgName text not null);";
        database.execSQL(sql);
    }

    // db - check if table exists
    private boolean isTableExist() {
        String searchTable = "select DISTINCT tbl_name from " +
                "sqlite_master where tbl_name = '" + TABLE_NAME + "';";
        Cursor cursor = database.rawQuery(searchTable, null);

        if (cursor.getCount() == 0) {
            cursor.close();
            return false;
        }

        cursor.close();
        return true;
    }

    // provider - db tasks when first instantiated
    @Override
    public boolean onCreate() {
        // application context 기억
        this.context = getContext();
        // database 생성
        SQLiteInitialize();
        // table 존재유무 판단 후 테이블 생성
        // 근데 이미 "CREATE TABLE IF NOT EXISTS" 아닌가?
        // 저 작업을 또 해줘야 되나?
        if (!isTableExist()) {
            createTable();
        }

        return true; // return true if initialization is successful
    }

    // provider - provide retrieve
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {

        switch (URI_MATCHER.match(uri)) {
            case ARTICLE_LIST:
                if (TextUtils.isEmpty(sortOrder)) {
                    sortOrder = NextagramContract.Article.SORT_ORDER_DEFAULT;
                }
                break;
            case ARTICLE_ID:
                if (TextUtils.isEmpty(sortOrder)) {
                    sortOrder = NextagramContract.Article.SORT_ORDER_DEFAULT;
                }
                if (selection == null) {
                    selection = "_id = ?"; // 여기도 db 칼럼 직접 쿼리하는 부분
                    selectionArgs = new String[]{uri.getLastPathSegment()};
                }
                break;
            default:
                throw new IllegalArgumentException("Unsupported URI: " + uri);
        }

        // db에 쿼리를 날려서 cursor를 가져옴
        Cursor cursor = database.query(TABLE_NAME,
                NextagramContract.Article.PROJECTION_ALL, selection,
                selectionArgs, null, null, sortOrder);

        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        // 커서가 해당 uri의 변경 여부를 감시하고 있다가
        // 변경이 일어나면 여기서 넣어준 content resolver에 등록된 리스너(content observer)들에게 알려주게 된다
        // cursorAdapter도 watcher들 중 하나
        // cursorAdapter의 경우 이 이벤트가 발생하면 해당 내용을 content resolver에 다시 요청한다

        return cursor;
    }

    // provider - provide insert
    @Override
    public Uri insert(Uri uri, ContentValues values) {

        // URI에 대한 유효성 검사
        if (URI_MATCHER.match(uri) != ARTICLE_LIST) {
            throw new IllegalArgumentException("Insertion을 지원하지 않는 URI입니다 : " + uri);
        }

        // ARTICLE_LIST에 대한 URI 요청이 들어왔을 때
        if (URI_MATCHER.match(uri) == ARTICLE_LIST) {
            // 데이터베이스에 insert를 하고 몇번째 row인지 리턴 받음
            long id = database.insert("Article", null, values);

            Uri itemUri = ContentUris.withAppendedId(uri, id);
            // "content://com.example.nextagram.provider/Article/id" 이런 형식으로 되겠지?
            getContext().getContentResolver().notifyChange(itemUri, null);
            // content resolver에 등록된 리스너들에게 알려줌
            // 디폴트로 cursorAdapter는 이 알림을 받게 된다
            // 누군가가 insert나 update나 delete를 통해 데이터를 변경했을 시
            // provider는 변경되었다고 resolver에게 알려줌!

            return itemUri;
        }

        return null;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }

    @Override
    public String getType(Uri uri) {
        return null;
    }
}
