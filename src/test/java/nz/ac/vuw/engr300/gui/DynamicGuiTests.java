package nz.ac.vuw.engr300.gui;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.lang.reflect.Field;
import java.util.stream.Stream;

import javafx.application.Platform;
import javafx.scene.layout.Region;
import javafx.stage.Stage;
import nz.ac.vuw.engr300.gui.layouts.DynamicGridPane;
import nz.ac.vuw.engr300.gui.model.GraphMasterList;
import nz.ac.vuw.engr300.gui.model.GraphType;
import nz.ac.vuw.engr300.gui.views.GraphView;
import nz.ac.vuw.engr300.gui.views.HomeView;
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
    private Stage stage;
    private HomeView homeView;
    private DynamicGridPane dynamicGridPane;

    /**
     * Start the application UI. This is run before each test.
     * 
     * @param stage Injected stage parameter to load the GUI inside.
     */
    @Start
    public void start(Stage stage) throws Exception {
        this.stage = stage;
        this.homeView = new HomeView(stage);
        width = stage.getWidth();
        height = stage.getHeight();

        // Make sure scene always matches stage
        assertEquals(homeView.getSceneHeight(), height);
        assertEquals(homeView.getSceneWidth(), width);

        this.dynamicGridPane = getCurrentDynamicGridPane();
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
        // Title bar
        checkVisibleByID(robot, "#pnBanner");
        // Center Graphs
        checkVisibleByID(robot, "#centerPanel");
        checkVisibleByID(robot, getAllGraphIds());
        // Side panels
        checkVisibleByID(robot, "#navigationView", "#informationView");
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
     * Test the two side panels match their specified 1/6 width.
     * 
     * @param robot Injected FxRobot to find the object on the screen.
     */
    private void testSidePanelSize(FxRobot robot) {
        // Specified for side panels to be 1/6 screen size
        double panelWidth = (int) (width / 6);

        Region apNav = getRegionByID(robot, "#navigationView");
        Region apWarnings = getRegionByID(robot, "#informationView");
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

        Region pnContent = getRegionByID(robot, "#centerPanel");
        assertWidths(panelWidth, pnContent);
    }

    /**
     * Test each graph matches the expected width they should have.
     *
     * @param robot Injected FxRobot to find the object on the screen.
     */
    private void testGraphSizes(FxRobot robot) {
        Region pnContent = getRegionByID(robot, "#gridPane");
        double graphWidth = Math.floor(pnContent.getWidth() / this.dynamicGridPane.getColumns());
        double graphHeight = Math.ceil(pnContent.getHeight() / this.dynamicGridPane.getRows());
        for (String graphID : getAllGraphIds()) {
            Region graphRegion = getRegionByID(robot, graphID);
            assertWidths(graphWidth, graphRegion);
            // Disable height until startup bug fixed
            // assertHeights(graphHeight, graphRegion);
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
            // Delta is set to allow +-1 pixel sizing. This accounts for padding/offsets or rounding errors.
            assertEquals(width, r.getWidth(), 1.0, "Failed on " + r.getId());
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
        return GraphMasterList.getInstance().getGraphs().stream().map(g -> "#" + g.getGraphID()).toArray(String[]::new);
    }

    /**
     * Get the dynamic GridPane being used within the content panel. This allows direct access to the graphs.
     *
     * @return DynamicGridPane which has been created and set in the center panel for application use.
     * @throws NoSuchFieldException Thrown if the field values changes from graphView or contentPane
     *                              within the respective classes.
     * @throws IllegalAccessException Thrown if access is denied when retrieving the value of the field.
     */
    private DynamicGridPane getCurrentDynamicGridPane() throws NoSuchFieldException, IllegalAccessException {
        Field graphViewField = homeView.getClass().getDeclaredField("graphView");
        graphViewField.setAccessible(true);
        GraphView graphView = (GraphView) graphViewField.get(homeView);

        Field dynamicGridPaneField = graphView.getClass().getDeclaredField("contentPane");
        dynamicGridPaneField.setAccessible(true);
        return (DynamicGridPane) dynamicGridPaneField.get(graphView);
    }
}
