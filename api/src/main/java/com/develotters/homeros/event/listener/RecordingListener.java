package com.develotters.homeros.event.listener;

import com.develotters.homeros.event.instant.InstantRecording;
import com.develotters.homeros.event.span.SpanRecording;

public interface RecordingListener<T> {
	void onStart(SpanRecording<T> spanRecording);
	void onStop(SpanRecording<T> spanRecording);
	void onError(SpanRecording<T> error);
	void record(InstantRecording instantRecording);

	static RecordingListener<?> NOOP() {
		return new RecordingListener<>() {
			@Override
			public void onStart(SpanRecording<Object> spanRecording) {}

			@Override
			public void onStop(SpanRecording<Object> spanRecording) {}

			@Override
			public void onError(SpanRecording<Object> error) {}

			@Override
			public void record(InstantRecording instantRecording) {}
		};
	}
}
