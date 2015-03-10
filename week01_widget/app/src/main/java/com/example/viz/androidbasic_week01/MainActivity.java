package com.example.viz.androidbasic_week01;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends Activity implements View.OnClickListener {

    private TextView textView1;
    private EditText editText1;
    private ImageView imageView1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button b = (Button) findViewById(R.id.button);
        ImageButton ib = (ImageButton) findViewById(R.id.imageButton);

        b.setOnClickListener(this);
        ib.setOnClickListener(this);

        textView1 = (TextView) findViewById(R.id.textView);
        editText1 = (EditText) findViewById(R.id.editText);
        imageView1 = (ImageView) findViewById(R.id.imageView);
    }

    @Override
    public void onClick(View v) {
        textView1.setText("입력하신 내용은 " + editText1.getText().toString() + "입니다.");

        switch (v.getId()) {
            case R.id.button:
                Toast.makeText(getApplicationContext(), "버튼을 터치하셨습니다.", Toast.LENGTH_SHORT).show();
                imageView1.setImageResource(R.drawable.gyunbin);
                break;
            case R.id.imageButton:
                Toast.makeText(getApplicationContext(), "이미지 버튼을 터치하셨습니다.", Toast.LENGTH_SHORT).show();
                imageView1.setImageResource(R.drawable.singer);
                break;
        }

    }
}
