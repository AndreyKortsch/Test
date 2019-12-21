package com.app.test;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import java.io.IOException;


public class TestActivity extends AppCompatActivity implements View.OnClickListener {
    private ProgressBar pbHorizontal;
    private int progress = 0;
    TextView textView;
    TextView textView2;
    Button button;
    Button button2;
    Intent intent;
    private int counter = 1;
    private int[] answer = new int[100];
    //Переменная для работы с БД
    private DataBaseHelper mDBHelper;
    private ProgressDialog dialog;
    private SQLiteDatabase mDb;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_test);
        Chronometer chronometer = (Chronometer) findViewById(R.id.chronometer);
        long startTime = SystemClock.elapsedRealtime();
        chronometer.setBase(startTime);
        chronometer.start();
        pbHorizontal = (ProgressBar) findViewById(R.id.progressBar);
        pbHorizontal.setVisibility(ProgressBar.VISIBLE);
        ActionBar actionBar =getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        mDBHelper = new DataBaseHelper(this);
        try {
            mDBHelper.updateDataBase();
        } catch (IOException mIOException) {
            throw new Error("UnableToUpdateDatabase");
        }

        try {
            mDb = mDBHelper.getWritableDatabase();
        } catch (SQLException mSQLException) {
            throw mSQLException;
        }
        for (int i=1;i<=71;i++) answer[i]=-1;
        button = (Button) findViewById(R.id.button3);
        button2 = (Button) findViewById(R.id.button4);
        textView = (TextView) findViewById(R.id.textView7);
        textView2 = (TextView) findViewById(R.id.textView5);
        button2.setEnabled(false);
        button2.setVisibility(View.INVISIBLE);
        new MyAsyncTask().execute();

    }
    class MyAsyncTask extends AsyncTask<String, String, String> {

        String a, b, answerHTTP;

        String server ="http://192.168.43.78/index5.php?a=";

        @Override
        protected void onPreExecute() {
            dialog = new ProgressDialog(TestActivity.this);
            dialog.setMessage("Загрузка...");
            dialog.setIndeterminate(true);
            dialog.setCancelable(false);
            dialog.show();
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            HttpClient httpclient = new DefaultHttpClient();

            HttpGet httpget = new HttpGet(server+counter);

            try {
                HttpResponse response = httpclient.execute(httpget);

                if (response.getStatusLine().getStatusCode() == 200) {
                    HttpEntity entity = response.getEntity();
                    answerHTTP = EntityUtils.toString(entity);
                }

            }
            catch (ClientProtocolException e) {

            }
            catch (IOException e) {

            }

            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            dialog.dismiss();
            super.onPostExecute(result);
            textView.setText(answerHTTP);
        }
    }
    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setTitle("Выйти из приложения?")
                .setMessage("Вы действительно хотите выйти?")
                .setNegativeButton(android.R.string.no, null)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
                        com.app.test.TestActivity.super.onBackPressed();
                    }
                }).create().show();
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

    @Override
    public void onClick(android.view.View v) {
        RadioGroup radioGroup =  findViewById(R.id.radioGroup2);
        RadioButton radioButton1 = findViewById(R.id.radioButton3);
        RadioButton radioButton2 =  findViewById(R.id.radioButton4);
     if (radioButton1.isChecked()|radioButton2.isChecked()|v.getId()==R.id.button4) {
                 String product = "";
                 switch (v.getId()) {
                     case R.id.button4:
                         if (counter > 1) counter--;
                         progress--;
                         break;
                     case R.id.button3:
                         if(radioButton1.isChecked())answer[counter]=1;
                         if(radioButton2.isChecked())answer[counter]=0;
                         radioGroup.clearCheck();
                         counter++;

                        break;
                 }
                 if (counter==71)
                 {
                     //button.setText("Завершить");
                     button.setBackgroundResource(R.drawable.qw);
                 }
                 else{
                     button.setText("");
                     button.setBackgroundResource(R.drawable.next1);
                 }
                 if (counter==72)
                 {
                     intent = new Intent(TestActivity.this, ResultActivity.class);
                     intent.putExtra ("answers", answer);
                     for (int i=1;i<72;i++){
                         Log.e("jnmm",Integer.toString(answer[i]));
                     }

                     startActivity(intent);
                     this.finish();
                 }
                 else {
                     textView2.setText("Вопрос " + counter + "/71");
                     switch (answer[counter]) {
                         case 1:
                             radioButton1.setChecked(true);
                             break;
                         case 0:
                             radioButton2.setChecked(true);
                             break;
                         default:
                             break;
                     }
                 }
                 if (counter == 1) {
                     button2.setEnabled(false);
                     button2.setVisibility(View.INVISIBLE);
                 } else {
                     button2.setVisibility(View.VISIBLE);
                     button2.setEnabled(true);
                 }


                 postProgress(counter);
                 new MyAsyncTask().execute();

             }
             else{
                 Toast toast = Toast.makeText(this,
                         "Выберите вариант ответа!", Toast.LENGTH_SHORT);
                 toast.show();
             }

        }



    private void postProgress(int progress) {
        String strProgress = String.valueOf(progress) + " %";
        pbHorizontal.setProgress(progress);
    }
}
