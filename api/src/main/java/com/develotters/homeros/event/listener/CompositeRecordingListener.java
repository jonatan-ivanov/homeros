package com.develotters.homeros.event.listener;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;

import com.develotters.homeros.event.instant.InstantRecording;
import com.develotters.homeros.event.span.SpanEvent;
import com.develotters.homeros.event.span.SpanRecording;
import com.develotters.homeros.event.tag.Tag;

public class CompositeRecordingListener implements RecordingListener<CompositeContext> {
	private final List<RecordingListener<?>> listeners;

	public CompositeRecordingListener(RecordingListener<?>... listeners) {
		this(Arrays.asList(listeners));
	}

	public CompositeRecordingListener(List<RecordingListener<?>> listeners) {
		this.listeners = listeners;
	}

	@Override
	public void onStart(SpanRecording<CompositeContext> spanRecording) {
		for (RecordingListener<?> listener : listeners) {
			listener.onStart(new SpanRecordingView<>(listener, spanRecording));
		}
	}

	@Override
	public void onStop(SpanRecording<CompositeContext> spanRecording) {
		for (RecordingListener<?> listener : listeners) {
			listener.onStop(new SpanRecordingView<>(listener, spanRecording));
		}
	}

	@Override
	public void onError(SpanRecording<CompositeContext> spanRecording) {
		for (RecordingListener<?> listener : listeners) {
			listener.onError(new SpanRecordingView<>(listener, spanRecording));
		}
	}

	@Override
	public void record(InstantRecording instantRecording) {
		for (RecordingListener<?> listener : listeners) {
			listener.record(instantRecording);
		}
	}

	@Override
	public CompositeContext createContext() {
		return new CompositeContext(this.listeners);
	}

	static class SpanRecordingView<T> implements SpanRecording<T> {
		private final RecordingListener<?> listener;
		private final SpanRecording<CompositeContext> delegate;

		SpanRecordingView(RecordingListener<?> listener, SpanRecording<CompositeContext> delegate) {
			this.listener = listener;
			this.delegate = delegate;
		}

		@Override
		public SpanEvent getEvent() {
			return this.delegate.getEvent();
		}

		@Override
		public Iterable<Tag> getTags() {
			return this.delegate.getTags();
		}

		@Override
		@SuppressWarnings("unchecked")
		public SpanRecording<T> tag(Tag tag) {
			return (SpanRecording<T>) this.delegate.tag(tag);
		}

		@Override
		public Duration getDuration() {
			return this.delegate.getDuration();
		}

		@Override
		public long getStartNanos() {
			return this.delegate.getStartNanos();
		}

		@Override
		public long getStopNanos() {
			return this.delegate.getStopNanos();
		}

		@Override
		public long getStartWallTime() {
			return this.delegate.getStartWallTime();
		}

		@Override
		@SuppressWarnings("unchecked")
		public SpanRecording<T> start() {
			return (SpanRecording<T>) this.delegate.start();
		}

		@Override
		public void stop() {
			this.delegate.stop();
		}

		@Override
		public Throwable getError() {
			return this.delegate.getError();
		}

		@Override
		@SuppressWarnings("unchecked")
		public SpanRecording<T> error(Throwable error) {
			return (SpanRecording<T>) this.delegate.error(error);
		}

		@Override
		@SuppressWarnings("unchecked")
		public T getContext() {
			return (T) this.delegate.getContext().byListener(listener);
		}

		@Override
		public String toString() {
			return this.delegate.toString();
		}
	}
}
