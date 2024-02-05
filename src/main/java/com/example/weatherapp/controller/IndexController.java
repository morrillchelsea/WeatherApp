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
        // declare variables
        String cityName;
        String mainWeather;
        String icon; // variable to hold icon code
        double humidity; // humidity %
        int temp; // temperature in Fahrenheit
        int lo; // min temp of the day
        int hi; // max temp of the day
        String iconImg; // URL string to query img from API

        // Create instance of weather data class
        WeatherData wd = new WeatherData(null, null, null, 0.0, 0.0, 0.0, 0.0);
        
        // call method to query weather from API
        wd.getWeatherData(zipCode);

        cityName = wd.getCityName();
        icon = wd.getIcon(); 
        temp = (int) wd.getTemp(); // cast double type return value to int
        lo = (int) wd.getLo(); 
        hi = (int) wd.getHi(); 
        mainWeather = wd.getConditions(); // weather conditions (e.g. rain, clear, mist, ...)
        humidity = wd.getHumidity();
        
        // create url String with icon code to query API
        iconImg = "https://openweathermap.org/img/wn/" + icon + "@2x.png";

        // add models
        model.addAttribute("zipCode", zipCode);
        model.addAttribute("cityName", cityName);
        model.addAttribute("mainWeather", mainWeather);
        model.addAttribute("iconImg", iconImg);
        model.addAttribute("temp", temp);
        model.addAttribute("lo", lo);
        model.addAttribute("hi", hi);
        model.addAttribute("humidity", humidity);
        return "currentpage";
    }
}
