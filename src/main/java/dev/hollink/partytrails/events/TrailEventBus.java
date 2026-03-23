package dev.hollink.partytrails.events;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.inject.Singleton;

@Singleton
public final class TrailEventBus
{
	private final Map<Class<?>, List<EventListener<?>>> listeners = new HashMap<>();

	public synchronized <T> Subscription register(Class<T> eventType, EventListener<T> listener)
	{
		listeners.computeIfAbsent(eventType, k -> new ArrayList<>()).add(listener);

		return () -> unregister(eventType, listener);
	}

	private synchronized <T> void unregister(Class<T> eventType, EventListener<T> listener)
	{
		List<EventListener<?>> eventListeners = listeners.get(eventType);
		if (eventListeners == null)
		{
			return;
		}

		eventListeners.remove(listener);

		if (eventListeners.isEmpty())
		{
			listeners.remove(eventType);
		}
	}

	@SuppressWarnings("unchecked")
	public <T> void publish(T event) {
		Class<?> eventClass = event.getClass();

		for (Map.Entry<Class<?>, List<EventListener<?>>> entry : listeners.entrySet()) {
			Class<?> registeredType = entry.getKey();

			if (registeredType.isAssignableFrom(eventClass)) {
				for (EventListener<?> listener : List.copyOf(entry.getValue())) {
					((EventListener<T>) listener).onEvent(event);
				}
			}
		}
	}
}
