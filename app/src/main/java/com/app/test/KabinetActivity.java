package com.app.test;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.service.autofill.TextValueSanitizer;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import android.widget.AdapterView.OnItemSelectedListener;

import org.apache.http.NameValuePair;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class KabinetActivity extends AppCompatActivity {

    String[] data = {"мужской", "женский"};
    private static final String MY_SETTINGS = "my_settings";
    public EditText login;
    public EditText pass;
    public EditText pass2;
    public EditText email;
    public TextView a1;
    public TextView a2;
    public TextView a3;
    Button btn;
    private ProgressDialog dialog;
    private InputStream is;
    TestActivity url;
    SharedPreferences sp;
    String login2;
    String password2;
    String email2;
    String gender2;
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kabinet);
        ActionBar actionBar =getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        // адаптер
        setTitle("Регистрация");
        sp = getSharedPreferences(MY_SETTINGS,
                Context.MODE_PRIVATE);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, data);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        Spinner spinner = (Spinner) findViewById(R.id.spinner);
        spinner.setAdapter(adapter);
        // заголовок
        spinner.setPrompt("Title");
        // выделяем элемент

        spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                // показываем позиция нажатого элемента
                if (position==0) gender2="m";
                else  gender2="g";
                Log.d("Tag",Integer.toString(position));
            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                gender2="m";
            }
        });
          btn = (Button) findViewById(R.id.button6);
        login = (EditText) findViewById(R.id.editText1);
        pass = (EditText) findViewById(R.id.editText3);
        pass2 = (EditText) findViewById(R.id.editText4);
        email = (EditText) findViewById(R.id.editText);
        a1 = (TextView) findViewById(R.id.textView12);
        a2 = (TextView) findViewById(R.id.textView13);
        a3 = (TextView) findViewById(R.id.textView14);
        btn.setOnClickListener(new android.view.View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //тут указываем куда будем конектится, для примера я привел удаленных хост если у вас не получилось освоить wamp (:

                 if (pass.getText().toString().length()<8)


                     a2. setText("Пароль должен содержать 8 и более символов");

                else
                if (!(pass.getText().toString().equals(pass2.getText().toString())))

                    a2. setText("Пароли не совпадают");

                else a2. setText("");
               if  (!(email.getText().toString().matches("^([a-z0-9_-]+\\.)*[a-z0-9_-]+@[a-z0-9_-]+(\\.[a-z0-9_-]+)*\\.[a-z]{2,6}$")))

                   a3.setText("Почта указана не корректно");

               else a3. setText("");
               if ((a2. getText().toString()=="" )&&(a3. getText().toString()=="" ))
               {
                   btn.setEnabled(false);
                   new RequestTask().execute("http://192.168.43.78/create_user.php");

               }
                if (login.getText().toString().length()==0)

                    a1. setText("Логин пустой");

                else a1. setText("");
            }
        });

        // устанавливаем обработчик нажатия

    }
    class RequestTask extends AsyncTask<String, String, String> {
        String a="";
        String b="";
        String c="";
        @Override
        protected String doInBackground(String... params) {

            try {
                //создаем запрос на сервер
                DefaultHttpClient hc = new DefaultHttpClient();
                ResponseHandler<String> res = new BasicResponseHandler();
                //он у нас будет посылать post запрос
                HttpPost postMethod = new HttpPost(params[0]);
                //будем передавать два параметра
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(5);
                //передаем параметры из наших текстбоксов
                //лоигн
                nameValuePairs.add(new BasicNameValuePair("login", login2));
                //пароль
                nameValuePairs.add(new BasicNameValuePair("password", password2));
                nameValuePairs.add(new BasicNameValuePair("email", email2));
                //пароль
                nameValuePairs.add(new BasicNameValuePair("gender", gender2));
                nameValuePairs.add(new BasicNameValuePair("role", "0"));
                //собераем их вместе и посылаем на сервер
                postMethod.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                //получаем ответ от сервера
                String response = hc.execute(postMethod, res);
                //посылаем на вторую активность полученные параметры

                Log.e("log_tagsw", "ok " +JSONURL(response));



            } catch (Exception e) {
                System.out.println("Exp=" + e);
                login2="";
            }
            return null;
        }
        public String JSONURL(String result) {

            try {
                //создали читателя json объектов и отдали ему строку - result
                JSONObject json = new JSONObject(result);
                //дальше находим вход в наш json им является ключевое слово data
                a  = json.getString("login");
                b  = json.getString("password");
                c=json.getString("id");
                Log.e("log_tags", "oks " +a);
                //проходим циклом по всем нашим параметрам
                //=joShow.getString("message");
                //читаем что в себе хранит параметр lastname


            }
            catch (JSONException e) {
                Log.e("log_tag", "Error parsing data " + e.toString());
                login2="";
            }
            return a;
        }
        @Override
        protected void onPostExecute(String result) {
            if (login2=="") {
                Toast toast = Toast.makeText(getApplicationContext(),
                        "Логин уже существует", Toast.LENGTH_SHORT);
                toast.show();
                btn.setEnabled(true);
            }
            else
            {
           Intent intent = new Intent(KabinetActivity.this,MyTestsActivity.class);
                SharedPreferences.Editor e = sp.edit();
                e.putString("id",c);
                e.putString("userName", a);
                e.putString("password", b);
                e.putString("role","0");
                 e.commit();
           startActivity(intent);
            finish();
            }
            dialog.dismiss();

        //btn.setEnabled(true);
            super.onPostExecute(result);
        }

        @Override
        protected void onPreExecute() {
            login2=login.getText().toString();
            password2=pass.getText().toString();
            email2=email.getText().toString();
            Log.d("Tag",login2);
            dialog = new ProgressDialog(KabinetActivity.this);
            dialog.setMessage("Загрузка...");
            dialog.setIndeterminate(true);
            dialog.setCancelable(true);
            dialog.show();
            super.onPreExecute();
        }
    }
}
