package dev.hollink.partytrails.codec.exceptions;

public class InvalidMagicHeaderException extends IllegalArgumentException
{
	public InvalidMagicHeaderException(String message)
	{
		super(message);
	}
}
