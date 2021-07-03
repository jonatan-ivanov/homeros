package com.develotters.homeros.event;

import com.develotters.homeros.event.instant.InstantRecording;
import com.develotters.homeros.event.listener.RecordingListener;
import com.develotters.homeros.event.interval.IntervalRecording;

public class SoutRecordingListener implements RecordingListener<Void> {
	@Override
	public void onStart(IntervalRecording<Void> intervalRecording) {
		System.out.println("Started: " + intervalRecording);
	}

	@Override
	public void onStop(IntervalRecording<Void> intervalRecording) {
		System.out.println("Stopped: " + intervalRecording);
	}

	@Override
	public void onError(IntervalRecording<Void> intervalRecording) {
		System.out.println("Oops, an error occurred!\n" + intervalRecording);
	}

	@Override
	public void record(InstantRecording instantRecording) {
		System.out.println("Event: " + instantRecording);
	}

	@Override
	public Void createContext() {
		return null;
	}
}
