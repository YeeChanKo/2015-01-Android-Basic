package com.example.viz.nextagram;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.io.InputStream;


public class ViewerActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewer);

        TextView tvTitle = (TextView) findViewById(R.id.viewer_title);
        TextView tvWriter = (TextView) findViewById(R.id.viewer_writer);
        TextView tvContent = (TextView) findViewById(R.id.viewer_content);
        TextView tvWriteTime = (TextView) findViewById(R.id.viewer_write_time);
        ImageView ivImage = (ImageView) findViewById(R.id.viewer_image_view);

        String articleNumber = getIntent().getExtras().getString("ArticleNumber");

        Dao dao = new Dao(getApplicationContext());
        Article article = dao.getArticleByArticleNumber(Integer.parseInt(articleNumber));

        tvTitle.setText(article.getTitle());
        tvWriter.setText(article.getWriter());
        tvContent.setText(article.getContent());
        tvWriteTime.setText(article.getWriteDate());

        try {
            // TODO: 그림이랑 내용 띄워주는 부분 수정, DB에서 가져오게
            //            InputStream is = getApplicationContext().getAssets().open(article.getImgName());
            //            Drawable d = Drawable.createFromStream(is, null);
            //            ivImage.setImageDrawable(d);

            String imgPath = getFilesDir().getPath() + "/" + article.getImgName();
            File imgLoadPath = new File(imgPath);

            if (imgLoadPath.exists()) {
                Bitmap bitmap = BitmapFactory.decodeFile(imgPath);
                ivImage.setImageBitmap(bitmap);
            } else {
                Log.e("viewImage", "fail: file not found");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_viewer, menu);
        return true;
    }
}
