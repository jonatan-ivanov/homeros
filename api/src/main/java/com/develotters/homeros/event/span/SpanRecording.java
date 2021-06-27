package com.develotters.homeros.event.span;

import java.time.Duration;

import com.develotters.homeros.event.Recording;

public interface SpanRecording<T> extends Recording<SpanEvent, SpanRecording<T>> {
	Duration getDuration();
	long getStartNanos();
	long getStopNanos();
	long getStartTimeStamp();

	SpanRecording<T> start();
	void stop();

	Throwable getError();
	SpanRecording<T> error(Throwable error);

	T getContext();
}
