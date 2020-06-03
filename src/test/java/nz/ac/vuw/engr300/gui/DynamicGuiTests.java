package nz.ac.vuw.engr300.gui;

import javafx.scene.layout.Region;
import javafx.stage.Stage;
import nz.ac.vuw.engr300.gui.components.RocketDataAngle;
import nz.ac.vuw.engr300.gui.components.RocketDataLineChart;
import nz.ac.vuw.engr300.gui.views.HomeView;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.api.FxRobot;
import org.testfx.assertions.api.Assertions;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;

/**
 * GUI tests to verify that our Dynamic UI is working as expected.
 * 
 * @author Nathan Duckett
 *
 */
@ExtendWith(ApplicationExtension.class)
class DynamicGuiTests {
    private static double width;
    private static double height;

    /**
     * Start the application UI. This is run before each test.
     * 
     * @param stage Injected stage parameter to load the GUI inside.
     */
    @Start
    private static void start(Stage stage) throws Exception {
        new HomeView(stage);
        width = stage.getWidth();
        height = stage.getHeight();
    }

    /*
     * Test that the required panels are visible.
     */
    @Test
    void test_panel_visibility(FxRobot robot) {
        checkVisibleByID(robot, "#lineChartTotalVelocity");
        checkVisibleByID(robot, "#lineChartTotalAcceleration");
        checkVisibleByID(robot, "#lineChartAltitude");
        checkVisibleByID(robot, "#compassWindDirection");
        checkVisibleByID(robot, "#pnBanner");
    }

    // /**
    //  * Test the banner panel matches the full width.
    //  * 
    //  * @param robot Injected FxRobot to find the object on the screen.
    //  */
    // @Test
    // void test_banner_panel(FxRobot robot) {
    //     Region pnBanner = getRegionByID(robot, "#pnBanner");
    //     assertWidths(width, pnBanner);
    // }

    // /**
    //  * Test the two sidepanels match their speicified 1/6 width.
    //  * 
    //  * @param robot Injected FxRobot to find the object on the screen.
    //  */
    // @Test
    // void test_sidepanels(FxRobot robot) {
    //     // Specified for side panels to be 1/6 screen size
    //     int panelWidth = (int) (width / 6);

    //     Region apNav = getRegionByID(robot, "#apNav");
    //     Region apWarnings = getRegionByID(robot, "#apWarnings");
    //     assertWidths(panelWidth, apNav, apWarnings);
    // }

//    /**
//     * Test that the centre panel with the graphs matches its' width.
//     * 
//     * @param robot Injected FxRobot to find the object on the screen.
//     */
//    @Test
//    void test_centre_panel(FxRobot robot) {
//        // Specified for centre panel to be 2/3 screen size
//        double panelWidth = ((width * 2) / 3);
//
//        Region pnContent = getRegionByID(robot, "#pnContent");
//        assertWidths(panelWidth, pnContent);
//    }

//    /**
//     * Test each graph matches the expected width they should have.
//     * 
//     * @param robot Injected FxRobot to find the object on the screen.
//     */
//    @Test
//    void test_graph_sizes(FxRobot robot) {
//        Region pnContent = getRegionByID(robot, "#pnContent");
//        double graphWidth = ((pnContent.getWidth() - 10) / 2) - 10;
//        Region velChart = getRegionByID(robot, "#lineChartVel");
//        Region accChart = getRegionByID(robot, "#lineChartAcceleration");
//        Region altChart = getRegionByID(robot, "#lineChartAltitude");
//        Region windCompass = getRegionByID(robot, "#windCompass");
//
//        assertWidths(graphWidth, velChart, accChart, altChart, windCompass);
//    }

    /**
     * Check if the panel with the matching panelID is visible on the Gui.
     * 
     * @param robot   FxRobot to scrape the Gui application.
     * @param panelID String format of the ID as "#id"
     */
    private void checkVisibleByID(FxRobot robot, String panelID) {
        Assertions.assertThat(robot.lookup(panelID).queryAs(Region.class)).isVisible();
    }

    /**
     * Get the Region object from the matching panelID.
     * 
     * @param robot   FxRobot to scrape the Gui application.
     * @param panelID String format of the ID as "#id"
     */
    private Region getRegionByID(FxRobot robot, String panelID) {
        return robot.lookup(panelID).queryAs(Region.class);
    }

    /**
     * Assert that all of the regions are at the specified width.
     * 
     * @param width   Width in double format which this region should match.
     * @param regions Array of regions to be tested against this width.
     */
    private void assertWidths(double width, Region... regions) {
        for (Region r : regions) {
            assertEquals(width, r.getWidth());
        }
    }

    /**
     * Assert that all of the regions are at the specified height.
     * 
     * @param height  Height in double format which this region should match.
     * @param regions Array of regions to be tested against this height.
     */
    private void assertHeights(double height, Region... regions) {
        for (Region r : regions) {
            assertEquals(height, r.getHeight());
        }
    }

}
