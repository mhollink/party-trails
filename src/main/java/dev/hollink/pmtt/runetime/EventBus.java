package dev.hollink.pmtt.runetime;

import dev.hollink.pmtt.data.events.ClueEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public final class EventBus
{
	private final List<Consumer<ClueEvent>> listeners = new ArrayList<>();

	public void register(Consumer<ClueEvent> listener)
	{
		listeners.add(listener);
	}

	public void unregister(Consumer<ClueEvent> listener)
	{
		listeners.remove(listener);
	}

	public void publish(ClueEvent event)
	{
		for (Consumer<ClueEvent> listener : List.copyOf(listeners))
		{
			listener.accept(event);
		}
	}
}
