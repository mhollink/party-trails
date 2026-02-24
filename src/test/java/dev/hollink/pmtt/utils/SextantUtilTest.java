package dev.hollink.pmtt.utils;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import org.junit.Test;

public class SextantUtilTest
{

	@Test
	public void getsCorrectCoordinatesForCenterPoint()
	{
		String coordinates = SextantUtil.getCoordinates(SextantUtil.X_ZERO, SextantUtil.Y_ZERO);
		assertThat(coordinates, equalTo("0 degrees 0 minutes north,\n0 degrees 0 minutes east"));
	}


	@Test
	public void getsCorrectMinutesEast()
	{
		String coordinates = SextantUtil.getCoordinates(2445, 3161);
		assertThat(coordinates, equalTo("0 degrees 0 minutes north,\n0 degrees 10 minutes east"));
	}

	@Test
	public void getsCorrectMinutesWest()
	{
		String coordinates = SextantUtil.getCoordinates(2435, 3161);
		assertThat(coordinates, equalTo("0 degrees 0 minutes north,\n0 degrees 10 minutes west"));
	}

	@Test
	public void getsCorrectMinutesNorth()
	{
		String coordinates = SextantUtil.getCoordinates(2440, 3166);
		assertThat(coordinates, equalTo("0 degrees 10 minutes north,\n0 degrees 0 minutes east"));
	}

	@Test
	public void getsCorrectMinutesSouth()
	{
		String coordinates = SextantUtil.getCoordinates(2440, 3156);
		assertThat(coordinates, equalTo("0 degrees 10 minutes south,\n0 degrees 0 minutes east"));
	}

	@Test
	public void getsCorrectDegreesNorth()
	{
		String coordinates = SextantUtil.getCoordinates(2440, 3193);
		assertThat(coordinates, equalTo("1 degrees 0 minutes north,\n0 degrees 0 minutes east"));
	}

	@Test
	public void getsCorrectDegreesSouth()
	{
		String coordinates = SextantUtil.getCoordinates(2440, 3129);
		assertThat(coordinates, equalTo("1 degrees 0 minutes south,\n0 degrees 0 minutes east"));
	}


	@Test
	public void getsCorrectDegreesEast()
	{
		String coordinates = SextantUtil.getCoordinates(2472, 3161);
		assertThat(coordinates, equalTo("0 degrees 0 minutes north,\n1 degrees 0 minutes east"));
	}

	@Test
	public void getsCorrectDegreesWest()
	{
		String coordinates = SextantUtil.getCoordinates(2408, 3161);
		assertThat(coordinates, equalTo("0 degrees 0 minutes north,\n1 degrees 0 minutes west"));
	}

	@Test
	public void realExample_LegendsGuild()
	{
		String coordinates = SextantUtil.getCoordinates(2729, 3347);
		assertThat(coordinates, equalTo("5 degrees 52 minutes north,\n9 degrees 2 minutes east"));
	}

	@Test
	public void realExample_FeroxEnclave()
	{
		String coordinates = SextantUtil.getCoordinates(3142, 3629);
		assertThat(coordinates, equalTo("14 degrees 40 minutes north,\n22 degrees 0 minutes east"));
	}

	@Test
	public void realExample_ThePandemonium()
	{
		String coordinates = SextantUtil.getCoordinates(3059, 2975);
		assertThat(coordinates, equalTo("5 degrees 52 minutes south,\n19 degrees 22 minutes east"));
	}

	@Test
	public void realExample_DeepfinPoint()
	{
		String coordinates = SextantUtil.getCoordinates(1927, 2760);
		assertThat(coordinates, equalTo("12 degrees 34 minutes south,\n16 degrees 2 minutes west"));
	}

	@Test
	public void realExample_Konar()
	{
		String coordinates = SextantUtil.getCoordinates(1309, 3785);
		assertThat(coordinates, equalTo("19 degrees 32 minutes north,\n35 degrees 22 minutes west"));
	}

	@Test
	public void realExample_TemporossDocks()
	{
		String coordinates = SextantUtil.getCoordinates(3149, 2840);
		assertThat(coordinates, equalTo("10 degrees 2 minutes south,\n22 degrees 10 minutes east"));
	}


}