package com.nbu.weather_app_main_f104930;

import org.json.JSONException;
import org.json.JSONObject;

public class WeatherController {

    public static WeatherData fromJson(JSONObject jsonObject) {

        try {
            WeatherData weatherData = new WeatherData();
            weatherData.setCity(jsonObject.getString("name"));
            weatherData.setCondition(jsonObject.getJSONArray("weather").getJSONObject(0).getInt("id"));
            weatherData.setWeatherType(jsonObject.getJSONArray("weather").getJSONObject(0).getString("main"));
            weatherData.setIcon(updateWeatherIcon(weatherData.getCondition()));

            double tempResult = jsonObject.getJSONObject("main").getDouble("temp") - 273.15;
            int roundedValue = (int) Math.rint(tempResult);
            weatherData.setTemperature(String.valueOf(roundedValue));
            return  weatherData;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static String updateWeatherIcon(int condition) {
        if (condition >= 0 && condition <= 300) {
            return "thunderstrom1";
        } else if (condition >= 300 && condition <= 500) {
            return "lightrain";
        } else if (condition >= 500 && condition <= 600) {
            return "shower";
        } else if (condition >= 600 && condition <= 700) {
            return "snow2";
        } else if (condition >= 701 && condition <= 771) {
            return "fog";
        } else if (condition >= 772 && condition <= 800) {
            return "overcast";
        } else if (condition == 800) {
            return "sunny";
        } else if (condition >= 801 && condition <= 804) {
            return "cloudy";
        } else if (condition >= 900 && condition <= 902) {
            return "thunderstrom1";
        }
        if (condition == 903) {
            return "snow1";
        }
        if (condition == 904) {
            return "sunny";
        }
        if (condition >= 905 && condition <= 1000) {
            return "thunderstrom2";
        }

        return "dunno";


    }
}
