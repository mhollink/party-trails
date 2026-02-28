package dev.hollink.pmtt.utils;

import static dev.hollink.pmtt.matchers.IsAlphanumeric.isAlphanumeric;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertThat;
import org.junit.Test;

public class RandomUtilTest
{

	@Test
	public void shouldGenerateStringOfRequestedLength()
	{
		String result = RandomUtil.randomAlphanumeric(16);

		assertThat(result.length(), equalTo(16));
	}

	@Test
	public void shouldContainOnlyAlphanumericCharacters()
	{
		String result = RandomUtil.randomAlphanumeric(100);

		assertThat(result, isAlphanumeric());
	}

	@Test
	public void shouldProduceDifferentValuesAcrossCalls() {
		String first = RandomUtil.randomAlphanumeric(16);
		String second = RandomUtil.randomAlphanumeric(16);

		assertThat(first, not(equalTo(second)));
	}
}