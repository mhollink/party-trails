package dev.hollink.pmtt.model.steps;

import dev.hollink.pmtt.model.StepTypes;
import dev.hollink.pmtt.model.events.ClueEvent;
import dev.hollink.pmtt.model.trail.ClueContext;
import java.awt.Graphics2D;
import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import net.runelite.client.ui.overlay.components.PanelComponent;


// TODO: implement this class

@Slf4j
public final class SkillStep implements TrailStep
{

	@Override
	public byte typeId()
	{
		return StepTypes.SKILL_STEP;
	}

	@Override
	public void drawOverlay(PanelComponent panel, Graphics2D graphics)
	{

	}

	@Override
	public void onActivate(ClueContext context)
	{

	}

	@Override
	public boolean isComplete(ClueEvent event)
	{
		return false;
	}

	@Override
	public void encode(DataOutput out)
		throws IOException
	{

	}

	public static SkillStep decode(DataInput in)
		throws IOException
	{
		return null;
	}

}
