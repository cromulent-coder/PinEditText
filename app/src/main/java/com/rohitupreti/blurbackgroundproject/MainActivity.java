package com.rohitupreti.blurbackgroundproject;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ViewGroup;

import jp.wasabeef.blurry.Blurry;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.background_app);
        getWindow().getDecorView().setBackground(new BitmapDrawable(getResources(), BlurBuilder.blur(this, bitmap)));
        bitmap.recycle();

        

    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        /*ConstraintLayout constraintLayout = findViewById(R.id.viewGroup);

        Blurry.with(this).
                radius(25).
                sampling(2).
                async().
                onto(constraintLayout);*/
    }
}
