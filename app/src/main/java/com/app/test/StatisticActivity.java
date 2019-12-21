package com.app.test;
import com.google.android.material.tabs.TabLayout;
import com.jjoe64.graphview.GraphView;

import com.jjoe64.graphview.series.BarGraphSeries;
import com.jjoe64.graphview.series.DataPoint;

import androidx.fragment.app.Fragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
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

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import static java.lang.Math.round;

public class StatisticActivity extends Fragment  {
    public static StatisticActivity newInstance() {
        return new StatisticActivity();
    }
    static final String TAG = "myLogs";
    String a, b, answerHTTP;
    float[][][] ad=new float[2][12][10000];
    List<String> ad2=new ArrayList<>();
    Intent intent;
    String login2;
    String password2;
    private ProgressDialog dialog;
    int id=0;
    int k=-1;
    SharedPreferences mSettings;
    private ArrayAdapter<String> mAdapter;
    GraphView graph;
    GraphView graph2;
    // определяем массив типа String
    final List<String> catNames = new ArrayList<String>();
    final List<String> catNames2 = new ArrayList<String>();
    final List<String> catNames3 = new ArrayList<String>();
    final List<String> catNames4 = new ArrayList<String>();
    Button button7;
    View rootView;
    public StatisticActivity() {
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }
    @Override
    public android.view.View onCreateView(LayoutInflater inflater, ViewGroup container,
                                          Bundle savedInstanceState) {

        rootView =
                inflater.inflate(R.layout.activity_statistic, container, false);

        graph = (GraphView) rootView.findViewById(R.id.graph);
        graph.setTitle("Мужчины");
        graph2 = (GraphView) rootView.findViewById(R.id.graph2);
        graph2.setTitle("Женщины");

        button7 = (Button) rootView.findViewById(R.id.button7);
        button7.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(android.view.View v) {
                switch (v.getId()) {

                    case R.id.button7:
                        intent = new Intent(getActivity(), ScalsActivity.class);
                        startActivity(intent);
                        break;

                }
            }
        });
        new RequestTask().execute();
        return rootView;
    }

    Menu ny;
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.add(Menu.NONE, Menu.FIRST + 1, 0, "1 (Нs) Ипохондрия");
        menu.add(Menu.NONE, Menu.FIRST + 2, 0, "2 (D) Депрессия");
        menu.add(Menu.NONE, Menu.FIRST + 3, 0, "3 (Ну) Истерия");
        menu.add(Menu.NONE, Menu.FIRST + 4, 0,  "4 (Рd) Психопатия");
        menu.add(Menu.NONE, Menu.FIRST + 5, 0, "6 (Ра) Паранойяльность");
        menu.add(Menu.NONE, Menu.FIRST + 6, 0, "7 (Рt) Психастения");
        menu.add(Menu.NONE, Menu.FIRST + 7, 0, "8 (Sc) Шизоидность");
        menu.add(Menu.NONE, Menu.FIRST + 8, 0, "9 (Ма) Гипомания");
        menu.add(Menu.NONE, Menu.FIRST + 9, 0,"(L) Ложь");
        menu.add(Menu.NONE, Menu.FIRST + 10, 0,"(F) Достоверность");
        menu.add(Menu.NONE, Menu.FIRST + 11, 0,"(K) Коррекция");
        inflater.inflate(R.menu.menu2,menu);
        super.onCreateOptionsMenu(menu,inflater);
    }


float ad3[]=new float[50];
    public float[] Statistic(float[][][] ad,int k1,int flag){
        for (int k2=0;k2<25;k2++) {

                ad3[k2]=0;

        }

        for (int k2=0;k2<k;k2++) {

                ad3[round(ad[flag][k1][k2])]++;
            }
        for (int k2=0;k2<25;k2++) {

            ad3[k2]=ad3[k2]/k*100;
            Log.e("Ефв",String.valueOf(k));

        }
        return ad3;
    }

    private void generateDataForGraph(float[] a) {
        int size = 25;
        GraphView graph = (GraphView) getActivity().findViewById(R.id.graph);
        DataPoint[] values = new DataPoint[size];
        for (int i=0; i<size; i++) {
            Integer xi = i;
            Integer yi = (int)(Math.round(a[i]));
            DataPoint v = new DataPoint(xi, yi);
            values[i] = v;
        }

        BarGraphSeries<DataPoint>  series = new BarGraphSeries<DataPoint>(values);
        graph.addSeries(series);
        graph.invalidate();

    }
    private void generateDataForGraph2(float[] a) {
        int size = 25;
        GraphView graph = (GraphView) getActivity().findViewById(R.id.graph2);
        DataPoint[] values = new DataPoint[size];
        for (int i=0; i<size; i++) {
            Integer xi = i;
            Integer yi = (int)(Math.round(a[i]));
            DataPoint v = new DataPoint(xi, yi);
            values[i] = v;
        }
        BarGraphSeries<DataPoint>  series = new BarGraphSeries<DataPoint>(values);
        graph.addSeries(series);
        graph.invalidate();

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        float[] Statistic;
        float[] Statistic2;
        getActivity().setTitle(item.getTitle());
        graph.removeAllSeries();
        graph2.removeAllSeries();
         switch (id){
             case android.R.id.home:
                 getActivity().finish();
                 return true;
            case Menu.FIRST+1:
                Statistic=Statistic(ad,1,0);
                generateDataForGraph(Statistic);
                Statistic2=Statistic(ad,1,1);
                generateDataForGraph2(Statistic2);
                return true;
            case Menu.FIRST+2:
                Statistic=Statistic(ad,2,0);
                generateDataForGraph(Statistic);
                Statistic2=Statistic(ad,2,1);
                generateDataForGraph2(Statistic2);
                return true;
            case Menu.FIRST+3:
                Statistic=Statistic(ad,3,0);
                generateDataForGraph(Statistic);
                Statistic2=Statistic(ad,3,1);
                generateDataForGraph2(Statistic2);
                return true;
            case Menu.FIRST+4:
                Statistic=Statistic(ad,4,0);
                generateDataForGraph(Statistic);
                Statistic2=Statistic(ad,4,1);
                generateDataForGraph2(Statistic2);
                return true;
            case Menu.FIRST+5:
                Statistic=Statistic(ad,5,0);
                generateDataForGraph(Statistic);
                Statistic2=Statistic(ad,5,1);
                generateDataForGraph2(Statistic2);
                return true;
            case Menu.FIRST+6:
                Statistic=Statistic(ad,6,0);
                generateDataForGraph(Statistic);
                Statistic2=Statistic(ad,6,1);
                generateDataForGraph2(Statistic2);
            case Menu.FIRST+7:
                Statistic=Statistic(ad,7,0);
                generateDataForGraph(Statistic);
                Statistic2=Statistic(ad,7,1);
                generateDataForGraph2(Statistic2);
                return true;
            case Menu.FIRST+8:
                Statistic=Statistic(ad,8,0);
                generateDataForGraph(Statistic);
                Statistic2=Statistic(ad,8,1);
                generateDataForGraph2(Statistic2);
                return true;
            case Menu.FIRST+9:
                Statistic=Statistic(ad,9,0);
                generateDataForGraph(Statistic);
                Statistic2=Statistic(ad,9,1);
                generateDataForGraph2(Statistic2);
                return true;
            case Menu.FIRST+10:
                Statistic=Statistic(ad,10,0);
                generateDataForGraph(Statistic);
                Statistic2=Statistic(ad,10,1);
                generateDataForGraph2(Statistic2);
                return true;
            case Menu.FIRST+11:
                Statistic=Statistic(ad,11,0);
                generateDataForGraph(Statistic);
                Statistic2=Statistic(ad,11,1);
                generateDataForGraph2(Statistic2);
                return true;

        }

        return super.onOptionsItemSelected(item);
    }
    class RequestTask extends AsyncTask<String, String, String> {
        int qw = 0;
        //String a="";
        String ca="";
        String d="";
        String server ="http://192.168.43.78/all_users.php";
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
                        String b45=JSONURL(answerHTTP);

                        for (int i=0;i<catNames.size();i++) {
                            httpget = new HttpGet("http://192.168.43.78/user_test.php?login=" + catNames.get(i) + "+&password=" + catNames2.get(i));
                            response = httpclient.execute(httpget);
                            HttpEntity entity2 = response.getEntity();
                            answerHTTP = EntityUtils.toString(entity2);
                            String a=JSONURL2(answerHTTP);
                            if (! a.equals("тестов нет")) {
                                Log.e("log_tags", "ok " + answerHTTP);
                                for (int j = 0; j < catNames3.size(); j++) {
                                    httpget = new HttpGet("http://192.168.43.78/index7.php?a=" + catNames3.get(j));
                                    response = httpclient.execute(httpget);
                                    entity2 = response.getEntity();
                                    k++;
                                    answerHTTP = EntityUtils.toString(entity2);
                                    Log.e("log_tagss", "okcs " + catNames3.get(j));
                                    int r = 1;
                                    int jf = 1;

                                    int r1 = 0;
                                    String aa = "";
                                    String b = "";
                                    while (r < answerHTTP.length()) {
                                        if ((r + 4) >= answerHTTP.length()) break;
                                        if (answerHTTP.substring(r, r + 4).equals("балл")) {
                                            r = r + 5;
                                            aa = "";
                                            while ((Character.toLowerCase((char) answerHTTP.charAt(r)) != Character.toLowerCase((char) ' '))) {
                                                if (r > answerHTTP.length()) break;
                                                aa = aa + answerHTTP.charAt(r);
                                                r++;

                                            }
                                            Log.e("log_tagss", "баллы " + Float.parseFloat(a));
                                            if (catNames4.get(i).equals("m"))
                                            ad[0][jf][k] = Float.parseFloat(aa);
                                            else  ad[1][jf][k] = Float.parseFloat(aa);
                                            b = b + aa;
                                            jf++;
                                        }
                                        Log.e("log_tagss", "okc " + jf);

                                        r++;
                                    }


                                }
                                catNames3.clear();
                            }
                        }
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
        public boolean JSONURL3(String result) {
            if (result!=null ) {


                try {


                        JSONObject c =new JSONObject(result);
                        //создали читателя json объектов и отдали ему строку - result
                        String a= c.getString("message");


                    //проходим циклом по всем нашим параметрам11
                    //=joShow.getString("message");
                    //читаем что в себе хранит параметр lastname

                    return false;
                } catch (JSONException e) {

                    return true;
                }

            }
            else return false;
        }
        public String JSONURL2(String result) {
            if (JSONURL3(result) ) {


                try {

                    JSONArray myValue = new JSONArray(result);

                    for (int i = 0; i < myValue.length(); i++) {
                        JSONObject c = myValue.getJSONObject(i);
                        //создали читателя json объектов и отдали ему строку - result
                        ca = c.getString("test_id");
                        catNames3.add(c.getString("answers"));
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
        public String JSONURL(String result) {
            if (result != null) {


                try {

                    JSONArray myValue = new JSONArray(result);

                    for (int i = 0; i < myValue.length(); i++) {
                        JSONObject c = myValue.getJSONObject(i);
                        //создали читателя json объектов и отдали ему строку - result
                        ca = c.getString("id");
                        catNames.add(c.getString("login"));
                        catNames2.add(c.getString("password"));
                        catNames4.add(c.getString("gender"));
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
            if (login2 == "") {
                Toast toast = Toast.makeText(getActivity().getApplicationContext(),
                        "Неверный логин или пароль!", Toast.LENGTH_SHORT);
                toast.show();
            } else {
                //JSONURL(answerHTTP);


                dialog.dismiss();
                getActivity().invalidateOptionsMenu();
                super.onPostExecute(result);
            }
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