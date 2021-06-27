package com.develotters.homeros.event;

import com.develotters.homeros.event.instant.InstantRecording;
import com.develotters.homeros.event.listener.RecordingListener;
import com.develotters.homeros.event.span.SpanRecording;

public class SoutRecordingListener implements RecordingListener<Void> {
	@Override
	public void onStart(SpanRecording<Void> spanRecording) {
		System.out.println("Starting: " + spanRecording);
	}

	@Override
	public void onStop(SpanRecording<Void> spanRecording) {
		System.out.println("Stopped: " + spanRecording);
	}

	@Override
	public void onError(SpanRecording<Void> spanRecording) {
		System.out.println("Oops, an error occurred!\n" + spanRecording);
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
