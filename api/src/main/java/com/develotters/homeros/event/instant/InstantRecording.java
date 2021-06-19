package com.develotters.homeros.event.instant;

import com.develotters.homeros.event.Recording;

public interface InstantRecording extends Recording<InstantEvent, InstantRecording> {
	void record();
}
