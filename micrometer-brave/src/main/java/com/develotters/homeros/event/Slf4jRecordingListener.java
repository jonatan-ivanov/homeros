package com.develotters.homeros.event;

import com.develotters.homeros.event.instant.InstantRecording;
import com.develotters.homeros.event.listener.RecordingListener;
import com.develotters.homeros.event.interval.IntervalRecording;
import org.slf4j.Logger;

public class Slf4jRecordingListener implements RecordingListener<Void> {
	private final Logger logger;

	public Slf4jRecordingListener(Logger logger) {
		this.logger = logger;
	}

	@Override
	public void onStart(IntervalRecording<Void> intervalRecording) {
		logger.info("Started: {}", intervalRecording);
	}

	@Override
	public void onStop(IntervalRecording<Void> intervalRecording) {
		logger.info("Stopped: {}", intervalRecording);
	}

	@Override
	public void onError(IntervalRecording<Void> intervalRecording) {
		logger.error("Oops, an error occurred!", intervalRecording.getError());
	}

	@Override
	public void record(InstantRecording instantRecording) {
		logger.info("Event: {}", instantRecording);
	}

	@Override
	public Void createContext() {
		return null;
	}
}
