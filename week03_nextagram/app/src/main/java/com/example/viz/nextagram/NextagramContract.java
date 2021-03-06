package com.example.viz.nextagram;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by viz on 15. 5. 12..
 */
public final class NextagramContract {

    // NEXTAGRAM Provider AUTHORITY
    public static final String AUTHORITY = "com.example.nextagram.provider";

    // 최상위 아이템의 AUTHORITY를 위한 CONTENT URI
    // content://com.example.nextagram.provider/
    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY);

    public static final class Article implements BaseColumns {
        public static final String ARTICLE_NUMBER = "_id"; // CursorAdapter 사용하려면 반드시 Cursor에 "_id" 칼럼이 있어야 한다고 한다...
        public static final String TITLE = "Title";
        public static final String WRITER_NAME = "WriterName";
        public static final String WRITER_ID = "WriterID";
        public static final String CONTENT = "Content";
        public static final String WRITE_DATE = "WriteDate";
        public static final String IMG_NAME = "ImgName";

        // content://com.example.nextagram.provider/Article
        public static final Uri CONTENT_URI = Uri.withAppendedPath(
                NextagramContract.CONTENT_URI, Article.class.getSimpleName()
        );

        public static final String[] PROJECTION_ALL = {
                ARTICLE_NUMBER, TITLE, WRITER_NAME, WRITER_ID, CONTENT, WRITE_DATE, IMG_NAME
        };

        public static final String SORT_ORDER_DEFAULT = ARTICLE_NUMBER + " ASC";

    }
}
