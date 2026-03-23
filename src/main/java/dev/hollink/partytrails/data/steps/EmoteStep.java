package dev.hollink.partytrails.data.steps;

import dev.hollink.partytrails.data.Emote;
import dev.hollink.partytrails.data.StepType;
import java.awt.Color;
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
public final class EmoteStep implements TrailStep
{

	private final String hint;
	private final Emote targetEmoteOne;
	private final WorldPoint targetLocation;
	private final StepType stepType = StepType.EMOTE_STEP;

	@Override
	public void drawOverlay(PanelComponent panel, Graphics2D graphics)
	{
		setPanelWidth(hint, panel, graphics);
		drawTitle("Emote Clue", panel);
		drawText("Emote:", panel, Color.WHITE);
		drawText(targetEmoteOne.getName(), panel);
		drawText("Location:", panel, Color.WHITE);
		drawText(hint, panel);
	}


}


