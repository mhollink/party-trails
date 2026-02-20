package dev.hollink.pmtt.runetime.events;

import net.runelite.api.Client;
import net.runelite.api.GameObject;
import net.runelite.api.MenuAction;
import net.runelite.api.MenuEntry;
import net.runelite.api.NPC;
import net.runelite.api.Tile;
import net.runelite.api.events.MenuOptionClicked;

import java.util.Optional;

public sealed interface InteractionEvent
    permits NpcInteraction, ObjectInteraction {
    String action();
    boolean compare(InteractionEvent event);

    static Optional<InteractionEvent> fromMenuOptionClicked(
        MenuOptionClicked event,
        Client client
    ) {
        if (event == null || client == null) {
            return Optional.empty();
        }

        MenuEntry entry = event.getMenuEntry();
        if (entry == null) {
            return Optional.empty();
        }

        String action = entry.getOption();
        MenuAction type = entry.getType();

        // ------------------------------------
        // NPC INTERACTION
        // ------------------------------------
        if (type == MenuAction.NPC_FIRST_OPTION
            || type == MenuAction.NPC_SECOND_OPTION
            || type == MenuAction.NPC_THIRD_OPTION
            || type == MenuAction.NPC_FOURTH_OPTION
            || type == MenuAction.NPC_FIFTH_OPTION) {
            return getNpc(client, entry, action);
        }

        // ------------------------------------
        // OBJECT INTERACTION
        // ------------------------------------
        if (type == MenuAction.GAME_OBJECT_FIRST_OPTION
            || type == MenuAction.GAME_OBJECT_SECOND_OPTION
            || type == MenuAction.GAME_OBJECT_THIRD_OPTION
            || type == MenuAction.GAME_OBJECT_FOURTH_OPTION
            || type == MenuAction.GAME_OBJECT_FIFTH_OPTION) {
            return getObject(client, entry, action);
        }

        return Optional.empty();
    }

    private static Optional<InteractionEvent> getObject(Client client, MenuEntry entry, String action) {
        Tile tile = getTile(client, entry);
        if (tile == null) {
            return Optional.empty();
        }

        for (GameObject obj : tile.getGameObjects()) {
            if (obj != null && obj.getId() == entry.getIdentifier()) {
                String objectName = client.getObjectDefinition(entry.getIdentifier()).getName();
                return Optional.of(new ObjectInteraction(
                    entry.getIdentifier(),
                    objectName,
                    action,
                    obj.getWorldLocation()
                ));
            }
        }

        return Optional.empty();
    }

    private static Tile getTile(Client client, MenuEntry entry) {
        var worldView = client.getWorldView(entry.getWorldViewId());
        int p = worldView.getPlane();
        int x = entry.getParam0();
        int y = entry.getParam1();

        return worldView.getScene().getTiles()[p][x][y];
    }

    private static Optional<InteractionEvent> getNpc(Client client, MenuEntry entry, String action) {
        int npcIndex = entry.getIdentifier();
        NPC npc = client.getTopLevelWorldView().npcs().byIndex(npcIndex);

        if (npc == null) {
            return Optional.empty();
        }

        return Optional.of(new NpcInteraction(
            npc.getId(),
            npc.getName(),
            action,
            npc.getWorldLocation()
        ));
    }
}