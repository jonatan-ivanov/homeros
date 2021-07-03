package com.develotters.homeros.event;

import java.util.concurrent.TimeUnit;

import brave.Span;
import brave.Tracer;
import com.develotters.homeros.event.instant.InstantRecording;
import com.develotters.homeros.event.listener.RecordingListener;
import com.develotters.homeros.event.interval.IntervalRecording;

public class BraveRecordingListener implements RecordingListener<BraveRecordingListener.BraveContext> {
	private final Tracer tracer;

	public BraveRecordingListener(Tracer tracer) {
		this.tracer = tracer;
	}

	@Override
	public void onStart(IntervalRecording<BraveContext> intervalRecording) {
		Span span = tracer.nextSpan()
				.name(intervalRecording.getEvent().getName())
				.start(getStartTimeInMicros(intervalRecording));
		intervalRecording.getContext().setSpanInScope(tracer.withSpanInScope(span));
	}

	@Override
	public void onStop(IntervalRecording<BraveContext> intervalRecording) {
		Span span = tracer.currentSpan();
		intervalRecording.getTags().forEach(tag -> span.tag(tag.getKey(), tag.getValue()));
		intervalRecording.getContext().getSpanInScope().close();
		span.finish(getStopTimeInMicros(intervalRecording));
	}

	@Override
	public void onError(IntervalRecording<BraveContext> intervalRecording) {
		tracer.currentSpan().error(intervalRecording.getError());
	}

	@Override
	public void record(InstantRecording instantRecording) {
		tracer.currentSpan().annotate(instantRecording.getEvent().getName());
	}

	@Override
	public BraveContext createContext() {
		return new BraveContext();
	}

	private long getStartTimeInMicros(IntervalRecording<BraveContext> intervalRecording) {
		return TimeUnit.NANOSECONDS.toMicros(intervalRecording.getStartWallTime());
	}

	private long getStopTimeInMicros(IntervalRecording<BraveContext> intervalRecording) {
		return TimeUnit.NANOSECONDS.toMicros(intervalRecording.getStartWallTime() + intervalRecording.getDuration().toNanos());
	}

	static class BraveContext {
		private Tracer.SpanInScope spanInScope;

		Tracer.SpanInScope getSpanInScope() {
			return spanInScope;
		}

		void setSpanInScope(Tracer.SpanInScope spanInScope) {
			this.spanInScope = spanInScope;
		}
	}
}
