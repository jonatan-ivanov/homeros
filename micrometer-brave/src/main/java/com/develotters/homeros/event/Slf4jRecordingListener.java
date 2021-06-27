package com.develotters.homeros.event;

import com.develotters.homeros.event.instant.InstantRecording;
import com.develotters.homeros.event.listener.RecordingListener;
import com.develotters.homeros.event.span.SpanRecording;
import org.slf4j.Logger;

public class Slf4jRecordingListener implements RecordingListener<Void> {
	private final Logger logger;

	public Slf4jRecordingListener(Logger logger) {
		this.logger = logger;
	}

	@Override
	public void onStart(SpanRecording<Void> spanRecording) {
		logger.info("Starting: {}", spanRecording);
	}

	@Override
	public void onStop(SpanRecording<Void> spanRecording) {
		logger.info("Stopped: {}", spanRecording);
	}

	@Override
	public void onError(SpanRecording<Void> spanRecording) {
		logger.error("Oops, an error occurred!", spanRecording.getError());
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
