package com.develotters.homeros.event;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import brave.propagation.TraceContext;
import com.develotters.homeros.event.exemplar.Exemplar;
import com.develotters.homeros.event.instant.InstantRecording;
import com.develotters.homeros.event.listener.RecordingListener;
import com.develotters.homeros.event.interval.IntervalRecording;
import com.develotters.homeros.event.tag.Cardinality;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Tag;
import io.micrometer.core.instrument.Timer;

public class MicrometerRecordingListener implements RecordingListener<Void> {
	private final MeterRegistry registry;

	public MicrometerRecordingListener(MeterRegistry registry) {
		this.registry = registry;
	}

	@Override
	public void onStart(IntervalRecording<Void> intervalRecording) {
	}

	@Override
	public void onStop(IntervalRecording<Void> intervalRecording) {
		Timer.builder(intervalRecording.getEvent().getName())
				.description(intervalRecording.getEvent().getDescription())
				.tags(metricTags(intervalRecording))
				.tag("error", intervalRecording.getError() != null ? intervalRecording.getError().getClass().getSimpleName() : "none")
//				.exemplars(exemplars(intervalRecording)) // does not exist
//				.exemplars(exemplars(span.context())) // does not exist
				.register(registry)
				.record(intervalRecording.getDuration());
	}

	@Override
	public void onError(IntervalRecording<Void> intervalRecording) {
	}

	@Override
	public void record(InstantRecording instantRecording) {
		Counter.builder(instantRecording.getEvent().getName())
				.description(instantRecording.getEvent().getDescription())
				.tags(metricTags(instantRecording))
//				.exemplars(exemplars(instantRecording)) // does not exist
				.register(registry)
				.increment();
	}

	@Override
	public Void createContext() {
		return null;
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
