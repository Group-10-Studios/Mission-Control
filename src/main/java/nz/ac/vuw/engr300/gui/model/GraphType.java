package nz.ac.vuw.engr300.gui.model;

/**
 * GraphType is an Enum to provide representation for a panel in accessible
 * terms for the side panel. Each
 *
 * @author Nathan Duckett
 */
public enum GraphType {
    TOTAL_VELOCITY("Total Velocity"),
    X_VELOCITY("X Velocity"),
    Y_VELOCITY("Y Velocity"),
    Z_VELOCITY("Z Velocity"),
    TOTAL_ACCELERATION("Total Acceleration"),
    X_ACCELERATION("X Acceleration"),
    Y_ACCELERATION("Y Acceleration"),
    Z_ACCELERATION("Z Acceleration"),
    ALTITUDE("Altitude"),
    WINDDIRECTION("Wind Direction");


    private String label;

    GraphType(String label) {
        this.label = label;
    }

    /**
     * Get the label to display within the side panel text for the graph type.
     *
     * @return String label to represent the graph type.
     */
    public String getLabel() {
        return this.label;
    }

    /**
     * Get a GraphType based on the label provided from the application. This is
     * used for comparing an onClick listener to the graphs for highlighting.
     *
     * @param label String label in the button generated above.
     * @return GraphType which matches the value of the label.
     */
    public static GraphType fromLabel(String label) {
        for (GraphType type : values()) {
            if (type.label.equals(label)) {
                return type;
            }
        }

        return null;
    }
}
