package dev.hollink.pmtt.runetime;

import dev.hollink.pmtt.TreasureTrailPlugin;
import dev.hollink.pmtt.data.steps.TrailStep;
import dev.hollink.pmtt.data.trail.Overlayable;
import java.awt.Dimension;
import java.awt.Graphics2D;
import javax.inject.Inject;
import static net.runelite.api.MenuAction.RUNELITE_OVERLAY_CONFIG;
import static net.runelite.client.ui.overlay.OverlayManager.OPTION_CONFIGURE;
import net.runelite.client.ui.overlay.OverlayPanel;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.components.PanelComponent;
import net.runelite.client.ui.overlay.components.TitleComponent;

/**
 * The TrailOverlay renders the current clue step in an
 * {@link OverlayPanel}. It tells the {@link TrailRuntime}
 * to render the current trail-step using
 * {@link TrailRuntime#renderCurrentStep(Graphics2D, PanelComponent)}
 * <p>
 * Each {@link TrailStep} implements the {@link Overlayable}.
 * The overlayable then contains the logic to show the correct
 * text in the panel.
 */
public class TrailOverlay extends OverlayPanel
{
	private final TrailRuntime runtime;

	@Inject
	public TrailOverlay(
		TreasureTrailPlugin plugin,
		TrailRuntime trailRuntime
	)
	{
		super(plugin);
		this.runtime = trailRuntime;

		setPosition(OverlayPosition.TOP_LEFT);
		addMenuEntry(RUNELITE_OVERLAY_CONFIG, OPTION_CONFIGURE, "Party Trail overlay");
	}

	@Override
	public Dimension render(Graphics2D graphics)
	{
		if (runtime == null || runtime.getTrail() == null)
		{
			return super.render(graphics);
		}

		renderTrailTitle();
		if (runtime.getContext().getProgress().isCompleted())
		{
			renderCompletionText();
		}
		else
		{
			runtime.renderCurrentStep(graphics, this.getPanelComponent());
		}

		return super.render(graphics);
	}


	private void renderCompletionText()
	{
		String formatted = String.format("You have completed all %d steps!", runtime.getTrail().getMetadata().getStepCount());
		this.panelComponent.getChildren().add(TitleComponent.builder().text(formatted).build());
	}

	private void renderTrailTitle()
	{
		this.panelComponent.getChildren().add(TitleComponent.builder().text(runtime.getTrail().getMetadata().getTrailName()).build());
	}
}
