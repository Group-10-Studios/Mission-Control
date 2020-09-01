package nz.ac.vuw.engr300.gui.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * GraphMasterList is a storage class to record the current graphs which are on the display. This
 * is mainly an interface to provide the buttons which will be created within the navigation panel.
 *
 * @author Nathan Duckett
 */
public class GraphMasterList {

    private static final GraphMasterList graphMasterList = new GraphMasterList();
    private final List<GraphType> graphs;

    /**
     * Create a new GraphMasterList instance for the beginning of the application.
     */
    private GraphMasterList() {
        graphs = new ArrayList<>();
    }

    /**
     * Get the instance of the graph master list.
     *
     * @return GraphMasterList for this application.
     */
    public static GraphMasterList getInstance() {
        return graphMasterList;
    }

    /**
     * Register a new graph to the master list.
     *
     * @param graphType GraphType to be registered name of the graph.
     */
    public void registerGraph(GraphType graphType) {
        this.graphs.add(graphType);
    }

    /**
     * Unregister a graph from the master list.
     *
     * @param graphType GraphType to be unregistered.
     */
    public void unRegisterGraph(GraphType graphType) {
        this.graphs.remove(graphType);
    }

    /**
     * Unregister all graphs from the master list.
     */
    public void clearRegisteredGraphs() {
        this.graphs.clear();
    }

    /**
     * Swaps the two graphs based on their indexes.
     * @param index1 The graph to swap.
     * @param index2 The other graph to swap with.
     */
    public void swapGraphs(int index1, int index2) {
        Collections.swap(graphs, index1, index2);
    }

    /**
     * Get a list of all the GraphType's registered to this master list.
     *
     * @return List of GraphType.
     */
    public List<GraphType> getGraphs() {
        return Collections.unmodifiableList(graphs);
    }
}
