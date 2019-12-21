package com.app.test;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.fragment.app.ListFragment;





public class UserFragment extends ListActivity {
    Intent intent;
    String login2;
    String a, b, answerHTTP;
    private ProgressDialog dialog;
    SharedPreferences mSettings;
    private ArrayAdapter<String> mAdapter;
    String server ="http://192.168.43.78/user_test.php?login="+a+"+&password="+b;
    public static UserFragment newInstance() {
        return new UserFragment();
    }
    // определяем массив типа String
    final List<String> catNames = new ArrayList<String>();
    final List<String> catNames2 = new ArrayList<String>();
    private static final String MY_SETTINGS = "my_settings";
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // setContentView(R.layout.fragment_home);
        setTitle("Ваши тесты");
        mSettings = getSharedPreferences(MY_SETTINGS,
                Context.MODE_PRIVATE);
        if(mSettings.contains("userName")) {
            a=mSettings.getString("userName", "");
            Log.e("log_tagsw", "ok " +a);
        }
        if(mSettings.contains("password")) {
            b=mSettings.getString("password", "");
            Log.e("log_tagsw", "ok " +b);

        }
        server ="http://192.168.43.78/user_test.php?login="+a+"+&password="+b;
        ListView lv = getListView();
        LayoutInflater inflater = getLayoutInflater();
        View header = inflater.inflate(R.layout.mu, lv, false);
        lv.addHeaderView(header, null, false);
        this.setTitle("Тесты пользователя");
        new RequestTask().execute();

    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        //String item = (String) getListAdapter().getItem(position);
        Log.e("log_tags", "ok " +position);
        intent = new Intent(this, ResultActivity.class);
        intent.putExtra ("answers", catNames2.get(position-1));
        startActivity(intent);



    }

    class RequestTask extends AsyncTask<String, String, String> {

        //String a="";
        String ca="";
        String d="";

        @Override
        protected String doInBackground(String... params) {

            try {
                //создаем запрос на сервер
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
            } catch (Exception e) {
                System.out.println("Exp=" + e);
                login2="";
            }
            return null;
        }
        public String JSONURL(String result) {
            if (result != null) {


                try {

                    JSONArray myValue = new JSONArray(result);

                    for (int i = 0; i < myValue.length(); i++) {
                        JSONObject c = myValue.getJSONObject(i);
                        //создали читателя json объектов и отдали ему строку - result
                        ca = c.getString("test_id");
                        catNames.add(c.getString("test_id"));
                        catNames2.add(c.getString("answers"));
                             Log.e("log_tags", "ok " +ca);
                        Log.e("log_tags", "ok " +catNames2.get(i));
                    }
               } catch (JSONException e) {
                    Log.e("log_tag", "Error parsing data " + e.toString());
                }
                return ca;
            }
            else return "тестов нет";
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
                JSONURL(answerHTTP);
                try {
                    mAdapter = new ArrayAdapter<String>(UserFragment.this,
                            android.R.layout.simple_list_item_1, catNames);
                    setListAdapter(mAdapter);
                }
                catch (Exception x){}
                //Intent intent = new Intent(getActivity(),ResultActivity.class);
                //startActivity(intent);
            }

            dialog.dismiss();
            super.onPostExecute(result);
        }

        @Override
        protected void onPreExecute() {
            //          Log.d("Tag",login2);
            dialog = new ProgressDialog(UserFragment.this);
            dialog.setMessage("Загрузка...");
            dialog.setIndeterminate(true);
            dialog.setCancelable(true);
            dialog.show();
            super.onPreExecute();
        }
    }
}

