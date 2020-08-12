package nz.ac.vuw.engr300.gui.controllers;

import com.google.gson.Gson;
import nz.ac.vuw.engr300.importers.JsonImporter;
import nz.ac.vuw.engr300.importers.KeyImporter;
import nz.ac.vuw.engr300.importers.MapImageImporter;
import nz.ac.vuw.engr300.model.LaunchParameters;
import nz.ac.vuw.engr300.weather.importers.WeatherImporter;

import java.io.FileNotFoundException;

public class HomeController {

    private final static HomeController instance = new HomeController();

    public static HomeController getInstance() {
        return instance;
    }

    private HomeController(){
        this.loadLaunchConfiguration();
    }

    private void loadLaunchConfiguration() {
        LaunchParameters parameters;
        try {
            Gson gson = new Gson();
            parameters = gson.fromJson(JsonImporter.load("src/main/resources/config/launch-parameters.json"), LaunchParameters.class);
        } catch (FileNotFoundException e) {
            parameters = new LaunchParameters();
        }

    }

}