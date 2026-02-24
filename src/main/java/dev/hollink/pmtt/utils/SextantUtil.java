package dev.hollink.pmtt.utils;

import net.runelite.api.coords.WorldPoint;

public final class SextantUtil
{
	public static final int X_ZERO = 2440;
	public static final int Y_ZERO = 3161;

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
