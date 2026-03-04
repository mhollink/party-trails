package dev.hollink.partytrails.data.events;

import lombok.Value;
import net.runelite.api.Skill;

@Value
public class SkillEvent implements TrailEvent
{
	Skill skill;
	int xp;
}
