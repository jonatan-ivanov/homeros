package com.develotters.homeros.event.instant;

import java.util.Collections;

import com.develotters.homeros.event.tag.Tag;

public class NoOpInstantRecording implements InstantRecording {
	private static final InstantEvent EVENT = new NoOpInstantEvent();
	private static final Iterable<Tag> TAGS = Collections.emptyList();

	@Override
	public InstantEvent getEvent() {
		return EVENT;
	}

	@Override
	public Iterable<Tag> getTags() {
		return TAGS;
	}

	@Override
	public InstantRecording tag(Tag tag) {
		return this;
	}

	@Override
	public void record() {
	}

	static class NoOpInstantEvent implements InstantEvent {
		@Override
		public String getName() {
			return "noop";
		}

		@Override
		public String getDescription() {
			return "noop";
		}
	}
}
