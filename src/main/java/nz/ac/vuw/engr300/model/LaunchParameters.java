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
    private final LaunchParameter<Double> maximumLaunchAngle = new LaunchParameter<>(-1d, "double");
    private final LaunchParameter<Double> maximumWindSpeed = new LaunchParameter<>(-1d, "double");
    private final LaunchParameter<Double> latitude = new LaunchParameter<>(-41.300442d, "double");
    private final LaunchParameter<Double> longitude = new LaunchParameter<>(174.780319d, "double");

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

    public LaunchParameter<Double> getMaximumWindSpeed() {
        return maximumWindSpeed;
    }

    public LaunchParameter<Double> getLatitude() {
        return latitude;
    }

    public LaunchParameter<Double> getLongitude() {
        return longitude;
    }

    /**
     * The class that represents an actual launch parameter.
     *
     * @param <T>   The type of launch parameter. Note, only double, integer, boolean and string is currently
     *           supported.
     */
    public static class LaunchParameter<T> {
        private boolean enabled;
        private T value;
        private final String type;

        public LaunchParameter(T value, String type) {
            this(true, value, type);
        }

        /**
         * Constructs a LaunchParameter object.
         *
         * @param enabled   Whether or not this parameter is enabled.
         * @param value     The value of the launch parameter.
         * @param type      The datatype of the launch parameter.
         */
        public LaunchParameter(boolean enabled, T value, String type) {
            this.enabled = enabled;
            this.value = value;
            this.type = type.toLowerCase();

            if (!(type.equals("double")
                    || type.equals("integer")
                    || type.equals("boolean")
                    || type.equals("string"))) {
                throw new RuntimeException("Invalid datatype!");
            }
        }

        public boolean isEnabled() {
            return enabled;
        }

        public T getValue() {
            return value;
        }

        public String getType() {
            return type;
        }

        public void setEnabled(boolean enabled) {
            this.enabled = enabled;
        }

        public void setValue(T value) {
            this.value = value;
        }
    }
}
