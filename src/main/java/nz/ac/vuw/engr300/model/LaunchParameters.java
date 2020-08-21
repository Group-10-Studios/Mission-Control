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
    public LaunchParameter<Double> maximumLaunchAngle = new LaunchParameter<>(-1d);
    public LaunchParameter<Double> maximumWindSpeed = new LaunchParameter<>(-1d);
    public LaunchParameter<Double> latitude = new LaunchParameter<>(-41.300442d);
    public LaunchParameter<Double> longitude = new LaunchParameter<>(174.780319d);

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
    }

    public static class LaunchParameter<T> {
        public boolean enabled = true;
        public T value;

        public LaunchParameter(T value) {
            this.value = value;
        }

        public LaunchParameter(boolean enabled, T value) {
            this.enabled = enabled;
            this.value = value;
        }
    }
}
