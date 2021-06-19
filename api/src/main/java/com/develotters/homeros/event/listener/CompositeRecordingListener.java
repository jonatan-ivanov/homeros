package com.develotters.homeros.event.listener;

import java.util.Arrays;
import java.util.List;

import com.develotters.homeros.event.instant.InstantRecording;
import com.develotters.homeros.event.span.SpanRecording;

public class CompositeRecordingListener<T> implements RecordingListener<T> {
	private final List<RecordingListener<T>> listeners;

	public CompositeRecordingListener(RecordingListener<T>... listeners) {
		this(Arrays.stream(listeners).toList());
	}

	public CompositeRecordingListener(List<RecordingListener<T>> listeners) {
		this.listeners = listeners;
	}

	@Override
	public void onStart(SpanRecording<T> spanRecording) {
		listeners.forEach(listener -> listener.onStart(spanRecording));
	}

	@Override
	public void onStop(SpanRecording<T> spanRecording) {
		listeners.forEach(listener -> listener.onStop(spanRecording));
	}

	@Override
	public void onError(SpanRecording<T> spanRecording) {
		listeners.forEach(listener -> listener.onError(spanRecording));
	}

	@Override
	public void record(InstantRecording instantRecording) {
		listeners.forEach(listener -> listener.record(instantRecording));
	}
}
