package com.develotters.homeros.event.tag;

public interface Tag {
	String getKey();
	String getValue();
	Cardinality getCardinality();

	static Tag of(String key, String value, Cardinality cardinality) {
		return new ImmutableTag(key, value, cardinality);
	}
}
