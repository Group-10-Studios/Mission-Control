package nz.ac.vuw.engr300.gui.model;

/**
 * GraphType is an Enum to provide representation for a panel in accessible
 * terms for the side panel. Each
 * 
 * @author Nathan Duckett
 */
public enum GraphType {
    VELOCITY, ALTITUDE, ACCELERATION, WINDDIRECTION;

    /**
     * Get the label to display within the side panel text for the graph type.
     * 
     * @return String label to represent the graph type.
     */
    public String getLabel() {
	switch (this) {
	case VELOCITY:
	    return "Velocity";
	case ACCELERATION:
	    return "Acceleration";
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
