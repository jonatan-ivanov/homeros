package com.develotters.homeros.event.exemplar;

import java.util.Objects;

import static java.util.Objects.requireNonNull;

public class ImmutableExemplar implements Exemplar {
	private final String key;
	private final String value;

	public ImmutableExemplar(String key, String value) {
		this.key = requireNonNull(key);
		this.value = requireNonNull(value);
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
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		ImmutableExemplar that = (ImmutableExemplar) o;
		return this.key.equals(that.key) && this.value.equals(that.value);
	}

	@Override
	public int hashCode() {
		return Objects.hash(this.key, this.value);
	}

	@Override
	public String toString() {
		return "exemplar{" + this.key + "=" + this.value + "}";
	}
}
