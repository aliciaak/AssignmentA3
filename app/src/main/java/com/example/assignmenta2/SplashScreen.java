package com.example.assignmenta2;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import android.content.Intent;
import android.widget.ImageView;

import com.felipecsl.gifimageview.library.GifImageView;
import org.apache.commons.io.IOUtils;

import android.os.Handler;

import java.io.IOException;
import java.io.InputStream;

public class SplashScreen extends AppCompatActivity {
    private GifImageView gif;
    private ImageView lightbulb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);

        gif = findViewById(R.id.gif);
        lightbulb = findViewById(R.id.lightbulb);

        try {
            InputStream inputStream = getAssets().open("thinktank.gif");
            byte[] bytes = IOUtils.toByteArray(inputStream);
            gif.setBytes(bytes);
            gif.startAnimation();
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        //wait 3 seconds and start MainActivity
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run()
            {
                SplashScreen.this.startActivity(new Intent(SplashScreen.this, HomeActivity.class));
                SplashScreen.this.finish();
            }
        }, 3500); //3000 = 3 seconds
    }
}