package dev.hollink.pmtt.encoding;

/**
 * Signals that the binary input being decoded does not contain the expected
 * magic header.
 * <p>
 * The magic header is written by {@link TrailEncoder} to allow early
 * validation of encoded trail definitions. A mismatch typically means that
 * the input is malformed, truncated, tampered with, or produced by an
 * incompatible encoder implementation.
 * <p>
 * This exception is thrown before any further decoding is attempted to fail
 * fast and prevent undefined behavior.
 */
public class InvalidMagicHeaderException extends IllegalArgumentException
{
	public InvalidMagicHeaderException(String message)
	{
		super(message);
	}
}
