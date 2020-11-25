package com.example.vaspe.umu;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class SplashServantActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        Intent intent = new Intent(this, ServantActivity.class);
        startActivity(intent);
        finish();
    }
}
