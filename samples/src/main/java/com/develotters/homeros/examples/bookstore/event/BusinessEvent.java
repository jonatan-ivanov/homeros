package com.develotters.homeros.examples.bookstore.event;

import com.develotters.homeros.event.interval.IntervalEvent;

public enum BusinessEvent implements IntervalEvent {
	SELL("sell", "Sold something"),
	REPAIR("repair", "Repaired something"),
	BUY("buy", "Bought something");

	private final String name;
	private final String description;

	BusinessEvent(String name, String description) {
		this.name =  name;
		this.description =  description;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public String getDescription() {
		return description;
	}

	@Override
	public String toString() {
		return "BusinessEvent{" +
				"name='" + name + '\'' +
				", description='" + description + '\'' +
				'}';
	}
}
