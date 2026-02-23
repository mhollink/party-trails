package dev.hollink.pmtt.data.events;

import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.GameObject;
import net.runelite.api.MenuAction;
import net.runelite.api.MenuEntry;
import net.runelite.api.NPC;
import net.runelite.api.Player;
import net.runelite.api.Tile;
import net.runelite.api.events.AnimationChanged;
import net.runelite.api.events.MenuOptionClicked;
import net.runelite.api.events.StatChanged;

@Slf4j
public final class ClueEventFactory
{

	public static Optional<AnimationEvent> fromAnimationChanged(AnimationChanged event, Client client)
	{
		Player player = client.getLocalPlayer();
		if (event.getActor() != player || player.getAnimation() == -1)
		{
			return Optional.empty();
		}

		AnimationEvent animationEvent = new AnimationEvent(player.getAnimation(), player.getWorldLocation());
		return Optional.of(animationEvent);
	}

	public static Optional<InteractionEvent> fromMenuOptionClicked(MenuOptionClicked event, Client client)
	{
		if (event == null || client == null)
		{
			return Optional.empty();
		}

		MenuEntry entry = event.getMenuEntry();
		if (entry == null)
		{
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
			|| type == MenuAction.NPC_FIFTH_OPTION)
		{
			return getNpcInteractionEvent(client, entry, action);
		}

		// ------------------------------------
		// OBJECT INTERACTION
		// ------------------------------------
		if (type == MenuAction.GAME_OBJECT_FIRST_OPTION
			|| type == MenuAction.GAME_OBJECT_SECOND_OPTION
			|| type == MenuAction.GAME_OBJECT_THIRD_OPTION
			|| type == MenuAction.GAME_OBJECT_FOURTH_OPTION
			|| type == MenuAction.GAME_OBJECT_FIFTH_OPTION)
		{
			return getObjectInteractionEvent(client, entry, action);
		}

		return Optional.empty();
	}

	private static Optional<InteractionEvent> getObjectInteractionEvent(Client client, MenuEntry entry, String action)
	{
		var worldView = client.getWorldView(entry.getWorldViewId());
		int p = worldView.getPlane();
		int x = entry.getParam0();
		int y = entry.getParam1();

		Tile tile = worldView.getScene().getTiles()[p][x][y];
		if (tile == null)
		{
			return Optional.empty();
		}

		for (GameObject obj : tile.getGameObjects())
		{
			if (obj != null && obj.getId() == entry.getIdentifier())
			{
				String objectName = client.getObjectDefinition(entry.getIdentifier()).getName();
				InteractionEvent event = new InteractionEvent(entry.getIdentifier(), objectName, action, obj.getWorldLocation());
				return Optional.of(event);
			}
		}

		return Optional.empty();
	}

	private static Optional<InteractionEvent> getNpcInteractionEvent(Client client, MenuEntry entry, String action)
	{
		int npcIndex = entry.getIdentifier();
		NPC npc = client.getTopLevelWorldView().npcs().byIndex(npcIndex);

		if (npc == null)
		{
			return Optional.empty();
		}

		InteractionEvent event = new InteractionEvent(npc.getId(), npc.getName(), action, npc.getWorldLocation());
		return Optional.of(event);
	}

	public static Optional<SkillEvent> fromStatChanged(StatChanged event, Client client)
	{
		log.info("[ClueEventFactory] [fromStatChanged] {}", event);
		return Optional.empty();
	}
}
