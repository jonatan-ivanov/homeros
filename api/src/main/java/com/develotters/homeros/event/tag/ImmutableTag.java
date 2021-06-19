package com.develotters.homeros.event.tag;

import java.util.Objects;

import static java.util.Objects.requireNonNull;

public class ImmutableTag implements Tag {
	private final String key;
	private final String value;
	private final Cardinality cardinality;

	public ImmutableTag(String key, String value, Cardinality cardinality) {
		this.key = requireNonNull(key);
		this.value = requireNonNull(value);
		this.cardinality = requireNonNull(cardinality);
	}

	@Override
	public String getKey() {
		return this.key;
	}

	@Override
	public String getValue() {
		return this.value;
	}

	@Override
	public Cardinality getCardinality() {
		return this.cardinality;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		ImmutableTag that = (ImmutableTag) o;
		return key.equals(that.key) && value.equals(that.value);
	}

	@Override
	public int hashCode() {
		return Objects.hash(key, value);
	}

	@Override
	public String toString() {
		return "tag{" + this.key + "=" + this.value + "}";
	}
}
