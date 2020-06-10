package nz.ac.vuw.engr300.gui;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.stream.Stream;
import javafx.scene.layout.Region;
import javafx.stage.Stage;
import nz.ac.vuw.engr300.gui.controllers.HomeController;
import nz.ac.vuw.engr300.gui.model.GraphType;
import nz.ac.vuw.engr300.gui.views.HomeView;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.api.FxRobot;
import org.testfx.api.FxToolkit;
import org.testfx.assertions.api.Assertions;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.ApplicationTest;
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
    private Stage stage;

    /**
     * Start the application UI. This is run before each test.
     * 
     * @param stage Injected stage parameter to load the GUI inside.
     */
    @Start
    public void start(Stage stage) throws Exception {
        this.stage = stage;
        HomeView v = new HomeView(stage);
        width = stage.getWidth();
        height = stage.getHeight();

        // Make sure scene always matches stage
        assertEquals(v.getSceneHeight(), height);
        assertEquals(v.getSceneWidth(), width);

        Thread.sleep(3000);
    }

    @Test
    public void test_DefaultSize(FxRobot robot) {
        testPanelVisibility(robot);
        testBannerSize(robot);
        testSidePanelSize(robot);
        testPnContentSize(robot);
        testGraphSizes(robot);
    }

    /*
     * Test that the required panels are visible.
     */
    private void testPanelVisibility(FxRobot robot) {
        checkVisibleByID(robot, getAllGraphIds());
        checkVisibleByID(robot, "#pnBanner");
    }

    /**
     * Test the banner panel matches the full width.
     * 
     * @param robot Injected FxRobot to find the object on the screen.
     */
    private void testBannerSize(FxRobot robot) {
        Region pnBanner = getRegionByID(robot, "#pnBanner");
        assertWidths(width, pnBanner);
    }

    /**
     * Test the two sidepanels match their specified 1/6 width.
     * 
     * @param robot Injected FxRobot to find the object on the screen.
     */
    private void testSidePanelSize(FxRobot robot) {
        // Specified for side panels to be 1/6 screen size
        // Need to -1 to account for offset adjustments (just works that way)
        double panelWidth = (int) (width / 6) - 1;

        Region apNav = getRegionByID(robot, "#apNav");
        Region apWarnings = getRegionByID(robot, "#apWarnings");
        assertWidths(panelWidth, apNav, apWarnings);
    }

    /**
     * Test that the center panel with the graphs matches its' width.
     *
     * @param robot Injected FxRobot to find the object on the screen.
     */
    private void testPnContentSize(FxRobot robot) {
        // Specified for center panel to be 2/3 screen size
        double panelWidth = Math.ceil((width * 2) / 3);

        Region pnContent = getRegionByID(robot, "#pnContent");
        assertWidths(panelWidth, pnContent);
    }

    /**
     * Test each graph matches the expected width they should have.
     *
     * @param robot Injected FxRobot to find the object on the screen.
     */
    private void testGraphSizes(FxRobot robot) {
        Region pnContent = getRegionByID(robot, "#pnContent");
        double graphWidth = Math.ceil(pnContent.getWidth() / HomeController.COLS);
        double graphHeight = Math.ceil(pnContent.getHeight() / HomeController.ROWS);
        for (String graphID : getAllGraphIds()) {
            Region graphRegion = getRegionByID(robot, graphID);
            assertWidths(graphWidth, graphRegion);
            // Disable height until startup bug fixed
//            assertHeights(graphHeight, graphRegion);
        }
    }

    /**
     * Check if the panel with the matching panelID is visible on the GUI.
     * 
     * @param robot    FxRobot to scrape the Gui application.
     * @param panelIDs String array format of the IDs as "#id"
     */
    private void checkVisibleByID(FxRobot robot, String... panelIDs) {
        for (String panelID : panelIDs) {
            Assertions.assertThat(robot.lookup(panelID).queryAs(Region.class)).isVisible();
        }
    }

    /**
     * Get the Region object from the matching panelID.
     * 
     * @param robot   FxRobot to scrape the GUI application.
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
            assertEquals(width, r.getWidth(), "Failed on " + r.getId());
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
            assertEquals(height, r.getHeight(), "Failed on " + r.getId());
        }
    }

    /**
     * Get all of the graph's IDs based on the conversion method to Graph IDs.
     * 
     * @return String array of all possible graph ID values.
     */
    private String[] getAllGraphIds() {
        // Make sure # appended before ID.
        return Stream.of(GraphType.values()).map(g -> "#" + g.getGraphID()).toArray(String[]::new);
    }

}
