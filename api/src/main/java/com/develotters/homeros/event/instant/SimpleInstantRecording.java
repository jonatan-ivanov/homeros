package com.develotters.homeros.event.instant;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

import com.develotters.homeros.event.listener.RecordingListener;
import com.develotters.homeros.event.tag.Tag;

public class SimpleInstantRecording implements InstantRecording {
	private final InstantEvent event;
	private final RecordingListener<?> listener;
	private final Set<Tag> tags = new LinkedHashSet<>();

	public SimpleInstantRecording(InstantEvent event, RecordingListener<?> listener) {
		this.event = event;
		this.listener = listener;
	}

	@Override
	public InstantEvent getEvent() {
		return this.event;
	}

	@Override
	public Iterable<Tag> getTags() {
		return Collections.unmodifiableSet(this.tags);
	}

	@Override
	public InstantRecording tag(Tag tag) {
		this.tags.add(tag);
		return this;
	}

	@Override
	public void record() {
		this.listener.record(this);
	}

	@Override
	public String toString() {
		return "{" +
				"event=" + event.getName() +
				", tags=" + tags +
				'}';
	}
}
