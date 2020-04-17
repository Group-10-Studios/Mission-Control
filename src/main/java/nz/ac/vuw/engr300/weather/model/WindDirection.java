package nz.ac.vuw.engr300.weather.model;

/**
 * Enum to represent WindDirection from the loaded data.
 * Can provide an angle and retrieve the direction that matches the angle.
 * 
 * @author Nathan Duckett
 *
 */
public enum WindDirection {
	NORTHWEST, NORTH, NORTHEAST, EAST, SOUTHEAST, SOUTH, SOUTHWEST, WEST;
	
	@Override
	public String toString() {
		// Switch the state of this WindDirection to get a printable value.
		switch(this) {
		case NORTHWEST:
			return "NW";
		case NORTH:
			return "N";
		case NORTHEAST:
			return "NE";
		case EAST:
			return "E";
		case SOUTHEAST:
			return "SE";
		case SOUTH:
			return "S";
		case SOUTHWEST:
			return "SW";
		// Default to WEST direction
		default:
			return "W";
		}
		
	}
	
	/**
	 * The following static values are set out in this document to determine the angles of the wind.
	 * @see <a href="http://snowfence.umn.edu/Components/winddirectionanddegrees.htm">Weather angles</a>
	 */
	private static final double Right_NORTH = 11.25;
	private static final double Right_NORTHEAST = 56.25;
	private static final double Right_EAST = 101.75;
	private static final double Right_SOUTHEAST = 146.25;
	private static final double Right_SOUTH = 191.25;
	private static final double Right_SOUTHWEST = 236.25;
	private static final double Right_WEST = 281.25;
	private static final double Right_NORTHWEST = 326.25;
	
	/**
	 * Get a WindDirection object based on the provided wind angle value.
	 * 
	 * @param windAngle double value of the wind angle extracted in the WeatherData.
	 * @return WindDirection object representing the direction of the angle.
	 */
	public static WindDirection getFromWindAngle(double windAngle) {
		// Check each direction if the wind angle is within the range.
		if (checkRange(windAngle, Right_NORTH, Right_NORTHEAST)) {
			return NORTHEAST;
		} else if (checkRange(windAngle, Right_NORTHEAST, Right_EAST)) {
			return EAST;
		} else if (checkRange(windAngle, Right_EAST, Right_SOUTHEAST)) {
			return SOUTHEAST;
		} else if (checkRange(windAngle, Right_SOUTHEAST, Right_SOUTH)) {
			return SOUTH;
		} else if (checkRange(windAngle, Right_SOUTH, Right_SOUTHWEST)) {
			return SOUTHWEST;
		} else if (checkRange(windAngle, Right_SOUTHWEST, Right_WEST)) {
			return WEST;
		} else if (checkRange(windAngle, Right_WEST, Right_NORTHWEST)) {
			return NORTHWEST;
		} else {
			return NORTH;
		}
	}
	
	/**
	 * Check if the windAngle is within the specified range.
	 * 
	 * @param windAngle Extracted windAngle from the WeatherData.
	 * @param leftSide Left bound of the direction range.
	 * @param rightSide Right bound of the direction range.
	 * @return Boolean indicating if the windAngle is within the bounds;
	 */
	private static boolean checkRange(double windAngle, double leftSide, double rightSide) {
		return windAngle >= leftSide && windAngle < rightSide;
	}
}
