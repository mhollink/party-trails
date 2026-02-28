package dev.hollink.pmtt.runetime;

import dev.hollink.pmtt.data.events.AnimationEvent;
import dev.hollink.pmtt.data.events.ClueEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import net.runelite.api.coords.WorldPoint;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsCollectionContaining.hasItem;
import org.junit.Before;
import org.junit.Test;

public class ClueEventBusTest
{

	private ClueEventBus eventBus;

	@Before
	public void setUp() throws Exception
	{
		eventBus = new ClueEventBus();
	}

	@Test
	public void shouldDeliverEventToRegisteredListener()
	{
		List<ClueEvent> received = new ArrayList<>();

		Consumer<ClueEvent> listener = received::add;
		eventBus.register(listener);

		ClueEvent event = new AnimationEvent(1, new WorldPoint(10, 10, 0));
		eventBus.publish(event);

		assertThat(received, hasItem(event));
	}

	@Test
	public void shouldNotDeliverEventToUnregisteredListener()
	{
		List<ClueEvent> received = new ArrayList<>();

		Consumer<ClueEvent> listener = received::add;
		eventBus.register(listener);
		eventBus.unregister(listener);

		ClueEvent event = new AnimationEvent(1, new WorldPoint(10, 10, 0));
		eventBus.publish(event);

		assertThat(received.size(), equalTo(0));
	}

	@Test
	public void shouldDeliverEventToAllRegisteredListeners()
	{
		List<ClueEvent> received1 = new ArrayList<>();
		List<ClueEvent> received2 = new ArrayList<>();

		eventBus.register(received1::add);
		eventBus.register(received2::add);

		ClueEvent event = new AnimationEvent(1, new WorldPoint(10, 10, 0));
		eventBus.publish(event);

		assertThat(received1, hasItem(event));
		assertThat(received2, hasItem(event));
	}
}