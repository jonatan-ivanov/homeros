package com.develotters.homeros.event.exemplar;

public interface Exemplar {
	String getKey();
	String getValue();

	static Exemplar of(String key, String value) {
		return new ImmutableExemplar(key, value);
	}
}
