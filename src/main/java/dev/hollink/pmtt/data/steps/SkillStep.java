package dev.hollink.pmtt.data.steps;

import dev.hollink.pmtt.data.StepTypes;
import dev.hollink.pmtt.data.events.ClueEvent;
import dev.hollink.pmtt.data.trail.ClueContext;
import java.awt.Graphics2D;
import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import net.runelite.client.ui.overlay.components.PanelComponent;


// TODO: implement this class

@Slf4j
@ToString
@EqualsAndHashCode
@RequiredArgsConstructor
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
