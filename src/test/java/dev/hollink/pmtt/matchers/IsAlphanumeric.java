package dev.hollink.pmtt.matchers;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

public class IsAlphanumeric extends TypeSafeMatcher<String>
{

	@Override
	protected boolean matchesSafely(String s)
	{
		return s.matches("^[A-Za-z0-9]+$");
	}

	@Override
	public void describeTo(Description description)
	{
		description.appendText("a string containing only alphanumeric characters");
	}

	public static Matcher<String> isAlphanumeric()
	{
		return new IsAlphanumeric();
	}
}
