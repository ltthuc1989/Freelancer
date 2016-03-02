package com.ltthuc.freelancer;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import com.ltthuc.freelancer.Fragment.PlaceHolderFragment;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getName();

    FragmentManager fragmentManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        fragmentManager = getSupportFragmentManager();
        PlaceHolderFragment placeHolderFragment = new PlaceHolderFragment();
        fragmentManager.beginTransaction().replace(R.id.frame_main,placeHolderFragment).commit();




    }

    @Override
    protected void onDestroy() {
        super.onDestroy();


    }




}
