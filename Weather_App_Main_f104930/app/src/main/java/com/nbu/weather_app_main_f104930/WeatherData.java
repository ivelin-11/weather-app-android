package com.nbu.weather_app_main_f104930;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class WeatherData implements Serializable {
    private String temperature;
    private String icon;
    private String city;
    private String weatherType;
    private int condition;

    public String getTemperature() {
        return temperature+"°C";
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getWeatherType() {
        return weatherType;
    }

    public void setWeatherType(String weatherType) {
        this.weatherType = weatherType;
    }

    public int getCondition() {
        return condition;
    }

    public void setCondition(int condition) {
        this.condition = condition;
    }
}
