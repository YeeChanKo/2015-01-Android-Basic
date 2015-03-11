package com.example.viz.week02_listview;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by viz on 2015. 3. 11..
 */
public class CustomAdapter extends ArrayAdapter<ListData> {
    private Context context;
    private int layoutResourceId;
    private ArrayList<ListData> listData;

    public CustomAdapter(Context context, int layoutResourceId, ArrayList<ListData> listData) {
        super(context, layoutResourceId, listData);
        this.context = context;
        this.layoutResourceId = layoutResourceId;
        this.listData = listData;
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
        TextView textView1 = (TextView) row.findViewById(R.id.customlist_textview1);
        TextView textView2 = (TextView) row.findViewById(R.id.customlist_textview2);
        textView1.setText(listData.get(position).getText1());
        textView2.setText(listData.get(position).getText2());

        // 해당 row의 이미지 넣어주기
        ImageView imageView = (ImageView) row.findViewById(R.id.customlist_imageview);
        try {
            InputStream is = context.getAssets().open(listData.get(position).getImgName());
            Drawable d = Drawable.createFromStream(is, null);
            imageView.setImageDrawable(d);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return row;
    }
}
