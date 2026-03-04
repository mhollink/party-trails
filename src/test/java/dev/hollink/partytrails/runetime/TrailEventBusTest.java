package dev.hollink.partytrails.runetime;

import dev.hollink.partytrails.data.events.AnimationEvent;
import dev.hollink.partytrails.data.events.TrailEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import net.runelite.api.coords.WorldPoint;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsCollectionContaining.hasItem;
import org.junit.Before;
import org.junit.Test;

public class TrailEventBusTest
{

	private TrailEventBus eventBus;

	@Before
	public void setUp() throws Exception
	{
		eventBus = new TrailEventBus();
	}

	@Test
	public void shouldDeliverEventToRegisteredListener()
	{
		List<TrailEvent> received = new ArrayList<>();

		Consumer<TrailEvent> listener = received::add;
		eventBus.register(listener);

		TrailEvent event = new AnimationEvent(1, new WorldPoint(10, 10, 0));
		eventBus.publish(event);

		assertThat(received, hasItem(event));
	}

	@Test
	public void shouldNotDeliverEventToUnregisteredListener()
	{
		List<TrailEvent> received = new ArrayList<>();

		Consumer<TrailEvent> listener = received::add;
		eventBus.register(listener);
		eventBus.unregister(listener);

		TrailEvent event = new AnimationEvent(1, new WorldPoint(10, 10, 0));
		eventBus.publish(event);

		assertThat(received.size(), equalTo(0));
	}

	@Test
	public void shouldDeliverEventToAllRegisteredListeners()
	{
		List<TrailEvent> received1 = new ArrayList<>();
		List<TrailEvent> received2 = new ArrayList<>();

		eventBus.register(received1::add);
		eventBus.register(received2::add);

		TrailEvent event = new AnimationEvent(1, new WorldPoint(10, 10, 0));
		eventBus.publish(event);

		assertThat(received1, hasItem(event));
		assertThat(received2, hasItem(event));
	}
}