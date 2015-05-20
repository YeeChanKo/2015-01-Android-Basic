package com.example.viz.week04_content_provider;

import android.app.Activity;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;


public class MainActivity extends Activity {

    private static final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        printLogContactData(getContactCursor());
    }

    private Cursor getContactCursor() {
        // 사용자 연락처 Provider에 접근하는 URI.
        // 연락처 정보 테이블의 위치
        Uri contactURI = ContactsContract.Contacts.CONTENT_URI;

        // 사용자 연락처 Provider에서 얻고 싶은 데이터의 종류를 String[]에 담아둠
        String[] projection = new String[]{
                ContactsContract.Contacts._ID,
                ContactsContract.Contacts.DISPLAY_NAME
        };

        Cursor contactCursor = getContentResolver().query(contactURI, projection, null, null, ContactsContract.Contacts.DISPLAY_NAME + " asc");

        if (contactCursor == null) {
            Log.e(TAG, "연결 실패");
            return null;
        }

        return contactCursor;
    }

    private void printLogContactData(Cursor contactData) {
        int idIndex;
        int displayNameIndex;

        if (contactData == null) {
            Log.e(TAG, "연결 실패");
            return;
        } else if (contactData.getCount() < 1) {
            // 결과값 개수가 0개일 때 처리
            Log.e(TAG, "매치되는 Provider가 없음");
        } else {
            idIndex = contactData.getColumnIndex(ContactsContract.Contacts._ID);
            displayNameIndex = contactData.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME);
            while (contactData.moveToNext()) {
                Log.i(TAG, "ID : " + contactData.getLong(idIndex) + ", NAME : " + contactData.getString(displayNameIndex));
            }
        }
    }
}
