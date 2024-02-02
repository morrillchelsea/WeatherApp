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
        String icon;
        double humidity;
        double temp;
        String iconImg;

        // Create instance of weather data class
        WeatherData wd = new WeatherData(null, null, null, 0.0, 0.0);
        
        wd.getWeatherData(zipCode);

        // declare variables
        cityName = wd.getCityName();
        icon = wd.getIcon();
        temp = wd.getTemp();
        mainWeather = wd.getConditions();
        humidity = wd.getHumidity();

        iconImg = "https://openweathermap.org/img/wn/" + icon + "@2x.png";

        System.out.println(iconImg);

        // add models
        model.addAttribute("zipCode", zipCode);
        model.addAttribute("cityName", cityName);
        model.addAttribute("mainWeather", mainWeather);
        model.addAttribute("iconImg", iconImg);
        model.addAttribute("temp", temp);
        model.addAttribute("humidity", humidity);
        return "currentpage";
    }
}