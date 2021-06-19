package com.develotters.homeros.event;

import com.develotters.homeros.event.context.ContextFactory;
import com.develotters.homeros.event.instant.InstantEvent;
import com.develotters.homeros.event.instant.InstantRecording;
import com.develotters.homeros.event.instant.NoOpInstantRecording;
import com.develotters.homeros.event.instant.SimpleInstantRecording;
import com.develotters.homeros.event.listener.RecordingListener;
import com.develotters.homeros.event.span.NoOpSpanRecording;
import com.develotters.homeros.event.span.SimpleSpanRecording;
import com.develotters.homeros.event.span.SpanEvent;
import com.develotters.homeros.event.span.SpanRecording;
import com.develotters.homeros.time.Clock;

public class SimpleRecorder<T> implements Recorder<T> {
	private final RecordingListener<T> listener;
	private final ContextFactory<T> contextFactory;
	private final Clock clock;
	private volatile boolean enabled;

	public SimpleRecorder(RecordingListener<T> listener, ContextFactory<T> contextFactory, Clock clock) {
		this.listener = listener;
		this.contextFactory = contextFactory;
		this.clock = clock;
		this.enabled = true;
	}

	@Override
	public SpanRecording<T> recordingFor(SpanEvent event) {
		return enabled ? new SimpleSpanRecording<>(event, listener, contextFactory, clock) : new NoOpSpanRecording<>();
	}

	@Override
	public InstantRecording recordingFor(InstantEvent event) {
		return enabled ? new SimpleInstantRecording(event, listener) : new NoOpInstantRecording();
	}

	public boolean isEnabled() {
		return this.enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
}
