package com.app.test;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
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

import androidx.appcompat.app.ActionBar;
import androidx.fragment.app.Fragment;



public class DashboardFragment extends Fragment  {
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
    SharedPreferences mSettings;
    private ProgressDialog dialog;
    public DashboardFragment() {
    }

    public static DashboardFragment newInstance() {
        return new DashboardFragment();
    }
    private static final String MY_SETTINGS = "my_settings";
    View rootView;
    String id;

    String server ="http://192.168.43.78/index5.php?a="+counter;
    @Override
    public android.view.View onCreateView(LayoutInflater inflater, ViewGroup container,
                                          Bundle savedInstanceState) {

        rootView =
                inflater.inflate(R.layout.activity_test, container, false);

        // выделяем элемент
        Chronometer chronometer = (Chronometer) rootView.findViewById(R.id.chronometer);
        long startTime = SystemClock.elapsedRealtime();
        chronometer.setBase(startTime);
        chronometer.start();
        pbHorizontal = (ProgressBar) rootView.findViewById(R.id.progressBar);
        pbHorizontal.setVisibility(ProgressBar.VISIBLE);
        for (int i=1;i<=71;i++) answer[i]=-1;
        button = (Button) rootView.findViewById(R.id.button3);
        button2 = (Button) rootView.findViewById(R.id.button4);
        textView = (TextView) rootView.findViewById(R.id.textView7);
        textView2 = (TextView) rootView.findViewById(R.id.textView5);
        button2.setEnabled(false);
        button2.setVisibility(View.INVISIBLE);
        mSettings = getActivity().getSharedPreferences(MY_SETTINGS,
                Context.MODE_PRIVATE);
        if(mSettings.contains("id")) {
           id=mSettings.getString("id", "");
        }
        button2.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //тут указываем куда будем конектится, для примера я привел удаленных хост если у вас не получилось освоить wamp (:

                RadioGroup radioGroup =  rootView.findViewById(R.id.radioGroup2);
                RadioButton radioButton1 = rootView.findViewById(R.id.radioButton3);
                RadioButton radioButton2 =  rootView.findViewById(R.id.radioButton4);
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
                    textView2.setText("Вопрос "+counter+"/71");
                    server ="http://192.168.43.78/index5.php?a="+counter;
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
                    Toast toast = Toast.makeText(getActivity().getApplicationContext(),
                            "Выберите вариант ответа!", Toast.LENGTH_SHORT);
                    toast.show();
                }


            }
        });
        button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //тут указываем куда будем конектится, для примера я привел удаленных хост если у вас не получилось освоить wamp (:

                RadioGroup radioGroup =  rootView.findViewById(R.id.radioGroup2);
                RadioButton radioButton1 = rootView.findViewById(R.id.radioButton3);
                RadioButton radioButton2 =  rootView.findViewById(R.id.radioButton4);
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
                        button.setText("Завершить");
                        button.setBackgroundResource(R.drawable.qw);
                    }
                    else{
                        button.setText("");
                        button.setBackgroundResource(R.drawable.next1);
                    }
                    if (counter==72)
                    {
                        String ms=new String();
                        for (int ii=0;ii<answer.length;ii++)
                        {


                            ms+=String.valueOf(answer[ii]);
                        }
                        server ="http://192.168.43.78/create_test.php?id="+id+"&result="+ms;
                        if(id==null)
                        {
                            intent = new Intent(getActivity(), ResultActivity.class);

                            intent.putExtra ("answers", answer);
                            startActivity(intent);

                        }
                        Log.e("result", answer.toString());
                    }
                    else{
                        server ="http://192.168.43.78/index5.php?a="+counter;
                    }

                    textView2.setText("Вопрос "+counter+"/71");
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
                    Toast toast = Toast.makeText(getActivity().getApplicationContext(),
                            "Выберите вариант ответа!", Toast.LENGTH_SHORT);
                    toast.show();
                }


            }
        });
        new MyAsyncTask().execute();

        return  rootView;
    }
    class MyAsyncTask extends AsyncTask<String, String, String> {

        String a, b, answerHTTP;


        @Override
        protected void onPreExecute() {
            dialog = new ProgressDialog(getActivity());
            dialog.setMessage("Загрузка...");
            dialog.setIndeterminate(true);
            dialog.setCancelable(false);
            dialog.show();
            if (counter==72){
            intent = new Intent(getActivity(), ResultActivity.class);
            intent.putExtra ("answers", answer);

            startActivity(intent);
            }
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

        }
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                getActivity().finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }




    private void postProgress(int progress) {
        String strProgress = String.valueOf(progress) + " %";
        pbHorizontal.setProgress(progress);
    }
}