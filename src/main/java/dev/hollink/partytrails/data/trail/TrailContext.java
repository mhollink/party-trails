package dev.hollink.partytrails.data.trail;

import dev.hollink.partytrails.events.events.TrailEvent;
import javax.inject.Inject;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import net.runelite.api.Client;
import net.runelite.api.Skill;

@Data
@RequiredArgsConstructor
public final class TrailContext
{
	private final Client client;
	private final TrailProgress progress;

	private TrailEvent lastEvent;

	@Inject
	public TrailContext(Client client)
	{
		this.client = client;
		this.progress = new TrailProgress();
	}

	public int getSkillExperience(Skill skill)
	{
		return client.getSkillExperience(skill);
	}
}
