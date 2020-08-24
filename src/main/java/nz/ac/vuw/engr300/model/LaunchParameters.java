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
    private final LaunchParameter<Double> maximumLaunchAngle = new LaunchParameter<>(-1d, Double.class);
    private final LaunchParameter<Double> maximumWindSpeed = new LaunchParameter<>(-1d, Double.class);
    private final LaunchParameter<Double> latitude = new LaunchParameter<>(-41.300442d, Double.class);
    private final LaunchParameter<Double> longitude = new LaunchParameter<>(174.780319d, Double.class);
    private final LaunchParameter<Double> maximumGroundHitSpeed = new LaunchParameter<>(-1d, Double.class);
    private final LaunchParameter<Double> maximumAngleOfAttack = new LaunchParameter<>(-1d, Double.class);
    private final LaunchParameter<Double> maximumParachuteDeploySpeed = new LaunchParameter<>(-1d, Double.class);

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
     * Default constructor for LaunchParameters.
     */
    private LaunchParameters() {
    }

    /**
     * Gets the maximum launch angle launch parameter object.
     *
     * @return  The maximum launch angle launch parameter object.
     */
    public LaunchParameter<Double> getMaximumLaunchAngle() {
        return maximumLaunchAngle;
    }

    /**
     * Gets the maximum wind speed launch parameter object.
     *
     * @return  The maximum wind speed launch parameter object.
     */
    public LaunchParameter<Double> getMaximumWindSpeed() {
        return maximumWindSpeed;
    }

    /**
     * Gets the latitude of the launch site launch parameter object.
     *
     * @return  The latitude of the launch site launch parameter object.
     */
    public LaunchParameter<Double> getLatitude() {
        return latitude;
    }

    /**
     * Gets the longitude of the launch site launch parameter object.
     *
     * @return The longitude of the launch site parameter object.
     */
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

        public LaunchParameter(T value, Class<T> type) {
            this(true, value, type);
        }

        /**
         * Constructs a LaunchParameter object.
         *
         * @param enabled   Whether or not this parameter is enabled.
         * @param value     The value of the launch parameter.
         * @param type      The datatype of the launch parameter.
         */
        public LaunchParameter(boolean enabled, T value, Class<T> type) {
            this.enabled = enabled;
            this.value = value;
            this.type = type.getSimpleName().toLowerCase();

            if (!(this.type.equals("double")
                    || this.type.equals("integer")
                    || this.type.equals("boolean")
                    || this.type.equals("string"))) {
                throw new RuntimeException("Invalid datatype!");
            }
        }

        /**
         * Gets whether or not this launch parameter is enabled.
         *
         * @return  Whether or not this launch parameter is enabled.
         */
        public boolean isEnabled() {
            return enabled;
        }

        /**
         * Gets the value of this launch parameter.
         *
         * @return  The value of this launch parameter.
         */
        public T getValue() {
            return value;
        }

        /**
         * Gets the type, represented by a string, of this launch parameter.
         *
         * @return The type, represented by a string, of this launch parameter.
         */
        public String getType() {
            return type;
        }

        /**
         * Sets whether or not this launch parameter is enabled or not.
         *
         * @param enabled  The value to set the enabled field to.
         */
        public void setEnabled(boolean enabled) {
            this.enabled = enabled;
        }

        /**
         * Sets the value of this launch parameter.
         *
         * @param value The value to set the launch parameter.
         */
        public void setValue(T value) {
            this.value = value;
        }
    }
}
