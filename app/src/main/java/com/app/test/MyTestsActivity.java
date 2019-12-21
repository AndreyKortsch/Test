package com.app.test;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;

import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MyTestsActivity extends AppCompatActivity {
    SharedPreferences mSettings;
    TextView textView1;
    private static final String MY_SETTINGS = "my_settings";
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    loadFragment(NotificationsFragment.newInstance());
                    return true;
                case R.id.navigation_dashboard:
                    loadFragment(DashboardFragment.newInstance());
                    return true;
                case R.id.navigation_notifications:
                    loadFragment(HomeFragment.newInstance());
                    return true;
            }
            return false;
        }
    };

    private void loadFragment(Fragment fragment) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fl_content, fragment);
        ft.commit();
    }
    @Override
    protected void onResume(){
        super.onResume();
        loadFragment(NotificationsFragment.newInstance());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_tests);
        ActionBar actionBar =getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        loadFragment(NotificationsFragment.newInstance());
       // textView1 = (TextView) findViewById(R.id.textView1);
        mSettings = getSharedPreferences(MY_SETTINGS,
                Context.MODE_PRIVATE);
        if(mSettings.contains("password")) {
          //  textView1.setText(mSettings.getString("password", ""));
        }

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}


