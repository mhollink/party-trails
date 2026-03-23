package dev.hollink.partytrails.data.steps;

import dev.hollink.partytrails.data.StepType;
import dev.hollink.partytrails.data.events.SkillEvent;
import dev.hollink.partytrails.data.events.TrailEvent;
import dev.hollink.partytrails.data.trail.TrailContext;
import java.awt.Graphics2D;
import java.util.Objects;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import net.runelite.api.Skill;
import net.runelite.api.coords.WorldArea;
import net.runelite.client.ui.overlay.components.PanelComponent;

@ToString
@Getter
@RequiredArgsConstructor
public final class SkillStep implements TrailStep
{
	private final String hint;
	private final Skill skill;
	private final int expRequired;
	private final WorldArea area;

	@Override
	public StepType getStepType()
	{
		return StepType.SKILL_STEP;
	}

	@Override
	public void drawOverlay(PanelComponent panel, Graphics2D graphics)
	{
		setPanelWidth(hint, panel, graphics);
		drawTitle("Skilling clue", panel);
		drawText(hint, panel);
	}

	@Override
	public void onActivate(TrailContext context)
	{
		String key = getContextKey(context);

		// Set the experience of the skill on start
		int startExp = context.getSkillExperience(skill);
		context.getProgress().storeInt(key, startExp);
	}

	@Override
	public boolean handlesEvent(TrailEvent event)
	{
		if (event instanceof SkillEvent)
		{
			SkillEvent skillEvent = (SkillEvent) event;
			return skillEvent.getSkill() == skill;
		}
		return false;
	}

	@Override
	public boolean isComplete(TrailContext context, TrailEvent event)
	{
		if (event instanceof SkillEvent)
		{
			SkillEvent skillEvent = (SkillEvent) event;
			if (skill == skillEvent.getSkill())
			{
				if (!context.getClient().getLocalPlayer().getWorldLocation().isInArea(area))
				{
					return false;
				}

				String key = getContextKey(context);
				int startExp = context.getProgress().getStoredInt(key);
				int expDiff = skillEvent.getXp() - startExp;

				return expDiff >= expRequired;
			}
		}

		return false;
	}

	private String getContextKey(TrailContext context)
	{
		int stepIndex = context.getProgress().getCurrentStepIndex();
		String skillName = skill.getName().toLowerCase();
		return String.format("step.%d.%s-experience", stepIndex, skillName);
	}

	/**
	 * Custom equals method to deep check the world area.
	 */
	@Override
	public boolean equals(Object o)
	{
		if (o instanceof SkillStep)
		{
			SkillStep skillStep = (SkillStep) o;
			return expRequired == skillStep.expRequired
				&& Objects.equals(hint, skillStep.hint)
				&& skill == skillStep.skill
				&& area.getX() == skillStep.area.getX()
				&& area.getY() == skillStep.area.getY()
				&& area.getWidth() == skillStep.area.getWidth()
				&& area.getHeight() == skillStep.area.getHeight()
				&& area.getPlane() == skillStep.area.getPlane();
		}
		return false;
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(hint, skill, expRequired, area);
	}
}
