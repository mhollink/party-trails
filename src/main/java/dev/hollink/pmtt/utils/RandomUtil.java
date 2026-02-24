package dev.hollink.pmtt.utils;

import java.security.SecureRandom;

public final class RandomUtil
{
	private static final String ALPHANUM = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

	private static final SecureRandom RANDOM = new SecureRandom();

	public static String randomAlphanumeric(int length)
	{
		StringBuilder sb = new StringBuilder(length);

		for (int i = 0; i < length; i++)
		{
			int index = RANDOM.nextInt(ALPHANUM.length());
			sb.append(ALPHANUM.charAt(index));
		}

		return sb.toString();
	}
}