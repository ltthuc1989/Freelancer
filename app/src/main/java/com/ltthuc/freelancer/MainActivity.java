package com.ltthuc.freelancer;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.ltthuc.freelancer.Fragment.PlaceHolderFragment;
import com.ltthuc.freelancer.Fragment.RateFragment;

public class MainActivity extends AppCompatActivity implements RateFragment.RateListener {

    private static final String TAG = MainActivity.class.getName();

    FragmentManager fragmentManager;
    PlaceHolderFragment placeHolderFragment;
    RateFragment rateFragment;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().setTitle("");
        fragmentManager = getSupportFragmentManager();
        placeHolderFragment = new PlaceHolderFragment();
        fragmentManager.beginTransaction().replace(R.id.frame_main, placeHolderFragment).commit();


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_action, menu);

        return super.onCreateOptionsMenu(menu);
    }

    private void showRateDialog() {

        rateFragment = new RateFragment();
        rateFragment.show(fragmentManager, "Rate_Fragment");
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_rate:

                showRateDialog();
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return false;
    }

    @Override
    public void onRate() {
        placeHolderFragment.pausePlayBack();
    }

    @Override
    protected void onPause() {
        super.onPause();
        //  placeHolderFragment.pausePlayBack();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onStop() {
        super.onStop();

        if (rateFragment != null) {
            rateFragment.dismiss();
        }

    }
}
