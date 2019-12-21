package com.app.test;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.IOException;


public class ScalsActivity extends AppCompatActivity implements View.OnClickListener{
TextView textView;

    private ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scals);
        textView = (TextView) findViewById(R.id.textView1);
        ActionBar actionBar =getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        new MyAsyncTask().execute();
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
        this.finish();
    }
    class MyAsyncTask extends AsyncTask<String, String, String> {
        Intent intent;
        String answerHTTP;
        String server ="http://192.168.43.78/index8.php";

        @Override
        protected void onPreExecute() {
            dialog = new ProgressDialog(ScalsActivity.this);
            dialog.setMessage("Загружаюсь...");
            dialog.setIndeterminate(true);
            dialog.setCancelable(true);
            dialog.show();
           super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            HttpClient httpclient = new DefaultHttpClient();

            HttpGet httpget = new HttpGet(server);

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

if (answerHTTP==null){
    intent = new Intent(ScalsActivity.this, MainActivity.class);

finish();
    startActivity(intent);
}

        }
    }
}
