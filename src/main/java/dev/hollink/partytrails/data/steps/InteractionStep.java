package dev.hollink.partytrails.data.steps;

import dev.hollink.partytrails.data.StepType;
import java.awt.Graphics2D;
import java.util.List;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.coords.WorldPoint;
import net.runelite.client.ui.overlay.components.PanelComponent;

import static dev.hollink.partytrails.data.StepType.ANAGRAM_STEP;
import static dev.hollink.partytrails.data.StepType.CIPHER_STEP;
import static dev.hollink.partytrails.data.StepType.CRYPTIC_STEP;

@Slf4j
@Getter
@ToString
@EqualsAndHashCode
public final class InteractionStep implements TrailStep
{
	public static final List<StepType> ALLOWED_TYPES =
		List.of(ANAGRAM_STEP, CIPHER_STEP, CRYPTIC_STEP);

	private final StepType stepType;
	private final String hint;
	private final Target target;

	public InteractionStep(StepType stepType, String hint, Target target)
	{
		if (stepType == null || !ALLOWED_TYPES.contains(stepType))
		{
			throw new IllegalArgumentException("Invalid step type: " + stepType);
		}

		this.stepType = stepType;
		this.hint = hint;
		this.target = target;
	}

	@Override
	public void drawOverlay(PanelComponent panel, Graphics2D graphics)
	{
		setPanelWidth(hint, panel, graphics);
		drawTitle(stepType.stepTypeName, panel);
		drawText(hint, panel);
	}

	@Value
	public static class Target
	{
		int targetId;
		String targetName;
		String interactionType;
		WorldPoint location;
	}

}
