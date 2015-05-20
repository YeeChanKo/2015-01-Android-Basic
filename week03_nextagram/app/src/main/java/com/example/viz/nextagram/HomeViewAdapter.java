package com.example.viz.nextagram;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.lang.ref.WeakReference;

public class HomeViewAdapter extends CursorAdapter {

    // TODO: CursorAdapter? vs ArrayAdapter? what else?

    private Context mContext;
    private Cursor mCursor;
    private int mLayoutResourceId;
    private LayoutInflater mLayoutInflater;

    static class ViewHolderItem {
        ImageView imageView;
        TextView tvWriter;
        TextView tvTitle;
        int articleNumber;
    }

    public HomeViewAdapter(Context context, Cursor cursor, int layoutResourceId) {
        super(context, cursor, layoutResourceId);

        this.mContext = context;
        this.mCursor = cursor;
        this.mLayoutResourceId = layoutResourceId;

        mLayoutInflater = LayoutInflater.from(mContext);
    }

    // 매번 어댑터 갱신할때마다 findViewById를 사용하면 성능이 저한된다!
    // ViewHolder Pattern 사용할 것! -> View 객체 재사용 -> need further study
    // 처음 view가 생성될 때 실행
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        // xml 파일로부터 inflate해서 view를 만들어줌
        View row = mLayoutInflater.inflate(mLayoutResourceId, parent, false);

        ViewHolderItem viewHolder = new ViewHolderItem();

        viewHolder.imageView = (ImageView) row.findViewById(R.id.customlist_imageview);
        viewHolder.tvTitle = (TextView) row.findViewById(R.id.customlist_textview1);
        viewHolder.tvWriter = (TextView) row.findViewById(R.id.customlist_textview2);

        row.setTag(viewHolder); // 객체를 row에 태그로 넣어줌

        return row;
    }

    // 생성된 뷰에 텍스트와 이미지를 넣어줌
    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        // 커서에서 내용 가져와 텍스트뷰 설정
        String writerName = cursor.getString(
                cursor.getColumnIndex(NextagramContract.Article.WRITER_NAME));
        String title = cursor.getString(
                cursor.getColumnIndex(NextagramContract.Article.TITLE));
        String imgName = cursor.getString(
                cursor.getColumnIndex(NextagramContract.Article.IMG_NAME));
        String articleNumber = cursor.getString(
                cursor.getColumnIndex(NextagramContract.Article.ARTICLE_NUMBER));

        ViewHolderItem viewHolder = (ViewHolderItem) view.getTag();
        viewHolder.articleNumber = Integer.parseInt(articleNumber);
        viewHolder.tvWriter.setText(writerName);
        viewHolder.tvTitle.setText(title);

        // 이미지 경로
        String imgPath = context.getFilesDir().getPath() + "/" + imgName;

        // 뷰홀더의 이미지뷰를 약 레퍼런스 타입의 객체로 만들어서 쉽게 쉽게 GC 되게 만든다
        WeakReference<ImageView> imageViewReference = new WeakReference<ImageView>(viewHolder.imageView);

        // 이미지로더 사용해 비트맵 가져오기
        Bitmap bitmap = ImageLoader.getInstance().get(imgPath);
        if (bitmap != null) {
            Log.i("ImageLoader", "getCache");
            imageViewReference.get().setImageBitmap(bitmap);
        } else {
            Log.i("ImageLoader", "putCache");
            File imgLoadPath = new File(imgPath);

            if (imgLoadPath.exists()) {
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inSampleSize = 4; // 이미지 크기 설정
                options.inPurgeable = true;

                bitmap = BitmapFactory.decodeFile(imgPath, options);
                ImageLoader.getInstance().put(imgPath, bitmap);

                // Bitmap resized = Bitmap.createScaledBitmap(bitmap, 1000, 1000, true);
                imageViewReference.get().setImageBitmap(bitmap);
            } else {
                Log.e(HomeViewAdapter.class.getSimpleName(), "file does not exist!");
            }
        }
    }
}
