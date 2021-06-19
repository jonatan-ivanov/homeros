package com.develotters.homeros.event.listener;

import com.develotters.homeros.event.instant.InstantRecording;
import com.develotters.homeros.event.span.SpanRecording;

public interface RecordingListener<T> {
	void onStart(SpanRecording<T> spanRecording);
	void onStop(SpanRecording<T> spanRecording);
	void onError(SpanRecording<T> spanRecording);
	void record(InstantRecording instantRecording);
}
