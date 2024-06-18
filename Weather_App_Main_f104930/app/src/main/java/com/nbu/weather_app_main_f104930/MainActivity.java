package com.nbu.weather_app_main_f104930;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;


public class MainActivity extends AppCompatActivity {
    private DatabaseHelper dbHelper;

    TextView nameOfCity, weatherState, temperature;
    ImageView weatherIcon;

    RelativeLayout cityFinder;

    LocationManager locationManager;
    LocationListener locationListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        weatherState = findViewById(R.id.weatherCondition);
        temperature = findViewById(R.id.temperature);
        weatherIcon = findViewById(R.id.weatherIcon);
        cityFinder = findViewById(R.id.cityFinder);
        nameOfCity = findViewById(R.id.cityName);

        dbHelper = new DatabaseHelper(this);

        cityFinder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, CityFinder.class);
                startActivity(intent);
            }
        });
    }


    @Override
    protected void onResume() {
        super.onResume();
        Intent mIntent = getIntent();
        if (mIntent != null) {
            String city = mIntent.getStringExtra(Constants.CITY);
            if (city != null) {
                SQLiteDatabase database = dbHelper.getWritableDatabase();
                dbHelper.insertCity(city);
                database.close();
                getWeatherForNewCity(city);
            } else {
                getWeatherForCurrentLocation();
            }
        }
    }


    private void getWeatherForNewCity(String city) {
        RequestParams params = new RequestParams();
        params.put(Constants.QUERY, city);
        params.put(Constants.APP_ID_VALUE, Constants.APP_ID);
        getWeather(params);
    }


    private void getWeatherForCurrentLocation() {

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locationListener =new LocationListener() {
            @Override
            public void onLocationChanged(@NonNull Location location) {

                String Latitude = String.valueOf(location.getLatitude());
                String Longitude = String.valueOf(location.getLongitude());

                RequestParams params = new RequestParams();
                params.put(Constants.LATITUDE, Latitude);
                params.put(Constants.LONGITUDE, Longitude);
                params.put(Constants.APP_ID_VALUE, Constants.APP_ID);
                getWeather(params);

            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {
            }

            @Override
            public void onProviderEnabled(String provider) {
            }

            @Override
            public void onProviderDisabled(String provider) {
            }
        };


        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    Constants.REQUEST_CODE);
            return;
        }

        LocationController.requestLocationUpdate(locationManager,locationListener);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == Constants.REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(MainActivity.this, "Location get succesfully", Toast.LENGTH_SHORT).show();
                getWeatherForCurrentLocation();
            }
        }
    }


    private void getWeather(RequestParams params) {
                AsyncHttpClient client = new AsyncHttpClient();
                client.get(Constants.WEATHER_URL, params, new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        Toast.makeText(MainActivity.this, "Data Get Success", Toast.LENGTH_SHORT).show();
                        WeatherData weatherD = WeatherController.fromJson(response);
                        updateUI(weatherD);
                        super.onSuccess(statusCode, headers, response);
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                        Toast.makeText(MainActivity.this, "Data Get Failed", Toast.LENGTH_SHORT).show();
                        super.onFailure(statusCode, headers, throwable, errorResponse);
                    }
                });
    }

    private void updateUI(WeatherData weather) {
        temperature.setText(weather.getTemperature());
        nameOfCity.setText(weather.getCity());
        weatherState.setText(weather.getWeatherType());
        int resourceID = getResources().getIdentifier(weather.getIcon(), "drawable", getPackageName());
        weatherIcon.setImageResource(resourceID);
    }

    @Override
    protected void onPause() {
        super.onPause();
        LocationController.removeUpdates(locationManager,locationListener);
    }
}
