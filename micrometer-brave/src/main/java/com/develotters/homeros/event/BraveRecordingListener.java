package com.develotters.homeros.event;

import brave.Span;
import brave.Tracer;
import com.develotters.homeros.event.instant.InstantRecording;
import com.develotters.homeros.event.listener.RecordingListener;
import com.develotters.homeros.event.span.SpanRecording;

public class BraveRecordingListener implements RecordingListener<BraveRecordingListener.BraveContext> {
	private final Tracer tracer;

	public BraveRecordingListener(Tracer tracer) {
		this.tracer = tracer;
	}

	@Override
	public void onStart(SpanRecording<BraveContext> spanRecording) {
		// TODO: use start(long) instead?
		Span span = tracer.nextSpan().name(spanRecording.getEvent().getName()).start();
		spanRecording.getContext().setSpanInScope(tracer.withSpanInScope(span));
	}

	@Override
	public void onStop(SpanRecording<BraveContext> spanRecording) {
		Span span = tracer.currentSpan();
		spanRecording.getTags().forEach(tag -> span.tag(tag.getKey(), tag.getValue()));
		spanRecording.getContext().getSpanInScope().close();
		// TODO: use finish(long) instead?
		span.finish();
	}

	@Override
	public void onError(SpanRecording<BraveContext> spanRecording) {
		tracer.currentSpan().error(spanRecording.getError());
	}

	@Override
	public void record(InstantRecording instantRecording) {
		tracer.currentSpan().annotate(instantRecording.getEvent().getName());
	}

	@Override
	public BraveContext createContext() {
		return new BraveContext();
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
