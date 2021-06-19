package com.develotters.homeros.examples.prometheus;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import io.micrometer.prometheus.PrometheusMeterRegistry;

public class PrometheusServer {
	private final PrometheusMeterRegistry registry;
	private final HttpServer server;

	public PrometheusServer(PrometheusMeterRegistry registry) {
		this(registry, 8080, "/prometheus");
	}

	public PrometheusServer(PrometheusMeterRegistry registry, int port, String path) {
		this.registry = registry;
		try {
			this.server = HttpServer.create(new InetSocketAddress(port), 0);
			this.server.createContext(path, this::httpHandler);
		}
		catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public void start() {
		new Thread(this.server::start).start();
	}

	public void stop() {
		this.server.stop(5);
	}

	private void httpHandler(HttpExchange exchange) throws IOException {
		byte[] response = this.registry.scrape().getBytes();
		exchange.sendResponseHeaders(200, response.length);
		try (OutputStream os = exchange.getResponseBody()) {
			os.write(response);
		}
	}
}
