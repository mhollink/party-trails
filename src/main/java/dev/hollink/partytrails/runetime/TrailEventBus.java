package dev.hollink.partytrails.runetime;

import dev.hollink.partytrails.data.events.TrailEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import javax.inject.Singleton;

/**
 * The EventBus forwards the {@link TrailEvent}'s to all the
 * interested parties. Classes can register an event listener
 * on the bus using the {@link TrailEventBus#register(Consumer)}
 * function. Each time an event gets published, it gets pushed
 * to *all* the {@link TrailEventBus#listeners}.
 */
@Singleton
public final class TrailEventBus
{
	private final List<Consumer<TrailEvent>> listeners = new ArrayList<>();

	public void register(Consumer<TrailEvent> listener)
	{
		listeners.add(listener);
	}

	public void unregister(Consumer<TrailEvent> listener)
	{
		listeners.remove(listener);
	}

	public void publish(TrailEvent event)
	{
		for (Consumer<TrailEvent> listener : List.copyOf(listeners))
		{
			listener.accept(event);
		}
	}
}
