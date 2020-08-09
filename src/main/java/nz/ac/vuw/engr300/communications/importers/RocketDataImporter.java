package nz.ac.vuw.engr300.communications.importers;

import java.util.function.Consumer;
import nz.ac.vuw.engr300.communications.model.RocketData;

/**
 * Temporary template interface for a dummy data importer. In the future this
 * interface could transform into the standard we use for defining communication
 * modules with the Rocket.
 */
public interface RocketDataImporter {

    /**
     * Subscribes a client to the data importer, the consumer is called everytime
     * data is received from the "dummy rocket".
     *
     * @param observer The callback for the client
     */
    void subscribeObserver(Consumer<RocketData> observer);

    /**
     * Unsubscribes a client from the data importer.
     *
     * @param observer The client callback to unsubscribe
     */
    void unsubscribeObserver(Consumer<RocketData> observer);

    /**
     * Unsubscribe all observers from the data importer.
     */
    void unsubscribeAllObservers();
}
