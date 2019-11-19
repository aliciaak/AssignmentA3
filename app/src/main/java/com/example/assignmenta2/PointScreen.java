package com.example.assignmenta2;

import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class PointScreen extends AppCompatActivity {
    private Category category;
    private TextView tv_title;
    private TextView btn_100, btn_200, btn_300, btn_400, btn_500;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.point_screen);
        category = (Category) getIntent().getSerializableExtra("category");
        tv_title = findViewById(R.id.tv_title);
        tv_title.setText(category.title);
        btn_100 = findViewById(R.id.btn_100);
        btn_200 = findViewById(R.id.btn_200);
        btn_300 = findViewById(R.id.btn_300);
        btn_400 = findViewById(R.id.btn_400);
        btn_500 = findViewById(R.id.btn_500);
    }
}
