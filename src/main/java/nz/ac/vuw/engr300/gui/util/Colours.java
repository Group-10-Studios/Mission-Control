package nz.ac.vuw.engr300.gui.util;

import javafx.scene.paint.Color;

public class Colours {
    public static final Color PRIMARY_COLOUR = Color.valueOf("#4267B2");
    public static final Color BACKGROUND_COLOUR = Color.valueOf("#F6F6F6");
    public static final Color TEXT_COLOUR = Color.valueOf("#FFFFFF");
    public static final Color SHADE_COLOUR = Color.valueOf("#E0E0E0");
    public static final Color CONTENT_BACKGROUND_COLOUR = Color.valueOf("#EEEEEE");

    public static final Color WARNING_INFO = Color.valueOf("#4267B2");
    public static final Color WARNING_WARN = Color.ORANGE;
    public static final Color WARNING_ERROR = Color.RED;

    /**
     * Formats an integer to a hex string.
     *
     * @param val   The integer to format.
     * @return      The formatted string.
     */
    private static String format(double val) {
        String in = Integer.toHexString((int) Math.round(val * 255));
        return in.length() == 1 ? "0" + in : in;
    }

    /**
     * Converts a colour to a hex string.
     *
     * @param value     The colour to convert.
     * @return          The hex string to colour.
     */
    public static String toHexString(Color value) {
        return "#" + (format(value.getRed()) + format(value.getGreen()) + format(value.getBlue())
                + format(value.getOpacity()))
                .toUpperCase();
    }
}
