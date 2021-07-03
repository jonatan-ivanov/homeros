package com.develotters.homeros.event;

import com.develotters.homeros.event.instant.InstantEvent;
import com.develotters.homeros.event.instant.InstantRecording;
import com.develotters.homeros.event.instant.NoOpInstantRecording;
import com.develotters.homeros.event.instant.SimpleInstantRecording;
import com.develotters.homeros.event.listener.RecordingListener;
import com.develotters.homeros.event.interval.NoOpIntervalRecording;
import com.develotters.homeros.event.interval.SimpleIntervalRecording;
import com.develotters.homeros.event.interval.IntervalEvent;
import com.develotters.homeros.event.interval.IntervalRecording;
import com.develotters.homeros.time.Clock;

public class SimpleRecorder<T> implements Recorder<T> {
	private final RecordingListener<T> listener;
	private final Clock clock;
	private volatile boolean enabled;

	public SimpleRecorder(RecordingListener<T> listener, Clock clock) {
		this.listener = listener;
		this.clock = clock;
		this.enabled = true;
	}

	@Override
	public IntervalRecording<T> recordingFor(IntervalEvent event) {
		return enabled ? new SimpleIntervalRecording<>(event, listener, clock) : new NoOpIntervalRecording<>();
	}

	@Override
	public InstantRecording recordingFor(InstantEvent event) {
		return enabled ? new SimpleInstantRecording(event, listener) : new NoOpInstantRecording();
	}

	@Override
	public boolean isEnabled() {
		return this.enabled;
	}

	@Override
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
}
