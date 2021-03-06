package com.develotters.homeros.event.interval;

import java.time.Duration;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

import com.develotters.homeros.event.listener.RecordingListener;
import com.develotters.homeros.event.tag.Tag;
import com.develotters.homeros.time.Clock;

public class SimpleIntervalRecording<T> implements IntervalRecording<T> {
	private final IntervalEvent event;
	private final RecordingListener<T> listener;
	private final T context;
	private final Clock clock;

	private Duration duration = Duration.ZERO;
	private long started = 0;
	private long stopped = 0;
	private long startWallTime = 0;
	private final Set<Tag> tags = new LinkedHashSet<>();
	private Throwable error = null;

	public SimpleIntervalRecording(IntervalEvent event, RecordingListener<T> listener, Clock clock) {
		this.event = event;
		this.listener = listener;
		this.context = listener.createContext();
		this.clock = clock;
	}

	@Override
	public IntervalEvent getEvent() {
		return this.event;
	}

	@Override
	public Duration getDuration() {
		return this.duration;
	}

	@Override
	public long getStartNanos() {
		return this.started;
	}

	@Override
	public IntervalRecording<T> start() {
		if (this.started != 0) {
			throw new IllegalStateException("IntervalRecording has already been started");
		}

		this.startWallTime = clock.wallTime();
		this.started = clock.monotonicTime();
		this.listener.onStart(this);

		return this;
	}

	@Override
	public long getStopNanos() {
		return this.stopped;
	}

	@Override
	public long getStartWallTime() {
		return this.startWallTime;
	}

	@Override
	public void stop() {
		if (this.started == 0) {
			throw new IllegalStateException("IntervalRecording hasn't been started");
		}
		checkIfStopped();
		this.stopped = clock.monotonicTime();
		this.duration = Duration.ofNanos(this.stopped - this.started);
		this.listener.onStop(this);
	}

	@Override
	public Iterable<Tag> getTags() {
		return Collections.unmodifiableSet(this.tags);
	}

	@Override
	public IntervalRecording<T> tag(Tag tag) {
		checkIfStopped();
		this.tags.add(tag);
		return this;
	}

	@Override
	public Throwable getError() {
		return this.error;
	}

	@Override
	public IntervalRecording<T> error(Throwable error) {
		checkIfStopped();
		this.error = error;
		this.listener.onError(this);
		return this;
	}

	@Override
	public T getContext() {
		return this.context;
	}

	@Override
	public String toString() {
		return "{" +
				"event=" + event.getName() +
				", duration=" + duration.toMillis() + "ms" +
				", tags=" + tags +
				", error=" + error +
				'}';
	}

	private void checkIfStopped() {
		if (this.stopped != 0) {
			throw new IllegalStateException("IntervalRecording has already been stopped");
		}
	}
}
