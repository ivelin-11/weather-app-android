package com.nbu.weather_app_main_f104930;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class CityFinder extends Activity {

    private DatabaseHelper dbHelper;
    private ListView cityListView;
    private ArrayAdapter<String> cityAdapter;
    private ArrayList<String> cityList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city_finder);
        final EditText editText = findViewById(R.id.searchCity);
        ImageView backButton = findViewById(R.id.backButton);

        cityListView = findViewById(R.id.cityListView);
        cityListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selectedCity = cityList.get(position);
                editText.setText(selectedCity);
            }
        });

        cityList = new ArrayList<>();

        dbHelper = new DatabaseHelper(this);

        SQLiteDatabase database = dbHelper.getWritableDatabase();
        loadCities();
        database.close();

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        editText.setOnEditorActionListener((v, actionId, event) -> {
            String newCity = editText.getText().toString();
            Intent intent = new Intent(CityFinder.this, MainActivity.class);
            intent.putExtra(Constants.CITY, newCity);
            startActivity(intent);
            finish();
            return false;
        });
    }



    private void loadCities() {
        cityList.clear();
        Cursor cursor = dbHelper.getAllCities();
        if (cursor.moveToFirst()) {
            do {
                @SuppressLint("Range")
                String city = cursor.getString(cursor.getColumnIndex("city"));
                cityList.add(city);
            } while (cursor.moveToNext());
        }

        cursor.close();
        cityList = new ArrayList<>(new HashSet<String>(cityList));

        cityAdapter = new ArrayAdapter<>(this,
                R.layout.list_cities,
                cityList);
        cityListView.setAdapter(cityAdapter);
    }
}