package com.develotters.homeros.event.interval;

import java.time.Duration;

import com.develotters.homeros.event.Recording;

public interface IntervalRecording<T> extends Recording<IntervalEvent, IntervalRecording<T>> {
	Duration getDuration();
	long getStartNanos();
	long getStopNanos();
	long getStartWallTime();

	IntervalRecording<T> start();
	void stop();

	Throwable getError();
	IntervalRecording<T> error(Throwable error);

	T getContext();
}
