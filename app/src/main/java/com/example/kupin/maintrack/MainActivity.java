package com.example.kupin.maintrack;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements LocationListener {

    SharedPreferences sPref;
    private static final int REQUEST_CODE_PERMISSION = 123;
    LocationManager locationManager ;
    Other other = new Other();

    double minTime,minDistance,Accuracy;

    private final static String TAG = "MainActivity";

    protected void OnStart() {
        int permissionStatus = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        int permissionStatusFile = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
        int permissionStatusFile2 = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        LoadSettings();
        WebView browser=(WebView)findViewById(R.id.webbrowser);
        browser.loadUrl("http://track.e-geron.ru/view.php?token="+LoadToken());
        browser.getSettings().setJavaScriptEnabled(true);
        if (permissionStatus == PackageManager.PERMISSION_GRANTED ) {
            TextView textToken  = (TextView) findViewById(R.id.tvToken);
            textToken.setText( "Ваш токен: " + LoadToken() );


           //  try {

                locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, (long) minTime, (float) minDistance, this);
                /*Location getLast = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                TextView coord = (TextView) findViewById(R.id.textView);

                TextView tok = (TextView) findViewById(R.id.tvToken);
                if(Double.valueOf(getLast.getLatitude())!=null && Double.valueOf(getLast.getLongitude()) != null) {
                    Long = Double.valueOf(getLast.getLongitude());
                    Lat = Double.valueOf(getLast.getProvider());
                }

                coord.setText("Координаты: "+Long + "   " + Lat);

            }
            catch (Exception e ) {
                Toast.makeText(getApplicationContext(),e.getMessage() ,Toast.LENGTH_LONG).show();

            } */
        } else {
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.ACCESS_FINE_LOCATION
                    },
                    REQUEST_CODE_PERMISSION);
        }
    }

    void LoadSettings() {
        sPref = getSharedPreferences("settings",MODE_PRIVATE);
        TextView t = (TextView)findViewById(R.id.outSettings);
        minDistance = 5;
        minTime = 5000;
        Accuracy = 70;
        if((sPref.getString("minDistance","") == "") && (sPref.getString("minTime","") == "") && (sPref.getString("Accuracy","") == "")) {
            minDistance = 5;
            minTime = 5000;
            Accuracy = 70;

        }
        else {
            minDistance = Double.parseDouble(sPref.getString("minDistance",""));
            minTime = Double.parseDouble(sPref.getString("minTime",""));
            Accuracy = Double.parseDouble(sPref.getString("Accuracy",""));
        }

        t.setText("mD "+minDistance+"mT "+minTime+"Ac  "+Accuracy);
    }
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
     //   MyTask Task = new MyTask();
       // Task.execute();

        OnStart();

        if(LoadToken() != "") {
            Toast.makeText(getApplicationContext(),"Успешно",Toast.LENGTH_LONG).show();
            TextView textToken  = (TextView) findViewById(R.id.tvToken);
            textToken.setText( "Ваш токен: " +LoadToken());
        }
        else {
            Other other = new Other();
            other.GeneratedToken();
            Toast.makeText(getApplicationContext(),other.token,Toast.LENGTH_LONG).show();
            SaveToken(other.token);
        }
    }
    Double  Long,Lat;
    String url; // "http://nottheblog.ru/interface/query.php?long="+Long+"&lat="+Lat+"&token="+LoadToken();//"http://hd.e-geron.ru/query.php?long="+Long+"&lat="+Lat;//"http://hd.e-geron.ru/answer.html?CoorLong=55&CoorLat=22" ;
    // http://nottheblog.ru/interface/query.php?long=112.499432&lat=52.03333&token=XXfdsdfsy
   /* public void Onclick(View view) {
    if(LoadToken() != "") {
            Toast.makeText(getApplicationContext(),"Nice",Toast.LENGTH_LONG).show();
            TextView textToken  = (TextView) findViewById(R.id.tvToken);
            textToken.setText( "Ваш токен: " +LoadToken());
        }
        else {
            Other other = new Other();
            other.GeneratedToken();
            Toast.makeText(getApplicationContext(),other.token,Toast.LENGTH_LONG).show();
            SaveToken(other.token);
        }

    } */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.mainmenu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch(id){
            case R.id.settings :
                Intent intent = new Intent(this,Setting.class);
                startActivity(intent);
                return true;
            case R.id.infomations:
               Intent infotent = new Intent(this,informations.class);
               startActivity(infotent);
                return true;

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onLocationChanged(Location location) {

        DecimalFormat format = new DecimalFormat("###.####");
        TextView coord = (TextView) findViewById(R.id.coordination);
        Toast.makeText(getApplicationContext(), String.valueOf(location.getAccuracy()),Toast.LENGTH_LONG).show();
        TextView tok = (TextView) findViewById(R.id.tvToken);
        if((Double.valueOf(location.getLatitude())!=null && Double.valueOf(location.getLongitude()) != null) && (location.getAccuracy() <= Accuracy)) {
            Long = Double.valueOf(location.getLongitude());
            Lat = Double.valueOf(location.getLatitude());
            coord.setText("Координаты: " + Long + "   " + Lat);

            url = "http://track.e-geron.ru/query.php?long=" + Long + "&lat=" + Lat + "&token=" + LoadToken();
            //Toast.makeText(getApplicationContext(),url,Toast.LENGTH_LONG).show();


                try {

                    GetAnswer(url);
                } catch (Exception e) {
                    Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }


    }
    void SaveToken(String token) {
        sPref = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor ed = sPref.edit();
        ed.putString("token",token);
        ed.commit();
        Toast.makeText(this, "Token saved", Toast.LENGTH_SHORT).show();
    }

    String LoadToken() {
        sPref = getPreferences(MODE_PRIVATE);
        String token = sPref.getString("token", "");

        return  token;
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }



    @Override
    public void onProviderDisabled(String provider) {
        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        startActivity(intent);
        Toast.makeText(getApplicationContext(),"Включите геолокацию", Toast.LENGTH_LONG).show();
    }
    String result ;
      class MyTask extends AsyncTask<Void, Void, String> {


        @Override
        protected String doInBackground(Void... voids) {


            try {
                URL obj = new URL(url);
                HttpURLConnection connection = (HttpURLConnection) obj.openConnection();

                //add reuqest header
                connection.setRequestMethod("GET");
                connection.setRequestProperty("User-Agent", "Mozilla/5.0" );
                connection.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
                connection.setRequestProperty("Content-Type", "application/json");

                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String inputLine;
                StringBuffer response = new StringBuffer();

                while ((inputLine = bufferedReader.readLine()) != null) {
                    response.append(inputLine);
                }
                bufferedReader.close();

                result =  "Вы тут: \n"+response.toString();
            } catch (Exception e) {
               Toast.makeText(getApplicationContext(),"SHIT FUCK",Toast.LENGTH_LONG).show();
            }
            return result;
        }

        protected  void onPostExecute(String result) {

            TextView text = (TextView) findViewById(R.id.TvRez) ;



            text.setText(result);
        }

    }


  public void GetAnswer(String url)  {
        final TextView s = (TextView) findViewById(R.id.TvRez) ;
        RequestQueue queue = Volley.newRequestQueue(this);

// Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.

                        result = response;
                        s.setText("Ваше местоположение: "+ response ); //response.substring(0,500)

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                s.setText("That didn't work!" + error.getMessage() + error.networkResponse.statusCode + " " + error.getLocalizedMessage() );
            }
        });

// Add the request to the RequestQueue.
        queue.add(stringRequest);
    }



    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_PERMISSION:
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission granted

                } else {
                    // permission denied
                }
                return;
        }
    }
}
