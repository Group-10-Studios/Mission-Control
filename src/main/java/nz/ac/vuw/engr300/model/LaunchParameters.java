package nz.ac.vuw.engr300.model;

import com.google.gson.Gson;
import nz.ac.vuw.engr300.importers.JsonImporter;
import java.io.FileNotFoundException;

/**
 * This class represents the different Launch Parameters, this is a global state.
 *
 * @author Ahad Rahman, Tim Salisbury
 */
public class LaunchParameters {
    public double maximumLaunchAngle;
    public double maximumWindSpeed;
    public double latitude;
    public double longitude;
    public int testInteger;
    public String testString;

    private static LaunchParameters instance;

    /**
     * Get the LaunchParameters object.
     *
     * @return LaunchParameters instance for the global state.
     */
    public static LaunchParameters getInstance() {
        if (instance == null) {
            try {
                Gson gson = new Gson();
                instance = gson.fromJson(JsonImporter.load("src/main/resources/config/launch-parameters.json"),
                        LaunchParameters.class);
            } catch (FileNotFoundException e) {
                instance = new LaunchParameters();
            }
        }

        return instance;
    }

    /**
     * Default values are set when the LaunchParameters object is created.
     */
    private LaunchParameters() {
        this.maximumLaunchAngle = -1;
        this.maximumWindSpeed = -1;
        this.latitude = -41.300442;
        this.longitude = 174.780319;
    }
}
