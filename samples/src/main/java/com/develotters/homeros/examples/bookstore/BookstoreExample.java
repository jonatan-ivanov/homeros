package com.develotters.homeros.examples.bookstore;

import java.io.IOException;

import brave.Tracing;
import com.develotters.homeros.event.BraveRecordingListener;
import com.develotters.homeros.event.MicrometerRecordingListener;
import com.develotters.homeros.event.Recorder;
import com.develotters.homeros.event.SoutRecordingListener;
import com.develotters.homeros.event.listener.CompositeContext;
import com.develotters.homeros.event.listener.CompositeRecordingListener;
import com.develotters.homeros.event.interval.IntervalRecording;
import com.develotters.homeros.event.tag.Tag;
import com.develotters.homeros.examples.prometheus.PrometheusServer;
import com.develotters.homeros.examples.zipkin.SoutSender;
import com.develotters.homeros.examples.bookstore.event.ReparationEvent;
import com.develotters.homeros.event.SimpleRecorder;
import com.develotters.homeros.time.Clock;
import io.micrometer.prometheus.PrometheusConfig;
import io.micrometer.prometheus.PrometheusMeterRegistry;
import zipkin2.reporter.brave.AsyncZipkinSpanHandler;
import zipkin2.reporter.okhttp3.OkHttpSender;

import static com.develotters.homeros.event.tag.Cardinality.HIGH;
import static com.develotters.homeros.event.tag.Cardinality.LOW;
import static com.develotters.homeros.examples.bookstore.event.BusinessEvent.BUY;
import static com.develotters.homeros.examples.bookstore.event.BusinessEvent.REPAIR;
import static com.develotters.homeros.examples.bookstore.event.BusinessEvent.SELL;

public class BookstoreExample {
//	private static final Logger logger = LoggerFactory.getLogger(BookstoreExample.class);
	private static final PrometheusMeterRegistry registry = new PrometheusMeterRegistry(PrometheusConfig.DEFAULT);
	private static final PrometheusServer prometheusServer = new PrometheusServer(registry);
	private static final AsyncZipkinSpanHandler soutSpanHandler = AsyncZipkinSpanHandler.create(new SoutSender());
	private static final AsyncZipkinSpanHandler zipkinSpanHandler = AsyncZipkinSpanHandler.create(OkHttpSender.create("http://localhost:9411/api/v2/spans"));
	private static final Tracing tracing = Tracing.newBuilder()
			.localServiceName("example-service")
			.addSpanHandler(soutSpanHandler)
//			.addSpanHandler(zipkinSpanHandler)
			.build();

	private static final Recorder<CompositeContext> recorder = new SimpleRecorder<>(
			new CompositeRecordingListener(
					new SoutRecordingListener(),
//					new Slf4jRecordingListener(logger),
					new BraveRecordingListener(tracing.tracer()),
					new MicrometerRecordingListener(registry)
			),
			Clock.SYSTEM
	);

	public static void main(String[] args) {
		prometheusServer.start();
		Runtime.getRuntime().addShutdownHook(new Thread(BookstoreExample::shutdown));

		doBusiness();
		recorder.setEnabled(false);
		System.out.println("Recording enabled: " + recorder.isEnabled());
		doBusiness();

		System.out.println();
		System.out.println("----- METRICS -----");
		System.out.println(registry.scrape());
	}

	private static void doBusiness() {
		buy("978-0134685991", "Joshua Bloch", "Effective Java", true);
		buy("978-0321349606", "Brian Goetz", "Java Concurrency in Practice", true);
		buy("978-0345391803", "Douglas Adams", "The Ultimate Hitchhiker's Guide to the Galaxy", false);

		sell("978-0134685991", "Joshua Bloch", "Effective Java");
		sell("978-0321349606", "Brian Goetz", "Java Concurrency in Practice");
	}

	private static void buy(String isbn, String author, String title, boolean reparable) {
		IntervalRecording<CompositeContext> recording = recorder.recordingFor(BUY)
				.tag(Tag.of("isbn", isbn, HIGH))
				.tag(Tag.of("author", author, LOW))
				.tag(Tag.of("title", title, HIGH));

		try {
			recording.start();
			simulateBusiness();
			repair(isbn, author, title, reparable);
		}
		catch (Throwable error) {
			recording.error(error);
		}
		finally {
			recording.stop();
		}
	}

	private static void sell(String isbn, String author, String title) {
		IntervalRecording<CompositeContext> recording = recorder.recordingFor(SELL)
				.tag(Tag.of("isbn", isbn, HIGH))
				.tag(Tag.of("author", author, LOW))
				.tag(Tag.of("title", title, HIGH));

		try {
			recording.start();
			simulateBusiness();
		}
		catch (Throwable error) {
			recording.error(error);
		}
		finally {
			recording.stop();
		}
	}

	private static void repair(String isbn, String author, String title, boolean reparable) throws Throwable {
		IntervalRecording<CompositeContext> recording = recorder.recordingFor(REPAIR)
				.tag(Tag.of("isbn", isbn, HIGH))
				.tag(Tag.of("author", author, LOW))
				.tag(Tag.of("title", title, HIGH));

		try {
			recording.start();
			recorder.recordingFor(ReparationEvent.NEW_TOOL)
					.tag(Tag.of("isbn", isbn, HIGH))
					.tag(Tag.of("author", author, LOW))
					.tag(Tag.of("title", title, HIGH))
					.record();
			simulateRepair(reparable);
		}
		catch (Throwable error) {
			recording.error(error);
			throw error;
		}
		finally {
			recording.stop();
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
