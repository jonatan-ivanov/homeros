package com.develotters.homeros.event;

import com.develotters.homeros.event.instant.InstantEvent;
import com.develotters.homeros.event.instant.InstantRecording;
import com.develotters.homeros.event.interval.IntervalEvent;
import com.develotters.homeros.event.interval.IntervalRecording;

public interface Recorder<T> {
	IntervalRecording<T> recordingFor(IntervalEvent event);
	InstantRecording recordingFor(InstantEvent event);
	boolean isEnabled();
	void setEnabled(boolean enabled);
}
