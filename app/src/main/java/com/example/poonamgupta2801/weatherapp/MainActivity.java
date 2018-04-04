package com.example.poonamgupta2801.weatherapp;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {
    EditText enterCityName;
    TextView cityNameTextView, weatherInfoTextView;
    Button findWeather;
    ImageView backgroundImage;


    public class DownloadTask extends AsyncTask<String, Void, String>{

        @Override
        protected String doInBackground(String... urls) {

            String result="";
            URL url;
            HttpURLConnection urlConnection=null;

            try {

                url= new URL(urls[0]);
                urlConnection=(HttpURLConnection) url.openConnection ();
                urlConnection.connect ();
                InputStream inputStream= urlConnection.getInputStream ();
                InputStreamReader inputStreamReader=new InputStreamReader ( inputStream );

                int data=inputStreamReader.read ();

                while(data!=-1){

                    char currentData=(char)data;
                    result+=currentData;
                    data=inputStreamReader.read ();

                }

                return result;

            } catch (Exception e) {
                Toast.makeText(getApplicationContext(), "Could not find weather", Toast.LENGTH_LONG).show ();

            }


            return null;
        }
        @Override
        public void onPostExecute (String result){
            super.onPostExecute ( result);

            try {

                String weatherInfoMessage="";

                JSONObject jsonObject= new JSONObject (result  );

                String weatherInfo= jsonObject.getString("weather");

                Log.i("Weather content", weatherInfo);

                JSONArray jsonArray= new JSONArray(weatherInfo);

                for(int i=0; i<jsonArray.length (); i++){

                    JSONObject jsonPart= jsonArray.getJSONObject ( i);

                    String main="";
                    String description="";

                    main=jsonPart.getString ( main );
                    result=jsonPart.getString ( description);

                    Log.i("main",jsonPart.getString ( "main" ));

                    Log.i("description",jsonPart.getString ( "description" ));

                    if(main!=""&&description!=""){
                        weatherInfoMessage+= main + ": " + description + "\r\n";
                    }

                }

                if(weatherInfoMessage!=""){

                 weatherInfoTextView.setText ( weatherInfoMessage );
                } else {

                    Toast.makeText(getApplicationContext(), "Could not find weather", Toast.LENGTH_LONG).show ();
                }


            } catch (JSONException e) {
                Toast.makeText(getApplicationContext(), "Could not find weather", Toast.LENGTH_LONG).show ();
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.activity_main );

        findWeather=(Button)findViewById (R.id.button);
        backgroundImage=(ImageView)findViewById ( R.id.imageView );
        cityNameTextView=(TextView)findViewById ( R.id. cityNameTextView);
        enterCityName=(EditText)findViewById ( R.id.cityNameEditText );
        weatherInfoTextView=(TextView)findViewById ( R.id.cityWeatherTextView );

    }

    public void fetchTheWeather(View view) {

        Log.i("City", enterCityName.getText ().toString ());

        InputMethodManager inputMethodManager=(InputMethodManager)getSystemService ( Context.INPUT_METHOD_SERVICE );
        inputMethodManager.hideSoftInputFromWindow ( enterCityName.getWindowToken (),0 );

        try {

            DownloadTask downloadTask=new DownloadTask ();
            String encodedCityName=URLEncoder.encode (enterCityName.getText ().toString (),"UTF-8") ;

            downloadTask.execute ( "http://samples.openweathermap.org/data/2.5/weather?q="+ encodedCityName+"+\"&appid=fd7e9d3dfeeec2eb4c87ace19d863567");

        } catch (UnsupportedEncodingException e) {

            Toast.makeText(getApplicationContext(), "Could not find weather", Toast.LENGTH_LONG).show ();
        }
    }
}
