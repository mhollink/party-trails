package dev.hollink.pmtt.model.events;

import net.runelite.api.coords.WorldPoint;

import static dev.hollink.pmtt.model.steps.TrailStep.DEFAULT_LOCATION_DISTANCE;

public record NpcInteraction(
    int npcId,
    String npcName,
    String action,
    WorldPoint location
) implements InteractionEvent {

    @Override
    public boolean compare(InteractionEvent event) {
        if (event instanceof NpcInteraction other) {
            return this.npcId == other.npcId
                && this.npcName.equalsIgnoreCase(other.npcName)
                && this.action.equalsIgnoreCase(other.action)
                && this.location.distanceTo(other.location) <= DEFAULT_LOCATION_DISTANCE;
        } else {
            return false;
        }
    }
}
