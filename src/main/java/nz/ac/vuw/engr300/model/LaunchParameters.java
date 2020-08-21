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
    private LaunchParameter<Double> maximumLaunchAngle = new LaunchParameter<>(-1d, Double.class);
    private LaunchParameter<Double> maximumWindSpeed = new LaunchParameter<>(-1d, Double.class);
    private LaunchParameter<Double> latitude = new LaunchParameter<>(-41.300442d, Double.class);
    private LaunchParameter<Double> longitude = new LaunchParameter<>(174.780319d, Double.class);

    private static transient LaunchParameters instance;

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

    public LaunchParameter<Double> getMaximumLaunchAngle() {
        return maximumLaunchAngle;
    }

    public void setMaximumLaunchAngle(LaunchParameter<Double> maximumLaunchAngle) {
        this.maximumLaunchAngle = maximumLaunchAngle;
    }

    public LaunchParameter<Double> getMaximumWindSpeed() {
        return maximumWindSpeed;
    }

    public void setMaximumWindSpeed(LaunchParameter<Double> maximumWindSpeed) {
        this.maximumWindSpeed = maximumWindSpeed;
    }

    public LaunchParameter<Double> getLatitude() {
        return latitude;
    }

    public void setLatitude(LaunchParameter<Double> latitude) {
        this.latitude = latitude;
    }

    public LaunchParameter<Double> getLongitude() {
        return longitude;
    }

    public void setLongitude(LaunchParameter<Double> longitude) {
        this.longitude = longitude;
    }

    public static void setInstance(LaunchParameters instance) {
        LaunchParameters.instance = instance;
    }

    public static class LaunchParameter<T> {
        private final boolean enabled;
        private final T value;
        private final transient Class<T> type;

        public LaunchParameter(T value, Class<T> type) {
            this(true, value, type);
        }

        public LaunchParameter(boolean enabled, T value, Class<T> type) {
            this.enabled = enabled;
            this.value = value;
            this.type = type;

            if (!(type.getSimpleName().equals("Double")
                    || type.getName().equals("Integer")
                    || type.getName().equals("Boolean")
                    || type.getName().equals("String"))) {
                throw new RuntimeException("Invalid datatype!");
            }
        }

        public boolean isEnabled() {
            return enabled;
        }

        public T getValue() {
            return value;
        }

        public Class<T> getType() {
            return type;
        }
    }
}
