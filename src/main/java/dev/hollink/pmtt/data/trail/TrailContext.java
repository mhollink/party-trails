package dev.hollink.pmtt.data.trail;

import dev.hollink.pmtt.data.events.ClueEvent;
import javax.inject.Inject;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import net.runelite.api.Client;
import net.runelite.api.Skill;
import net.runelite.api.coords.WorldArea;
import net.runelite.api.coords.WorldPoint;

@Data
@RequiredArgsConstructor
public final class TrailContext
{
	private final Client client;
	private final TrailProgress progress;

	private ClueEvent lastEvent;

	@Inject
	public TrailContext(Client client)
	{
		this.client = client;
		this.progress = new TrailProgress();
	}

	public WorldPoint getLocation()
	{
		return client.getLocalPlayer().getWorldLocation();
	}

	public int getSkillExperience(Skill skill)
	{
		return client.getSkillExperience(skill);
	}

	public boolean isInArea(WorldArea area)
	{
		return area.contains(this.getLocation());
	}
}
