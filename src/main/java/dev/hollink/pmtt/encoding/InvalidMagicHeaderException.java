package dev.hollink.pmtt.encoding;

public class InvalidMagicHeaderException extends IllegalArgumentException
{
	public InvalidMagicHeaderException(String message)
	{
		super(message);
	}
}
