package com.develotters.homeros.event.interval;

import java.time.Duration;
import java.util.Collections;

import com.develotters.homeros.event.tag.Tag;

public class NoOpIntervalRecording<T> implements IntervalRecording<T> {
	@Override
	public IntervalEvent getEvent() {
		return new IntervalEvent() {
			@Override
			public String getName() {
				return "noop";
			}

			@Override
			public String getDescription() {
				return "noop";
			}
		};
	}

	@Override
	public Iterable<Tag> getTags() {
		return Collections.emptyList();
	}

	@Override
	public IntervalRecording<T> tag(Tag tag) {
		return this;
	}

	@Override
	public Duration getDuration() {
		return Duration.ZERO;
	}

	@Override
	public long getStartNanos() {
		return 0;
	}

	@Override
	public long getStopNanos() {
		return 0;
	}

	@Override
	public long getStartWallTime() {
		return 0;
	}

	@Override
	public IntervalRecording<T> start() {
		return this;
	}

	@Override
	public void stop() {
	}

	@Override
	public Throwable getError() {
		return null;
	}

	@Override
	public IntervalRecording<T> error(Throwable error) {
		return this;
	}

	@Override
	public T getContext() {
		return null;
	}
}
