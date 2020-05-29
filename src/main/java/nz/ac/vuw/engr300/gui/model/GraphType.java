package nz.ac.vuw.engr300.gui.model;

/**
 * GraphType is an Enum to provide representation for a panel in accessible
 * terms for the side panel. Each
 * 
 * @author Nathan Duckett
 */
public enum GraphType {
    TOTAL_VELOCITY, X_VELOCITY, Y_VELOCITY, Z_VELOCITY, ALTITUDE, TOTAL_ACCELERATION, X_ACCELERATION, Y_ACCELERATION, Z_ACCELERATION, WINDDIRECTION;

    /**
     * Get the label to display within the side panel text for the graph type.
     * 
     * @return String label to represent the graph type.
     */
    public String getLabel() {
        switch (this) {
            case TOTAL_VELOCITY:
                return "Velocity";
            case X_VELOCITY:
                return "X Velocity";
            case Y_VELOCITY:
                return "Y Velocity";
            case Z_VELOCITY:
                return "Z Velocity";
            case TOTAL_ACCELERATION:
                return "Acceleration";
            case X_ACCELERATION:
                return "X Acceleration";
            case Y_ACCELERATION:
                return "Y Acceleration";
            case Z_ACCELERATION:
                return "Z Acceleration";
            case ALTITUDE:
                return "Altitude";
            case WINDDIRECTION:
                return "Wind Direction";
            default:
                return "Invalid GraphType";
        }
    }

    /**
     * Get a GraphType based on the label provided from the application. This is
     * used for comparing an onClick listener to the graphs for highlighting.
     * 
     * @param label String label in the button generated above.
     * @return GraphType which matches the value of the label.
     */
    public static GraphType fromLabel(String label) {
        switch (label) {
            case "Velocity":
                return VELOCITY;
            case "Acceleration":
                return ACCELERATION;
            case "Altitude":
                return ALTITUDE;
            case "Wind Direction":
                return WINDDIRECTION;
            default:
                return null;
        }
    }
}
