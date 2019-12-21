package com.app.test;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;


import org.apache.http.NameValuePair;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {
    private static final String MY_SETTINGS = "my_settings";
    public EditText login;
    public EditText pass;
    private ProgressDialog dialog;
    private InputStream is;
    TestActivity url;
    SharedPreferences sp;
    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        ImageView imageView = new ImageView(this);
        imageView.setImageResource(R.drawable.er);
        setContentView(R.layout.activity_login);
        Button btn = (Button) findViewById(R.id.button1);
        Button btn2 = (Button) findViewById(R.id.button2);
        Button btn3 = (Button) findViewById(R.id.button5);
        login = (EditText) findViewById(R.id.editText1);
        pass = (EditText) findViewById(R.id.editText2);
        sp = getSharedPreferences(MY_SETTINGS,
                Context.MODE_PRIVATE);
        btn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                //тут указываем куда будем конектится, для примера я привел удаленных хост если у вас не получилось освоить wamp (:
                new RequestTask().execute("http://192.168.43.78/login.php");

            }
        });
        btn2.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                //тут указываем куда будем конектится, для примера я привел удаленных хост если у вас не получилось освоить wamp (:
                Intent intent = new Intent(LoginActivity.this, TwoActivity.class);

                startActivity(intent);

            }
        });
        btn3.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                //тут указываем куда будем конектится, для примера я привел удаленных хост если у вас не получилось освоить wamp (:
                Intent intent = new Intent(LoginActivity.this, KabinetActivity.class);

                startActivity(intent);

            }
        });
    }

    class RequestTask extends AsyncTask<String, String, String> {
        String login2;
        String password2;
        String a="";
        String ap="";
        String ap2="";
        String ap3="";
        String ap4="";
        String ad;
        String response;
        @Override
        protected String doInBackground(String... params) {

            try {
                //создаем запрос на сервер
                DefaultHttpClient hc = new DefaultHttpClient();
                ResponseHandler<String> res = new BasicResponseHandler();
                //он у нас будет посылать post запрос
                HttpPost postMethod = new HttpPost(params[0]);
                //будем передавать два параметра
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(3);
                ad=UUID.randomUUID().toString();
                //передаем параметры из наших текстбоксов
                nameValuePairs.add(new BasicNameValuePair("uuid", ad));
                //лоигн
                nameValuePairs.add(new BasicNameValuePair("login", login2));
                //пароль
                nameValuePairs.add(new BasicNameValuePair("password", password2));
                //собераем их вместе и посылаем на сервер
                postMethod.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                //получаем ответ от сервера
                response = hc.execute(postMethod, res);
                DefaultHttpClient hc2 = new DefaultHttpClient();
                ResponseHandler<String> res2 = new BasicResponseHandler();
                HttpPost postMethod2 = new HttpPost("http://192.168.43.78/validate_token.php");
                List<NameValuePair> nameValuePairs2 = new ArrayList<NameValuePair>(2);
                //передаем параметры из наших текстбоксов
                nameValuePairs2.add(new BasicNameValuePair("uuid",ad));
                //лоигн
                nameValuePairs2.add(new BasicNameValuePair("jwt", JSONURL(response)));
                //пароль
                //собераем их вместе и посылаем на сервер
                postMethod2.setEntity(new UrlEncodedFormEntity(nameValuePairs2));
                //получаем ответ от сервера
                String response2 = hc2.execute(postMethod2, res2);
                //посылаем на вторую активность полученные параметры
                //Intent intent = new Intent(LoginActivity.this,MyTestsActivity.class);
                Log.e("log_tag", "okq" +UUID.randomUUID().toString());
                Log.e("log_tag", "okq2" +JSONURL2(response2));
                //startActivity(intent);
            } catch (Exception e) {

                System.out.println("Exp=" + e);
                login2="";
            }
            return null;
        }
        public String JSONURL2(String result) {

            try {
                //создали читателя json объектов и отдали ему строку - result
                JSONObject json = new JSONObject(result);
                //дальше находим вход в наш json им является ключевое слово data
                JSONObject phoneNumbersArr  = (JSONObject) json.get("data");
                ap= phoneNumbersArr.getString("id");
                ap2= phoneNumbersArr.getString("gender");
                ap3= phoneNumbersArr.getString("email");
                ap4= phoneNumbersArr.getString("role");
                Log.e("log_tag", "okq" +ap);
                //проходим циклом по всем нашим параметрам
                //=joShow.getString("message");
                //читаем что в себе хранит параметр lastname


            }
            catch (JSONException e) {
                Log.e("log_tag", "Error parsing data " + e.toString());
            }
            return a;
        }
        public String JSONURL(String result) {

            try {
                //создали читателя json объектов и отдали ему строку - result
                JSONObject json = new JSONObject(result);
                //дальше находим вход в наш json им является ключевое слово data
                a  = json.getString("jwt");
                //проходим циклом по всем нашим параметрам
                 //=joShow.getString("message");
                    //читаем что в себе хранит параметр lastname


                }
            catch (JSONException e) {
                Log.e("log_tag", "Error parsing data " + e.toString());
            }
            return a;
        }
        @Override
        protected void onPostExecute(String result) {
            if (login2=="") {
                Toast toast = Toast.makeText(getApplicationContext(),
                        "Неверный логин или пароль!", Toast.LENGTH_SHORT);
                toast.show();
            }
            else
            {
                EditText userNameLogin = (EditText) findViewById(R.id.editText1);
                EditText Password = (EditText) findViewById(R.id.editText2);
                String userName = userNameLogin.getText().toString();
                String password = Password.getText().toString();
                SharedPreferences.Editor e = sp.edit();
                Intent intent;
                Log.e("log_tag", "okааq " +ap4);
                e.putString("role", ap4);
                if (ap4.equals("1"))
                    intent = new Intent(LoginActivity.this,AdminActivity.class);
                    else
                        {
                    e.putString("id", ap);
                    e.putString("gender", ap2);
                    e.putString("email", ap3);


                    e.putString("userName", userName);
                    e.putString("password", password);
                    e.putString("uuid", ad);
                    e.putString("jwt", JSONURL(response));

                intent = new Intent(LoginActivity.this,MyTestsActivity.class);
                    }
                e.commit();
                startActivity(intent);
            }
            dialog.dismiss();
            super.onPostExecute(result);
        }

        @Override
        protected void onPreExecute() {
            login2=login.getText().toString();
            password2=pass.getText().toString();
            Log.d("Tag",login2);
            dialog = new ProgressDialog(LoginActivity.this);
            dialog.setMessage("Загрузка...");
            dialog.setIndeterminate(true);
            dialog.setCancelable(true);
            dialog.show();
            super.onPreExecute();
        }
    }
}
