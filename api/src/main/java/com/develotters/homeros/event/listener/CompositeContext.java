package com.develotters.homeros.event.listener;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CompositeContext {
	private final Map<RecordingListener<?>, Object> contexts = new HashMap<>();

	CompositeContext(List<RecordingListener<?>> listeners) {
		// Could be a .stream().collect(toMap(...)) but toMap fails on null values: https://bugs.openjdk.java.net/browse/JDK-8148463
		for (RecordingListener<?> listener : listeners) {
			this.contexts.put(listener, listener.createContext());
		}
	}

	@SuppressWarnings("unchecked")
	<T> T byListener(RecordingListener<T> listener) {
		return (T) this.contexts.get(listener);
	}
}
