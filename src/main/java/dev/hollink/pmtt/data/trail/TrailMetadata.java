package dev.hollink.pmtt.data.trail;

import lombok.Value;

@Value
public class TrailMetadata
{
	int version;
	String trailId;
	String trailName;
	String author;
	int stepCount;
}