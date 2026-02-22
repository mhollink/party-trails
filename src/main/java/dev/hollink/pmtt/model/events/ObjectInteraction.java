package dev.hollink.pmtt.model.events;

import net.runelite.api.coords.WorldPoint;

import static dev.hollink.pmtt.model.steps.TrailStep.DEFAULT_LOCATION_DISTANCE;

public record ObjectInteraction(
    int objectId,
    String objectName,
    String action,
    WorldPoint location
) implements InteractionEvent {

    @Override
    public boolean compare(InteractionEvent event) {
        if (event instanceof ObjectInteraction other) {
            return this.objectId == other.objectId
                && this.objectName.equalsIgnoreCase(other.objectName)
                && this.action.equalsIgnoreCase(other.action)
                && this.location.distanceTo(other.location) <= DEFAULT_LOCATION_DISTANCE;
        } else {
            return false;
        }
    }
}
