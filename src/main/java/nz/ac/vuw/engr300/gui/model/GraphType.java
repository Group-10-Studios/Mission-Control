package nz.ac.vuw.engr300.gui.model;

/**
 * GraphType is an Enum to provide representation for a panel in accessible
 * terms for the side panel. Each panel uses the GraphType as a form of ID to
 * represent which graph it is in operations.
 *
 * @author Nathan Duckett
 */
public class GraphType {

    private String label;

    public GraphType(String label) {
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
     * Get a generated GraphID based on this Graph's label.
     * 
     * @return String ID representation of the graph.
     */
    public String getGraphID() {
        return "graph" + this.getLabel().replace(" ", "");
    }
}
