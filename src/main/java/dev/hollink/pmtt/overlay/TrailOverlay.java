package dev.hollink.pmtt.overlay;

import dev.hollink.pmtt.TreasureTrailConfig;
import dev.hollink.pmtt.TreasureTrailPlugin;
import dev.hollink.pmtt.runetime.TrailRuntime;
import lombok.Getter;
import lombok.Setter;
import net.runelite.api.Client;
import net.runelite.client.ui.overlay.OverlayPanel;
import net.runelite.client.ui.overlay.OverlayPosition;

import javax.inject.Inject;

import java.awt.Dimension;
import java.awt.Graphics2D;

import static net.runelite.api.MenuAction.RUNELITE_OVERLAY_CONFIG;
import static net.runelite.client.ui.overlay.OverlayManager.OPTION_CONFIGURE;

public class TrailOverlay extends OverlayPanel {

    private final Client client;
    private final TreasureTrailPlugin plugin;
    private final TreasureTrailConfig config;

    @Getter
    @Setter
    private TrailRuntime trail;

    @Inject
    public TrailOverlay(Client client, TreasureTrailPlugin plugin, TreasureTrailConfig config) {
        super(plugin);
        this.plugin = plugin;
        this.client = client;
        this.config = config;

        setPosition(OverlayPosition.TOP_LEFT);
        addMenuEntry(RUNELITE_OVERLAY_CONFIG, OPTION_CONFIGURE, "Party Trail overlay");
    }

    @Override
    public Dimension render(Graphics2D graphics) {
        if (trail == null || trail.isFinished()) {
            return super.render(graphics);
        }

        trail.renderCurrentStep(graphics, this.getPanelComponent());

        return super.render(graphics);
    }
}
