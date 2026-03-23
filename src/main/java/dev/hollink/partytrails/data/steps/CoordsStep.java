package dev.hollink.partytrails.data.steps;

import dev.hollink.partytrails.data.StepType;
import java.awt.Graphics2D;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import net.runelite.api.coords.WorldPoint;
import net.runelite.client.ui.overlay.components.PanelComponent;

@Getter
@ToString
@EqualsAndHashCode
@RequiredArgsConstructor
public final class CoordsStep implements TrailStep
{
	private final StepType stepType = StepType.COORDINATE_STEP;
	private final String hint;
	private final WorldPoint targetLocation;

	@Override
	public void drawOverlay(PanelComponent panel, Graphics2D graphics)
	{
		setPanelWidth(hint, panel, graphics);
		drawTitle("Coordinate clue", panel);
		drawText(hint, panel);
	}

}
