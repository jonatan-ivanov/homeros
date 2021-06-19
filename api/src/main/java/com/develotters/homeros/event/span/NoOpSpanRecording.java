package com.develotters.homeros.event.span;

import java.time.Duration;
import java.util.Collections;

import com.develotters.homeros.event.tag.Tag;

public class NoOpSpanRecording<T> implements SpanRecording<T> {
	@Override
	public SpanEvent getEvent() {
		return new SpanEvent() {
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
	public SpanRecording<T> tag(Tag tag) {
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
	public SpanRecording<T> start() {
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
	public SpanRecording<T> error(Throwable error) {
		return this;
	}

	@Override
	public T getContext() {
		return null;
	}
}
