package dev.hollink.pmtt.model.trail;

import dev.hollink.pmtt.model.events.ClueEvent;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.runelite.api.Client;
import net.runelite.api.Skill;
import net.runelite.api.coords.WorldArea;
import net.runelite.api.coords.WorldPoint;

@Data
@RequiredArgsConstructor
public final class ClueContext {

    @Getter(AccessLevel.NONE)
    private final Client client;
    private final TrailProgress progress;
    private ClueEvent lastEvent;

    public WorldPoint getLocation() {
        return client.getLocalPlayer().getWorldLocation();
    }

    public int getSkillLevel(Skill skill) {
        return client.getRealSkillLevel(skill);
    }

    public boolean isInArea(WorldArea area) {
        return area.contains(this.getLocation());
    }
}
