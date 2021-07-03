package com.develotters.homeros.event.listener;

import com.develotters.homeros.event.context.ContextFactory;
import com.develotters.homeros.event.instant.InstantRecording;
import com.develotters.homeros.event.interval.IntervalRecording;

public interface RecordingListener<T> extends ContextFactory<T> {
	void onStart(IntervalRecording<T> intervalRecording);
	void onStop(IntervalRecording<T> intervalRecording);
	void onError(IntervalRecording<T> intervalRecording);
	void record(InstantRecording instantRecording);
}
