package com.example.assignmenta2;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class PointScreen extends AppCompatActivity implements View.OnClickListener {
    Category category;
    TextView tv_title, earnings;
    TextView button1, button2, button3, button4, button5;
    int clickedButton = -1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.point_screen);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setTitle("Selection");
        earnings = findViewById(R.id.earnings);

        category = (Category) getIntent().getSerializableExtra("category");
        tv_title = findViewById(R.id.tv_title);
        String upperString = category.title.substring(0,1).toUpperCase() + category.title.substring(1);
        tv_title.setText(upperString);
        button1 = findViewById(R.id.btn_100);
        button2 = findViewById(R.id.btn_200);
        button3 = findViewById(R.id.btn_300);
        button4 = findViewById(R.id.btn_400);
        button5 = findViewById(R.id.btn_500);
        button1.setOnClickListener(this);
        button2.setOnClickListener(this);
        button3.setOnClickListener(this);
        button4.setOnClickListener(this);
        button5.setOnClickListener(this);
    }

    //sets the point buttons to gray and unclickable once certain point button is selected
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_100:
                if (clickedButton == 1) {
                    return;
                }
                clickedButton = 1;
                button1.setBackgroundResource(R.drawable.gray_corners);
                startActivity(new Intent(PointScreen.this, QuestionAnswerActivity.class).putExtra("categoryID", "" + category.id).putExtra("value", "100"));
                break;
            case R.id.btn_200:
                if (clickedButton == 2) {
                    return;
                }
                clickedButton = 2;
                button2.setBackgroundResource(R.drawable.gray_corners);
                startActivity(new Intent(PointScreen.this, QuestionAnswerActivity.class).putExtra("categoryID", "" + category.id).putExtra("value", "200"));
                break;
            case R.id.btn_300:
                if (clickedButton == 3) {
                    return;
                }
                clickedButton = 3;
                button3.setBackgroundResource(R.drawable.gray_corners);
                startActivity(new Intent(PointScreen.this, QuestionAnswerActivity.class).putExtra("categoryID", "" + category.id).putExtra("value", "300"));
                break;
            case R.id.btn_400:
                if (clickedButton == 4) {
                    return;
                }
                clickedButton = 4;
                button4.setBackgroundResource(R.drawable.gray_corners);
                startActivity(new Intent(PointScreen.this, QuestionAnswerActivity.class).putExtra("categoryID", "" + category.id).putExtra("value", "400"));
                break;
            case R.id.btn_500:
                if (clickedButton == 5) {
                    return;
                }
                clickedButton = 5;
                button5.setBackgroundResource(R.drawable.gray_corners);
                startActivity(new Intent(PointScreen.this, QuestionAnswerActivity.class).putExtra("categoryID", "" + category.id).putExtra("value", "500"));
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences prefs = getSharedPreferences("prefs", MODE_PRIVATE);
        earnings.setText("$" + prefs.getLong("earnings", 0));
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return true;
    }
}
