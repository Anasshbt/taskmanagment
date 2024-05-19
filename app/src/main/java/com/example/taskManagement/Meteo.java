package com.example.taskManagement;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputEditText;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Meteo extends AppCompatActivity {

    private RelativeLayout homeRl;
    private ProgressBar loadingPB;
    private TextView cityNameTV,temperatureTV,conditionTV;
    private TextInputEditText cityEdt;
    private ImageView backIV;
    private ImageView iconIV;

    private ArrayList<WeatherRVModal> weatherRVModalArrayList;
    private WeatherRVAdapter weatherRVAdapter;
    private static final int PERMISSION_CODE= 1;

    private String  cityName ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setNavigationBarColor(Color.parseColor("#008DDA"));
        getWindow().setStatusBarColor(Color.parseColor("#008DDA"));
        setContentView(R.layout.activity_meteo);
        homeRl=findViewById(R.id.idRLHome);
        loadingPB=findViewById(R.id.idPBLoading);
        cityNameTV=findViewById(R.id.idTVCityName);
        temperatureTV=findViewById(R.id.idTVTemperature);
        conditionTV=findViewById(R.id.idTVCondition);
        RecyclerView weatherRV = findViewById(R.id.idRvWeather);
        cityEdt=findViewById(R.id.idEdtCity);
        backIV=findViewById(R.id.idIVBack);
        iconIV=findViewById(R.id.idIVIcon);
        ImageView searchIV = findViewById(R.id.idIVSearch);

        weatherRVModalArrayList=new ArrayList<>();
        weatherRVAdapter=new WeatherRVAdapter(this,weatherRVModalArrayList);
        weatherRV.setAdapter(weatherRVAdapter);

        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(Meteo.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                    PERMISSION_CODE);



        }

        Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        assert location != null;
        cityName=getCityName(location.getLongitude(),location.getLatitude());


        searchIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String city = cityEdt.getText().toString();
                if (city.isEmpty()){
                    Toast.makeText(Meteo.this ," please enter city ..",Toast.LENGTH_SHORT).show();

                }else{
                    cityNameTV.setText(cityName);
                    getweatherinfo(city);
                }

            }
        });



    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode==PERMISSION_CODE){
            if (grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
                Toast.makeText(this,"permision granted",Toast.LENGTH_SHORT).show();

            }else {
                Toast.makeText(this,"please provide the permission ",Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }



    private String getCityName(double longitude, double latitude) {

        String CityName= "Not Found";
        Geocoder gcd = new Geocoder(getBaseContext(), Locale.getDefault());
        System.out.println("1");

        try {
            List<Address> addresses =gcd.getFromLocation(latitude,longitude,10);
            for ( Address adr : addresses){
                System.out.println("2");
                if (adr!=null){
                    System.out.println("3");
                    String city= adr.getLocality();
                    if (city!=null && !city.equals("")) {
                        CityName=city;

                    }else{
                        Log.d("Tag","city not found");
                        Toast.makeText(this ,"user city not found ..",Toast.LENGTH_SHORT).show();
                    }
                }
            }



        } catch (IOException e) {
            e.printStackTrace();
        }


        return CityName;
    }

    private void getweatherinfo(String cityname){
        if (cityname.isEmpty()) {

            cityname = "Rabat";}

        String url="http://api.weatherapi.com/v1/forecast.json?key=3ac92895c9704cbbb30163652241804&q="+cityname+"&days=1&aqi=yes&alerts=yes";

        cityNameTV.setText(cityname);

        RequestQueue requestQueue = Volley.newRequestQueue(Meteo.this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                loadingPB.setVisibility(View.GONE);
                homeRl.setVisibility(View.VISIBLE);
                weatherRVModalArrayList.clear();

                try {
                    String temperature = response.getJSONObject("current").getString("temp_c");
                    temperatureTV.setText(temperature+ "°C");
                    int is_day = response.getJSONObject("current").getInt("is_day");
                    String condition = response.getJSONObject("current").getJSONObject("condition").getString("text");
                    String conditionIcon = response.getJSONObject("current").getJSONObject("condition").getString("icon");
                    Picasso.get().load("http:".concat(conditionIcon)).into(iconIV);
                    conditionTV.setText(condition);
                    /*if (is_day == 1){
                        Picasso.get().load("https://www.google.com/imgres?q=blue%20ciel%20background&imgurl=https%3A%2F%2Fimg.freepik.com%2Fphotos-gratuite%2Fpapier-peint-graphique-2d-gradients-granuleux-colores_23-2151001503.jpg&imgrefurl=https%3A%2F%2Ffr.freepik.com%2Fphotos-vecteurs-libre%2Ffond-bleu-ciel&docid=XpgE4HN3KlcCWM&tbnid=G1b_jg32t8Vf6M&vet=12ahUKEwjq787Pp-iFAxV7AvsDHctdAaoQM3oECBkQAA..i&w=417&h=626&hcb=2&ved=2ahUKEwjq787Pp-iFAxV7AvsDHctdAaoQM3oECBkQAA").into(backIV);
                    }else {
                        Picasso.get().load("https://media.istockphoto.com/id/1367980878/vector/futuristic-geometric-deep-blue-gradation-background-illustration.jpg?s=1024x1024&w=is&k=20&c=bim2Bzkres0aX5OMv6Gc0zPXuL97xywfPQ10LMR-Uy4=").into(backIV);
                    }*/

                    JSONObject forcastobj=response.getJSONObject("forecast");
                    JSONObject forcast0=forcastobj.getJSONArray("forecastday").getJSONObject(0);
                    JSONArray hourArray=forcast0.getJSONArray("hour");

                    for (int i=0;i<hourArray.length();i++){
                        JSONObject hourObj=hourArray.getJSONObject(i);
                        String time =hourObj.getString("time");
                        String temper =hourObj.getString("temp_c");
                        String img =hourObj.getJSONObject("condition").getString("icon");
                        String wind =hourObj.getString("wind_kph");
                        weatherRVModalArrayList.add(new WeatherRVModal(time,temper,img,wind));


                    }
                    weatherRVAdapter.notifyDataSetChanged();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(Meteo.this ,"please enter valide city name",Toast.LENGTH_SHORT).show();

            }
        });

        requestQueue.add(jsonObjectRequest);


}


}

