package com.app.test;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
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
import java.util.ArrayList;
import java.util.List;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.fragment.app.ListFragment;





public class NotificationsFragment extends ListFragment {
Intent intent;
    String login2;
    String password2;
    String a, b, answerHTTP;
    private ProgressDialog dialog;
    SharedPreferences mSettings;
    private ArrayAdapter<String> mAdapter;
    String server ="http://192.168.43.78/user_test.php?login="+a+"+&password="+b;
    // определяем массив типа String
    final List<String> catNames = new ArrayList<String>();
    final List<String> catNames2 = new ArrayList<String>();
    public static NotificationsFragment newInstance() {
        return new NotificationsFragment();
    }
    private static final String MY_SETTINGS = "my_settings";
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


        mSettings = getActivity().getSharedPreferences(MY_SETTINGS,
                Context.MODE_PRIVATE);
        if(mSettings.contains("id")) {
            //id=mSettings.getString("id", "");
        }
        if(mSettings.contains("userName")) {
            a=mSettings.getString("userName", "");
            Log.e("log_tags", "ok " +a);
        }
        if(mSettings.contains("password")) {
            b=mSettings.getString("password", "");

        }
        if(mSettings.getString("role", "").equals("1")) {
            server = "http://192.168.43.78/users.php";

        }
            else server ="http://192.168.43.78/user_test.php?login="+a+"+&password="+b;




        new RequestTask().execute();

    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        String item = (String) getListAdapter().getItem(position);
        Log.e("log_tags", "ok " +position);
        //Toast.makeText(this, item + " selected", Toast.LENGTH_LONG).show();
        if(mSettings.getString("role", "").equals("1"))
           {
                SharedPreferences.Editor e = mSettings.edit();
                e.putString("userName", catNames.get(position));
                e.putString("password", catNames2.get(position));
                e.commit();
               intent = new Intent(getActivity(), UserFragment.class);
                     }
        else{
            intent = new Intent(getActivity(), ResultActivity.class);
           intent.putExtra ("answers", catNames2.get(position));


            }
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
                        //Log.e("log_tags", "ok " +JSONURL(answerHTTP));
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
                        if (server=="http://192.168.43.78/users.php"){
                            ca = c.getString("id");
                            catNames.add(c.getString("login"));
                            catNames2.add(c.getString("password"));
                        }
                            else{
                            ca = c.getString("test_id");
                            catNames.add(c.getString("test_id"));
                            catNames2.add(c.getString("answers"));

                        }

                        //дальше находим вход в наш json им является ключевое слово data
                        Log.e("log_tags", "ok " +ca);
                    }
                    //проходим циклом по всем нашим параметрам11
                    //=joShow.getString("message");
                    //читаем что в себе хранит параметр lastname


                } catch (JSONException e) {
                    Log.e("log_tag", "Error parsing data " + e.toString());
                }
                return ca;
            }
            else return "тестов нет";
        }
        @Override
        protected void onPostExecute(String result) {

               JSONURL(answerHTTP);
                try {

                    mAdapter = new ArrayAdapter<String>(getActivity(),
                            android.R.layout.simple_list_item_1, catNames);
                    setListAdapter(mAdapter);
                }
                catch (Exception x){}
                //Intent intent = new Intent(getActivity(),ResultActivity.class);
                //startActivity(intent);


            dialog.dismiss();
            super.onPostExecute(result);
        }

        @Override
        protected void onPreExecute() {
  //          Log.d("Tag",login2);
            dialog = new ProgressDialog(getActivity());
            dialog.setMessage("Загрузка...");
            dialog.setIndeterminate(true);
            dialog.setCancelable(true);
            dialog.show();
            super.onPreExecute();
        }
    }
}
