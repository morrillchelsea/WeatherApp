package com.example.weatherapp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.weatherapp.models.WeatherData;

@Controller
public class IndexController {

    @GetMapping("/")
    public String home() {
        return "homepage";
    }

    @PostMapping("/current")
    public String outputWeather (int zipCode, Model model) {
        String cityName;
        String mainWeather;
        double humidity;
        double temp;

        // Create instance of weather data class
        WeatherData wd = new WeatherData(null, null, 0.0, 0.0);
        
        wd.getWeatherData(zipCode);

        // declare variables
        cityName = wd.getCityName();
        temp = wd.getTemp();
        mainWeather = wd.getConditions();
        humidity = wd.getHumidity();

        // add models
        model.addAttribute("zipcode", zipCode);
        model.addAttribute("cityName", cityName);
        model.addAttribute("mainWeather", mainWeather);
        model.addAttribute("temp", temp);
        model.addAttribute("humidity", humidity);
        return "currentpage";
    }
}
