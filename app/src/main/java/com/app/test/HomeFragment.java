package com.app.test;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

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
import java.util.UUID;

import androidx.fragment.app.Fragment;

public class HomeFragment extends Fragment  {
    SharedPreferences mSettings;
    EditText editText;
    EditText editText3;
    EditText editText4;
    EditText editText2;
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
     public HomeFragment() {
    }

    public static HomeFragment newInstance() {
        return new HomeFragment();
    }
    String[] data = {"мужской", "женский"};

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView =
                inflater.inflate(R.layout.activity_kabinet, container, false);
        btn = (Button) rootView.findViewById(R.id.button6);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(rootView.getContext(), android.R.layout.simple_spinner_item, data);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        btn.setText("Сохранить изменения");
        Spinner spinner = (Spinner) rootView.findViewById(R.id.spinner);
        spinner.setAdapter(adapter);
        // заголовок
        spinner.setPrompt("Title");
        sp = getActivity().getSharedPreferences(MY_SETTINGS,
                Context.MODE_PRIVATE);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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
                Log.d("Tag23",gender2);
            }
        });
        editText = (EditText) rootView.findViewById(R.id.editText1);
        editText3 = (EditText) rootView.findViewById(R.id.editText3);
        editText4 = (EditText) rootView.findViewById(R.id.editText4);
        editText2 = (EditText) rootView.findViewById(R.id.editText);
        a1 = (TextView) rootView.findViewById(R.id.textView12);
        a2 = (TextView) rootView.findViewById(R.id.textView13);
        a3 = (TextView) rootView.findViewById(R.id.textView14);
        mSettings = getActivity().getSharedPreferences(MY_SETTINGS,
                Context.MODE_PRIVATE);
        if(mSettings.contains("userName")) {
            editText.setText(mSettings.getString("userName", ""));
        }
        if(mSettings.contains("password")) {
            editText3.setText(mSettings.getString("password", ""));
            editText4.setText(mSettings.getString("password", ""));
        }
        if(mSettings.contains("email")) {
            editText2.setText(mSettings.getString("email", ""));

        }

        if(mSettings.contains("gender")) {
            gender2=mSettings.getString("gender", "");
           if (mSettings.getString("gender", "")=="g") spinner.setSelection(1);
        else spinner.setSelection(0);

        }
        // выделяем элемент

            btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (editText3.getText().toString().length()<8)


                    a2. setText("Пароль должен содержать 8 и более символов");

                else
                if (!(editText3.getText().toString().equals(editText4.getText().toString())))

                    a2. setText("Пароли не совпадают");

                else a2. setText("");
                if  (!(editText2.getText().toString().matches("^([a-z0-9_-]+\\.)*[a-z0-9_-]+@[a-z0-9_-]+(\\.[a-z0-9_-]+)*\\.[a-z]{2,6}$")))

                    a3.setText("Почта указана не корректно");

                else a3. setText("");
                if ((a2. getText().toString()=="" )&&(a3. getText().toString()=="" ))
                {
                    btn.setEnabled(false);
                    new RequestTask().execute("http://192.168.43.78/update_user.php");

                }


            }
        });

        return  rootView;
    }
    class RequestTask extends AsyncTask<String, String, String> {
        String login2;
        String password2;
        String a="";
        String ap="";
        String ap2="";
        String ap3="";
        String ap4="";
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

                //передаем параметры из наших текстбоксов
                nameValuePairs.add(new BasicNameValuePair("uuid", a));
                //лоигн
                nameValuePairs.add(new BasicNameValuePair("jwt", ap));
                nameValuePairs.add(new BasicNameValuePair("login", login2));
                nameValuePairs.add(new BasicNameValuePair("password", password2));
                //пароль
                nameValuePairs.add(new BasicNameValuePair("email", email2));
                nameValuePairs.add(new BasicNameValuePair("gender", gender2));
                //собераем их вместе и посылаем на сервер
                postMethod.setEntity(new UrlEncodedFormEntity(nameValuePairs));

                //получаем ответ от сервера
                response = hc.execute(postMethod, res);

                //посылаем на вторую активность полученные параметры
                Intent intent = new Intent(getActivity(),MyTestsActivity.class);
                Log.e("log_tag", "okq" +response);
                     startActivity(intent);
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
                Toast toast = Toast.makeText(getActivity().getApplicationContext(),
                        "Логин уже существует", Toast.LENGTH_SHORT);
                toast.show();

            }
            else
            {

                Toast toast = Toast.makeText(getActivity().getApplicationContext(),
                        "Данные обновлены!", Toast.LENGTH_SHORT);
                toast.show();
                SharedPreferences.Editor e = sp.edit();
                e.putString("uuid", a);
                e.putString("jwt", JSONURL(response));
                e.putString("gender", gender2);
                e.putString("email", email2);
                e.putString("userName", login2);
                e.putString("password", password2);
                e.apply();
                Intent intent = getActivity().getIntent();
                getActivity().finish();
                 startActivity(intent);
            }
            btn.setEnabled(true);
            dialog.dismiss();
            super.onPostExecute(result);
        }

        @Override
        protected void onPreExecute() {
            if(mSettings.contains("uuid")) {
                a=mSettings.getString("uuid", "");

            }
            if(mSettings.contains("jwt")) {
                ap=mSettings.getString("jwt", "");

            }
            login2=editText.getText().toString();
            password2=editText3.getText().toString();
            email2=editText2.getText().toString();
            Log.d("Tagsa",login2);
            dialog = new ProgressDialog(getActivity());
            dialog.setMessage("Загрузка...");
            dialog.setIndeterminate(true);
            dialog.setCancelable(true);
            dialog.show();
            super.onPreExecute();
        }
    }
    }