package nz.ac.vuw.engr300.gui.components;

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
}
