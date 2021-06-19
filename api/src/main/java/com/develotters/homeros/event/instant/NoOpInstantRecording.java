package com.develotters.homeros.event.instant;

import java.util.Collections;

import com.develotters.homeros.event.tag.Tag;

public class NoOpInstantRecording implements InstantRecording {
	@Override
	public InstantEvent getEvent() {
		return new InstantEvent() {
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
	public InstantRecording tag(Tag tag) {
		return this;
	}

	@Override
	public void record() {
	}
}
