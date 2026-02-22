package dev.hollink.pmtt;

import com.google.inject.Provides;
import dev.hollink.pmtt.model.events.AnimationEvent;
import dev.hollink.pmtt.model.events.InteractionEvent;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.Player;
import net.runelite.api.events.AnimationChanged;
import net.runelite.api.events.MenuOptionClicked;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;

import javax.inject.Inject;

@Slf4j
@PluginDescriptor(name = "Party Trails", description = "Create and complete custom player made treasure trails", tags = {"clue", "treasure", "custom"})
public class TreasureTrailPlugin extends Plugin {

    @Inject
    private Client client;

    @Inject
    private TreasureTrailConfig config;

    @Inject
    private TrailManager trailManager;

    @Override
    protected void startUp() throws Exception {
        trailManager.startPartyTrails();
    }

    @Override
    protected void shutDown() throws Exception {
        trailManager.stop();
    }

    @Subscribe
    public void onAnimationChanged(AnimationChanged event) {
        Player player = client.getLocalPlayer();
        if (event.getActor() != player || player.getAnimation() == -1) {
            return;
        }

        AnimationEvent animationEvent = new AnimationEvent(player.getWorldLocation(), player.getAnimation());
        trailManager.getClueEventBus().publish(animationEvent);
    }

    @Subscribe
    public void onMenuOptionClicked(MenuOptionClicked event) {
        InteractionEvent.fromMenuOptionClicked(event, client)
            .ifPresent(interactionEvent -> trailManager.getClueEventBus().publish(interactionEvent));
    }

    @Provides
    TreasureTrailConfig provideConfig(ConfigManager configManager) {
        return configManager.getConfig(TreasureTrailConfig.class);
    }
}
