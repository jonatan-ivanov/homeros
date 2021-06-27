package com.develotters.homeros.event;

import com.develotters.homeros.event.instant.InstantEvent;
import com.develotters.homeros.event.instant.InstantRecording;
import com.develotters.homeros.event.span.SpanEvent;
import com.develotters.homeros.event.span.SpanRecording;

public interface Recorder<T> {
	SpanRecording<T> recordingFor(SpanEvent event);
	InstantRecording recordingFor(InstantEvent event);
	boolean isEnabled();
	void setEnabled(boolean enabled);
}
