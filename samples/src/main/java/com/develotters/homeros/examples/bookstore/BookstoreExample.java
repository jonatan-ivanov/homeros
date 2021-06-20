package com.develotters.homeros.examples.bookstore;

import java.io.IOException;

import brave.Tracing;
import com.develotters.homeros.event.Recorder;
import com.develotters.homeros.event.lmt.LmtRecordingListener;
import com.develotters.homeros.event.span.SpanRecording;
import com.develotters.homeros.event.tag.Tag;
import com.develotters.homeros.examples.prometheus.PrometheusServer;
import com.develotters.homeros.examples.zipkin.SoutSender;
import com.develotters.homeros.examples.bookstore.event.ReparationEvent;
import com.develotters.homeros.event.SimpleRecorder;
import com.develotters.homeros.time.Clock;
import io.micrometer.prometheus.PrometheusConfig;
import io.micrometer.prometheus.PrometheusMeterRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import zipkin2.reporter.brave.AsyncZipkinSpanHandler;
import zipkin2.reporter.okhttp3.OkHttpSender;

import static com.develotters.homeros.event.tag.Cardinality.HIGH;
import static com.develotters.homeros.event.tag.Cardinality.LOW;
import static com.develotters.homeros.examples.bookstore.event.BusinessEvent.BUY;
import static com.develotters.homeros.examples.bookstore.event.BusinessEvent.REPAIR;
import static com.develotters.homeros.examples.bookstore.event.BusinessEvent.SELL;

public class BookstoreExample {
	private static final Logger logger = LoggerFactory.getLogger(BookstoreExample.class);
	private static final PrometheusMeterRegistry registry = new PrometheusMeterRegistry(PrometheusConfig.DEFAULT);
	private static final PrometheusServer prometheusServer = new PrometheusServer(registry);
	private static final AsyncZipkinSpanHandler soutSpanHandler = AsyncZipkinSpanHandler.create(new SoutSender());
	private static final AsyncZipkinSpanHandler zipkinSpanHandler = AsyncZipkinSpanHandler.create(OkHttpSender.create("http://localhost:9411/api/v2/spans"));
	private static final Tracing tracing = Tracing.newBuilder()
			.localServiceName("example-service")
			.addSpanHandler(soutSpanHandler)
			.addSpanHandler(zipkinSpanHandler)
			.build();

	private static final Recorder<?> recorder = new SimpleRecorder<>(
			new LmtRecordingListener(logger, registry, tracing.tracer()),
			Clock.SYSTEM
	);

	public static void main(String[] args) {
		prometheusServer.start();
		Runtime.getRuntime().addShutdownHook(new Thread(BookstoreExample::shutdown));

		buy("978-0134685991", "Joshua Bloch", "Effective Java", true);
		buy("978-0321349606", "Brian Goetz", "Java Concurrency in Practice", true);
		buy("978-0345391803", "Douglas Adams", "The Ultimate Hitchhiker's Guide to the Galaxy", false);

		sell("978-0134685991", "Joshua Bloch", "Effective Java");
		sell("978-0321349606", "Brian Goetz", "Java Concurrency in Practice");

		System.out.println();
		System.out.println("----- METRICS -----");
		System.out.println(registry.scrape());
	}

	private static void buy(String isbn, String author, String title, boolean reparable) {
		SpanRecording<?> spanRecording = recorder.recordingFor(BUY)
				.tag(Tag.of("isbn", isbn, HIGH))
				.tag(Tag.of("author", author, LOW))
				.tag(Tag.of("title", title, HIGH));

		try {
			spanRecording.start();
			simulateBusiness();
			repair(isbn, author, title, reparable);
		}
		catch (Throwable error) {
			spanRecording.error(error);
		}
		finally {
			spanRecording.stop();
		}
	}

	private static void sell(String isbn, String author, String title) {
		SpanRecording<?> spanRecording = recorder.recordingFor(SELL)
				.tag(Tag.of("isbn", isbn, HIGH))
				.tag(Tag.of("author", author, LOW))
				.tag(Tag.of("title", title, HIGH));

		try {
			spanRecording.start();
			simulateBusiness();
		}
		catch (Throwable error) {
			spanRecording.error(error);
		}
		finally {
			spanRecording.stop();
		}
	}

	private static void repair(String isbn, String author, String title, boolean reparable) throws Throwable {
		SpanRecording<?> spanRecording = recorder.recordingFor(REPAIR)
				.tag(Tag.of("isbn", isbn, HIGH))
				.tag(Tag.of("author", author, LOW))
				.tag(Tag.of("title", title, HIGH));

		try {
			spanRecording.start();
			recorder.recordingFor(ReparationEvent.NEW_TOOL)
					.tag(Tag.of("isbn", isbn, HIGH))
					.tag(Tag.of("author", author, LOW))
					.tag(Tag.of("title", title, HIGH))
					.record();
			simulateRepair(reparable);
		}
		catch (Throwable error) {
			spanRecording.error(error);
			throw error;
		}
		finally {
			spanRecording.stop();
		}
	}

	private static void simulateBusiness() throws Throwable {
		Thread.sleep((long)(Math.random() * 100 + 100));
//		Thread.sleep(100);
	}

	private static void simulateRepair(boolean reparable) throws Throwable {
		Thread.sleep((long)(Math.random() * 100 + 100));
//		Thread.sleep(1_000);
		if (!reparable) {
			throw new IOException("simulated");
		}
	}

	private static void shutdown() {
		zipkinSpanHandler.close();
		prometheusServer.stop();
	}
}
