package com.example.aditya.weatherap;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    EditText et1=null;
    ImageButton btn=null;
    TextView tvtemp=null;
    TextView tvcity=null;
    TextView tvcondition=null;
    ImageView imgcond=null;
    ListView lstvw=null;
    ArrayList<Day> lst=null;
    ArrayList<Bitmap> imgLst=null;
    boolean trig=true;
    boolean shot=true;
    String[] places={"Toronto","Ottawa","Vancouver","Seattle","Mumbai","London","Hyderabad","Vijayawada","Schiphol","Moscow","Barcelona","Rome","Budapest","Prague","Paris"};
    int count=-1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        et1=(EditText)findViewById(R.id.etplace);
        btn=(ImageButton)findViewById(R.id.getLocation);
        tvtemp=(TextView)findViewById(R.id.tvtemp);
        tvcity=(TextView)findViewById(R.id.tvcity);
        tvcondition=(TextView)findViewById(R.id.tvcondition);
        imgcond=(ImageView)findViewById(R.id.imgcond);
        lstvw=(ListView)findViewById(R.id.lstvw);
        lst=new ArrayList<Day>();
        imgLst=new ArrayList<Bitmap>();


        new CountDownTimer(45000,3000){

            @Override
            public void onTick(long l) {
                if (trig==true) {
                    if (count < 14) {
                        count++;
                    }
                    DownloadTask task = new DownloadTask();
                    task.execute("http://api.openweathermap.org/data/2.5/weather?q=" + places[count] + "&units=metric&APPID=0b2c11c47932ad97926c2017e05da29e");
                    tvcity.setText(places[count]);
                }
            }

            @Override
            public void onFinish() {

            }
        }.start();
    }

    public class DownloadImage extends AsyncTask<String , Void ,Bitmap>{
        URL url=null;
        HttpURLConnection urlConnection=null;
        String result="";

        @Override
        protected Bitmap doInBackground(String... strings) {
            try {
                url=new URL(strings[0]);
                urlConnection= (HttpURLConnection) url.openConnection();
                urlConnection.connect();
                InputStream in=urlConnection.getInputStream();
                Bitmap image= BitmapFactory.decodeStream(in);
                return image;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);

                imgcond.setImageBitmap(bitmap);
        }
    }

    public void getLocation(View view) {
        trig=false;
        if(et1.getText()==null){
            et1.setText("Enter location");
        }

        DownloadTask task=new DownloadTask();
        task.execute("http://api.openweathermap.org/data/2.5/weather?q="+et1.getText()+"&units=metric&APPID=0b2c11c47932ad97926c2017e05da29e");
        tvcity.setText(et1.getText());

        //arraylist
        shot=false;
        DownloadForecast forecast=new DownloadForecast();
        forecast.execute("http://api.openweathermap.org/data/2.5/forecast?q="+tvcity.getText()+"&units=metric&APPID=0b2c11c47932ad97926c2017e05da29e");
        CustomAdapter customAdapter=new CustomAdapter(this,R.layout.row,lst);
        lstvw.setAdapter(customAdapter);
    }

    public class DownloadForecast extends AsyncTask<String,Void,String> {
        URL url=null;
        HttpURLConnection urlConnection=null;
        String result="";

        @Override
        protected String doInBackground(String... strings) {
            try {
                url=new URL(strings[0]);
                urlConnection= (HttpURLConnection) url.openConnection();
                InputStream in=urlConnection.getInputStream();
                InputStreamReader reader=new InputStreamReader(in);
                int data=reader.read();
                while(data!=-1){
                    char current=(char)data;
                    result+=current;
                    data=reader.read();
                }
                return result;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                JSONObject obj=new JSONObject(s);
                JSONArray arr=new JSONArray(obj.getString("list"));
                for(int i=0;i<arr.length();i++){
                    JSONObject objpart=arr.getJSONObject(i);
                    JSONObject objWeather=new JSONObject(objpart.getString("main"));

                    JSONArray arr1=new JSONArray(objpart.getString("weather"));
                    for(int j=0;j<arr1.length();j++){
                        //count1++;
                        JSONObject objarr1=arr1.getJSONObject(j);
                        //Log.i("result",objWeather.getString("temp")+" "+objpart.getString("dt_txt")+" "+objarr1.getString("icon"));
                        //DownloadImage imgtask=new DownloadImage();
                        //imgtask.execute("http://openweathermap.org/img/w/"+objarr1.getString("icon")+".png");
                        lst.add(new Day(objpart.getString("dt_txt").toString(),objWeather.getString("temp").toString()+"°C"));
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public class DownloadTask extends AsyncTask<String,Void,String>{
        URL url=null;
        HttpURLConnection urlConnection=null;
        String result="";

        @Override
        protected String doInBackground(String... strings) {
            try {
                url=new URL(strings[0]);
                urlConnection= (HttpURLConnection) url.openConnection();
                InputStream in=urlConnection.getInputStream();
                InputStreamReader reader=new InputStreamReader(in);
                int data=reader.read();
                while(data!=-1){
                    char current=(char)data;
                    result+=current;
                    data=reader.read();
                }
                return result;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            try {
                Log.i("result",result);
                JSONObject obj1=new JSONObject(result);

                String arr=obj1.getString("weather");
                JSONArray jarr=new JSONArray(arr);
                for(int i=0;i<jarr.length();i++){
                    JSONObject jpart=jarr.getJSONObject(i);
                   DownloadImage image=new DownloadImage();
                   image.execute("http://openweathermap.org/img/w/"+jpart.getString("icon")+".png");
                    tvcondition.setText(jpart.getString("description"));

                }
                String obj2=obj1.getString("main");
                JSONObject obj3=new JSONObject(obj2);
                tvtemp.setText(obj3.getString("temp")+"°C");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
