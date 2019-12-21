package com.app.test;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.RadioGroup;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

public class TwoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_two);
        RadioGroup radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        ActionBar actionBar =getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        setTitle("Аннотация");
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
        Intent intent;

        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
                        intent = new Intent(TwoActivity.this, TestActivity.class);
                        startActivity(intent);
                        finish();

                }

        });


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
