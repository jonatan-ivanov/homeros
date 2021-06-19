package com.develotters.homeros.examples.bookstore.event;

import com.develotters.homeros.event.instant.InstantEvent;

public enum ReparationEvent implements InstantEvent {
	NEW_TOOL("new-tool", "New tool was needed during reparation");

	private final String name;
	private final String description;

	ReparationEvent(String name, String description) {
		this.name = name;
		this.description = description;
	}

	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public String getDescription() {
		return this.description;
	}

	@Override
	public String toString() {
		return "ReparationEvent{" +
				"name='" + name + '\'' +
				", description='" + description + '\'' +
				'}';
	}
}
