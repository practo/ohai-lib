package com.practo.ohai.app;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.practo.ohai.Ohai;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Ohai.getInstance(this).setEmail("twntee@gmail.com").setName("Rachit").start();
    }
}
