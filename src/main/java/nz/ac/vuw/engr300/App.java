package nz.ac.vuw.engr300;

import org.apache.log4j.Logger;

public class App {
    private static final Logger logger = Logger.getLogger(App.class);

    public boolean getStatus() {
        logger.info("Getting status: True");
        return true;
    }

    public void printHello() {
        System.out.println("Hello World!");
    }

    public static void main(String[] args) {
        logger.info("Application Starting");
        new App().printHello();
    }
}
