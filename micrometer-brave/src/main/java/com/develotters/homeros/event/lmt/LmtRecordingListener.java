package com.develotters.homeros.event.lmt;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import brave.Span;
import brave.Tracer;
import brave.propagation.TraceContext;
import com.develotters.homeros.event.Recording;
import com.develotters.homeros.event.instant.InstantRecording;
import com.develotters.homeros.event.listener.RecordingListener;
import com.develotters.homeros.event.span.SpanRecording;
import com.develotters.homeros.event.exemplar.Exemplar;
import com.develotters.homeros.event.tag.Cardinality;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Tag;
import io.micrometer.core.instrument.Timer;
import org.slf4j.Logger;

public class LmtRecordingListener implements RecordingListener<LmtContext> {
	private final Logger logger;
	private final MeterRegistry registry;
	private final Tracer tracer;

	public LmtRecordingListener(Logger logger, MeterRegistry registry, Tracer tracer) {
		this.logger = logger;
		this.registry = registry;
		this.tracer = tracer;
	}

	@Override
	public void onStart(SpanRecording<LmtContext> spanRecording) {
//		logger.info("Starting: {}", spanRecording);
//		System.out.println("Starting: " + spanRecording);

		// TODO: use start(long) instead?
		Span span = tracer.nextSpan().name(spanRecording.getEvent().getName()).start();
		spanRecording.getContext().setSpanInScope(tracer.withSpanInScope(span));
	}

	@Override
	public void onStop(SpanRecording<LmtContext> spanRecording) {
//		logger.info("Stopped: {}", spanRecording);
//		System.out.println("Stopped: " + spanRecording);

		Span span = tracer.currentSpan();
		spanRecording.getTags().forEach(tag -> span.tag(tag.getKey(), tag.getValue()));
		spanRecording.getContext().getSpanInScope().close();
//		List<Exemplar> spanExemplars = exemplars(span.context());
		// TODO: use finish(long) instead?
		span.finish();

		Timer.builder(spanRecording.getEvent().getName())
				.description(spanRecording.getEvent().getDescription())
				.tags(metricTags(spanRecording))
				.tag("error", spanRecording.getError() != null ? spanRecording.getError().getClass().getSimpleName() : "none")
//				.exemplars(exemplars(spanRecording)) // does not exist
//				.exemplars(spanExemplars) // does not exist
				.register(registry)
				.record(spanRecording.getDuration());
	}

	@Override
	public void onError(SpanRecording<LmtContext> spanRecording) {
//		logger.error("Oops", spanRecording.getError());
//		System.out.println("Oops: " + spanRecording);

		tracer.currentSpan().error(spanRecording.getError());
	}

	@Override
	public void record(InstantRecording instantRecording) {
//		System.out.println("Instantaneous event: " + instantRecording);

		tracer.currentSpan().annotate(instantRecording.getEvent().getName());
		Counter.builder(instantRecording.getEvent().getName())
				.description(instantRecording.getEvent().getDescription())
				.tags(metricTags(instantRecording))
//				.exemplars(exemplars(instantRecording)) // does not exist
				.register(registry)
				.increment();
	}

	private List<Tag> metricTags(Recording<?, ?> recording) {
		return StreamSupport.stream(recording.getTags().spliterator(), false)
				.filter(tag -> tag.getCardinality() == Cardinality.LOW)
				.map(tag -> Tag.of(tag.getKey(), tag.getValue()))
				.collect(Collectors.toList());
	}

	private List<Exemplar> exemplars(Recording<?, ?> recording) {
		return StreamSupport.stream(recording.getTags().spliterator(), false)
				.filter(tag -> tag.getCardinality() == Cardinality.HIGH)
				.map(tag -> Exemplar.of(tag.getKey(), tag.getValue()))
				.collect(Collectors.toList());
	}

	private List<Exemplar> exemplars(TraceContext context) {
		return List.of(
				Exemplar.of("traceId", context.traceIdString()),
				Exemplar.of("parentId", context.parentIdString()),
				Exemplar.of("spanId", context.spanIdString())
		);
	}
}
