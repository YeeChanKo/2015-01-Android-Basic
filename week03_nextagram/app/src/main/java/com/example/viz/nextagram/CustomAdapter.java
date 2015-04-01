package com.example.viz.nextagram;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class CustomAdapter extends ArrayAdapter<Article> {
    private Context context;
    private int layoutResourceId;
    private ArrayList<Article> articleData;

    public CustomAdapter(Context context, int layoutResourceId, ArrayList<Article> articleData) {
        super(context, layoutResourceId, articleData);
        this.context = context;
        this.layoutResourceId = layoutResourceId;
        this.articleData = articleData;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;

        // 해당 row의 레이아웃 그려주기
        if (row == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);
        }

        // 해당 row의 텍스트들 넣어주기
        TextView tvTitle = (TextView) row.findViewById(R.id.customlist_textview1);
        TextView tvContent = (TextView) row.findViewById(R.id.customlist_textview2);
        tvTitle.setText(articleData.get(position).getTitle());
        tvContent.setText(articleData.get(position).getContent());

        // 해당 row의 이미지 넣어주기
        ImageView imageView = (ImageView) row.findViewById(R.id.customlist_imageview);

        String imgPath = context.getFilesDir().getPath() + "/" + articleData.get(position).getImgName();
        File imgLoadPath = new File(imgPath);

        if (imgLoadPath.exists()) {
            Bitmap bitmap = BitmapFactory.decodeFile(imgPath);
            imageView.setImageBitmap(bitmap);
        }

        // TODO: 없을 때 else 처리해줘야 함

//        try {
//            InputStream is = context.getAssets().open(articleData.get(position).getImgName());
//            Drawable d = Drawable.createFromStream(is, null);
//            imageView.setImageDrawable(d);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

        return row;
    }
}
