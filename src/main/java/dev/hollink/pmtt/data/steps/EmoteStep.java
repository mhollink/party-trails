package dev.hollink.pmtt.data.steps;

import static dev.hollink.pmtt.encoding.TrailDecoder.readString;
import static dev.hollink.pmtt.encoding.TrailEncoder.writeString;
import dev.hollink.pmtt.data.Emote;
import dev.hollink.pmtt.data.StepType;
import dev.hollink.pmtt.data.events.AnimationEvent;
import dev.hollink.pmtt.data.events.ClueEvent;
import dev.hollink.pmtt.data.trail.ClueContext;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.coords.WorldPoint;
import static net.runelite.client.plugins.cluescrolls.ClueScrollOverlay.TITLED_CONTENT_COLOR;
import net.runelite.client.ui.overlay.components.ComponentConstants;
import net.runelite.client.ui.overlay.components.LineComponent;
import net.runelite.client.ui.overlay.components.PanelComponent;
import net.runelite.client.ui.overlay.components.TitleComponent;

@Slf4j
@ToString
@EqualsAndHashCode
@RequiredArgsConstructor
public final class EmoteStep implements TrailStep
{

	private final String hint;
	private final Emote targetEmoteOne;
	private final WorldPoint targetLocation;

	@Override
	public StepType type()
	{
		return StepType.EMOTE_STEP;
	}

	@Override
	public void drawOverlay(PanelComponent panel, Graphics2D graphics)
	{
		final FontMetrics fontMetrics = graphics.getFontMetrics();
		int textWidth = Math.max(ComponentConstants.STANDARD_WIDTH, fontMetrics.stringWidth(hint) + 10);

		panel.setPreferredSize(new Dimension(textWidth, 0));

		panel.getChildren().add(TitleComponent.builder().text("Emote Clue").build());
		panel.getChildren().add(LineComponent.builder().left("Emotes:").build());
		panel.getChildren().add(LineComponent.builder().left(targetEmoteOne.getName()).leftColor(TITLED_CONTENT_COLOR).build());

		panel.getChildren().add(LineComponent.builder().left("Location:").build());
		panel.getChildren().add(LineComponent.builder().left(hint).leftColor(TITLED_CONTENT_COLOR).build());
	}

	@Override
	public void onActivate(ClueContext context)
	{
		// Noop.
	}

	@Override
	public boolean isComplete(ClueEvent event)
	{
		if (event instanceof AnimationEvent animationEvent)
		{
			log.info("Validating emote clue step");
			return animationEvent.animationId() == targetEmoteOne.getAnimationId()
				&& animationEvent.location().distanceTo(targetLocation) <= DEFAULT_LOCATION_DISTANCE;
		}
		else
		{
			return false;
		}
	}

	@Override
	public void encode(DataOutput out) throws IOException
	{
		writeString(out, hint);
		out.writeInt(targetEmoteOne.ordinal());
		out.writeInt(targetLocation.getX());
		out.writeInt(targetLocation.getY());
		out.writeInt(targetLocation.getPlane());
	}

	public static EmoteStep decode(DataInput in) throws IOException
	{
		String hint = readString(in);
		Emote emote = Emote.values()[in.readInt()];
		int x = in.readInt();
		int y = in.readInt();
		int plane = in.readInt();

		return new EmoteStep(hint, emote, new WorldPoint(x, y, plane));
	}
}


