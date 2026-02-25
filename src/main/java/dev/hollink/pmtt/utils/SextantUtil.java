package dev.hollink.pmtt.utils;

import lombok.experimental.UtilityClass;
import net.runelite.api.coords.WorldPoint;

@UtilityClass
public final class SextantUtil
{
	public static final int X_ZERO = 2440;
	public static final int Y_ZERO = 3161;

	/**
	 * Converts a point of the OSRS worldmap to a coordinate-clue
	 * description. Uses the absolute 0-position for the sextant
	 * which is located in the center of the observatory.
	 *
	 * @param x the world point position on the x-axis
	 * @param y the world point position on the y-axis
	 * @return A string representation in degrees and minutes of the given x/y coordinate.
	 */
	public static String getCoordinates(int x, int y)
	{
		int dx = x - X_ZERO;
		int dy = y - Y_ZERO;

		String eastWest = dx >= 0 ? "east" : "west";
		String northSouth = dy >= 0 ? "north" : "south";

		var xc = convertToCoordinate(dx);
		var yc = convertToCoordinate(dy);

		return String.format(
			"%d degrees %d minutes %s,\n%d degrees %d minutes %s",
			yc.degrees, yc.minutes, northSouth,
			xc.degrees, xc.minutes, eastWest
		);
	}

	/**
	 * @param point A point on the 0-plane.
	 * @return A string representation in degrees and minutes of the given x/y coordinate.
	 * @see #getCoordinates(int, int)
	 */
	public static String getCoordinates(WorldPoint point)
	{
		return getCoordinates(point.getX(), point.getY());
	}

	private static Coordinate convertToCoordinate(int point)
	{
		int absoluteValue = Math.abs(point);
		int degrees = absoluteValue / 32;
		int minutes = absoluteValue - degrees * 32;

		if (minutes >= 30)
		{
			degrees += (minutes / 30);
			minutes -= (minutes / 30) * 30;
		}

		return new Coordinate(degrees, minutes * 2);
	}

	private record Coordinate(int degrees, int minutes)
	{
	}
}
