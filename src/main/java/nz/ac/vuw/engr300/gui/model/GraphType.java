package nz.ac.vuw.engr300.gui.model;

/**
 * GraphType is an Enum to provide representation for a panel in accessible
 * terms for the side panel. Each
 * 
 * @author Nathan Duckett
 */
public enum GraphType {
    VELOCITY, ACCELERATION, ALTITUDE;

    /**
     * Get the label to display within the side panel text for the graph type.
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
      	default:
      	    return "Invalid GraphType";
	}
    }
}
