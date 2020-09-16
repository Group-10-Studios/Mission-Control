package nz.ac.vuw.engr300.gui.model;

/**
 * GraphType is an Enum to provide representation for a panel in accessible
 * terms for the side panel. Each panel uses the GraphType as a form of ID to
 * represent which graph it is in operations.
 *
 * @author Nathan Duckett
 */
public class GraphType {

    private final String label;
    private final String chartType;

    /**
     * Create a new GraphType for a respective graph. This contains the label provided for the graph and the expected
     * type of chart this graph will contain.
     *
     * @param label Label of the graph.
     * @param chartType Chart type of the graph.
     */
    public GraphType(String label, String chartType) {
        this.label = label;
        this.chartType = chartType;
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

    /**
     * Get the chart type of this graphtype. This can be used for type-checking for casting to various graphs.
     *
     * @return String containing the type of graph this matches from the list {@code line, angle, map}.
     */
    public String getChart() {
        return this.chartType;
    }
}
