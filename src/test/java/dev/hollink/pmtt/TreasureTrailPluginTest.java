package dev.hollink.pmtt;

import net.runelite.client.RuneLite;
import net.runelite.client.externalplugins.ExternalPluginManager;

public class TreasureTrailPluginTest
{
	public static void main(String[] args) throws Exception
	{
		ExternalPluginManager.loadBuiltin(TreasureTrailPlugin.class);
		RuneLite.main(args);
	}
}