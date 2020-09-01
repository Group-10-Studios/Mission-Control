package nz.ac.vuw.engr300.gui.components;

import nz.ac.vuw.engr300.gui.controllers.GraphController;
import nz.ac.vuw.engr300.gui.model.GraphType;

/**
 * Interface to parent our Mission Control graph components. Provides necessary
 * function to allow finding by type and organizing the graphs.
 * 
 * @author Nathan Duckett
 *
 */
public interface RocketGraph {
    /**
     * Set the underlying type of this graph for matching against the side panel.
     * 
     * @param g GraphType to match this graph against.
     */
    public void setGraphType(GraphType g);

    /**
     * Get the underlying type of this graph.
     * 
     * @return GraphType which this graph is classified as.
     */
    public GraphType getGraphType();
    
    /**
     * Clears the current graph, and resets the X scale.
     */
    public void clear();

    /**
     * Get the status of this RocketGraph if it is visible or hidden.
     *
     * @return Boolean indicating if the graph is visible on the content grid.
     */
    public boolean isGraphVisible();

    /**
     * Switch the visibility status from visible-hidden and vice versa.
     */
    public void toggleVisibility();

    /**
     * Default double click callback which will open a pop out graph for this graph.
     */
    default void doubleClickCallback() {
        GraphController.getInstance().popOutGraph(this);
    }
}
