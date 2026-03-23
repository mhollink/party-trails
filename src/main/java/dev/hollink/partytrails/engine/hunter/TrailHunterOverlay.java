package dev.hollink.partytrails.engine.hunter;

import dev.hollink.partytrails.data.steps.TrailStep;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.util.Optional;
import javax.inject.Singleton;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.ui.overlay.OverlayPanel;

@Singleton
public class TrailHunterOverlay extends OverlayPanel
{
	private final TrailHunterEngine engine;

	public TrailHunterOverlay(Plugin plugin, TrailHunterEngine engine)
	{
		super(plugin);
		this.engine = engine;
	}

	@Override
	public Dimension render(Graphics2D graphics)
	{
		Optional.ofNullable(engine.getCurrentStep())
			.ifPresent(step -> renderHintForStep(step, graphics));

		return super.render(graphics);
	}

	private void renderHintForStep(TrailStep step, Graphics2D graphics) {
		step.drawOverlay(this.getPanelComponent(), graphics);
	}
}
