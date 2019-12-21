package com.app.test;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

public class ResultActivity extends AppCompatActivity implements View.OnClickListener{
    String ms = "";
    TextView textView;
    String a, b, answerHTTP;
    Intent intent;
    Button button;
    Button button2;
    private ProgressDialog dialog;

    float[] ad=new float[100];
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        Bundle arguments = getIntent().getExtras();
        button = (Button) findViewById(R.id.button);
        button2 = (Button) findViewById(R.id.button4);
        ActionBar actionBar =getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        if (arguments.getIntArray ("answers")==null)
                ms=arguments.getString ("answers");
        else{
        int[] name = (int[]) arguments.getIntArray ("answers");
            for (int ii=0;ii<name.length;ii++)
            {


                ms+=String.valueOf(name[ii]);
            }
        }
        textView = (TextView) findViewById(R.id.textView6);

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
    class MyAsyncTask extends AsyncTask<String, String, String> {


        String server ="http://192.168.43.78/index7.php?a=";

        @Override
        protected void onPreExecute() {
            dialog = new ProgressDialog(ResultActivity.this);
            dialog.setMessage("Загрузка...");
            dialog.setIndeterminate(true);
            dialog.setCancelable(true);
            dialog.show();
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            HttpClient httpclient = new DefaultHttpClient();

            HttpGet httpget = new HttpGet(server+ms);

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
    public void onClick(android.view.View v) {
        switch (v.getId()) {
            case R.id.button:
                intent = new Intent(ResultActivity.this, GraphActivity.class);
                int r=1;
                int j=1;
                int qw=0;
                String a="";
                String b="";
                while(r<answerHTTP.length()){
                    if ((r+4)>=answerHTTP.length())break;
                    if (answerHTTP.substring(r,r+4).equals("балл")){
                        r = r + 5;
                        a="";
                        while ((Character.toLowerCase((char)answerHTTP.charAt(r)) != Character.toLowerCase((char)' '))) {
                            if (r>answerHTTP.length()) break;
                            a=a+answerHTTP.charAt(r);
                            r++;

                        }
                        ad[j]=Float.parseFloat(a);
                        b=b+a;
                        j++;
                    }

                    r++;
                }
                intent.putExtra ("answers", ad);
                startActivity(intent);
                break;
            case R.id.button4:
                intent = new Intent(ResultActivity.this, ScalsActivity.class);
                startActivity(intent);
                break;

        }


    }

}