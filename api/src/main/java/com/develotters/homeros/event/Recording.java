package com.develotters.homeros.event;
import com.develotters.homeros.event.tag.Tag;

public interface Recording<E extends Event, R extends Recording<E, R>> {
	E getEvent();
	Iterable<Tag> getTags();
	R tag(Tag tag);
}
