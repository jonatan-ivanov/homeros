package com.develotters.homeros.event;

import com.develotters.homeros.event.context.ContextFactory;
import com.develotters.homeros.event.instant.InstantEvent;
import com.develotters.homeros.event.instant.InstantRecording;
import com.develotters.homeros.event.instant.SimpleInstantRecording;
import com.develotters.homeros.event.listener.RecordingListener;
import com.develotters.homeros.event.span.SimpleSpanRecording;
import com.develotters.homeros.event.span.SpanEvent;
import com.develotters.homeros.event.span.SpanRecording;
import com.develotters.homeros.time.Clock;

public class SimpleRecorder<T> implements Recorder<T> {
	private final RecordingListener<T> listener;
	private final ContextFactory<T> contextFactory;
	private final Clock clock;

	public SimpleRecorder(RecordingListener<T> listener, ContextFactory<T> contextFactory, Clock clock) {
		this.listener = listener;
		this.contextFactory = contextFactory;
		this.clock = clock;
	}

	@Override
	public SpanRecording<T> recordingFor(SpanEvent event) {
		return new SimpleSpanRecording<>(event, listener, contextFactory, clock);
	}

	@Override
	public InstantRecording recordingFor(InstantEvent event) {
		return new SimpleInstantRecording(event, listener);
	}
}
