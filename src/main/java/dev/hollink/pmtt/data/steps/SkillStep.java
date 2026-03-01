package dev.hollink.pmtt.data.steps;

import dev.hollink.pmtt.data.StepType;
import dev.hollink.pmtt.data.events.ClueEvent;
import dev.hollink.pmtt.data.events.SkillEvent;
import dev.hollink.pmtt.data.trail.TrailContext;
import static dev.hollink.pmtt.encoding.TrailDecoder.readString;
import static dev.hollink.pmtt.encoding.TrailEncoder.writeString;
import java.awt.Graphics2D;
import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import net.runelite.api.Skill;
import net.runelite.api.coords.WorldArea;
import net.runelite.client.ui.overlay.components.PanelComponent;

@ToString
@RequiredArgsConstructor
public final class SkillStep implements TrailStep
{
	private final String hint;
	private final Skill skill;
	private final int expRequired;
	private final WorldArea area;

	@Override
	public StepType type()
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
	public boolean handlesEvent(ClueEvent event)
	{
		if (event instanceof SkillEvent)
		{
			SkillEvent skillEvent = (SkillEvent) event;
			return skillEvent.getSkill() == skill;
		}
		return false;
	}

	@Override
	public boolean isComplete(TrailContext context, ClueEvent event)
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

	@Override
	public void encode(DataOutput out)
		throws IOException
	{
		writeString(out, hint);
		out.writeInt(skill.ordinal());
		out.writeInt(expRequired);
		out.writeInt(area.getX());
		out.writeInt(area.getY());
		out.writeInt(area.getWidth());
		out.writeInt(area.getHeight());
		out.writeInt(area.getPlane());
	}

	public static SkillStep decode(DataInput in)
		throws IOException
	{
		String hint = readString(in);
		Skill skill = Skill.values()[in.readInt()];
		int exp = in.readInt();
		int x = in.readInt();
		int y = in.readInt();
		int w = in.readInt();
		int h = in.readInt();
		int plane = in.readInt();

		return new SkillStep(hint, skill, exp, new WorldArea(x, y, w, h, plane));
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
