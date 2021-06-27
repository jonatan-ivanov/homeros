package com.develotters.homeros.time;

import java.time.Instant;
import java.util.concurrent.TimeUnit;

/**
 * Mostly from: https://github.com/micrometer-metrics/micrometer/blob/main/micrometer-core/src/main/java/io/micrometer/core/instrument/Clock.java
 */
public interface Clock {
	Clock SYSTEM = new Clock() {
		@Override
		public long wallTime() {
			Instant instant = java.time.Clock.systemUTC().instant();
			return TimeUnit.SECONDS.toNanos(instant.getEpochSecond()) + instant.getNano();
		}

		@Override
		public long monotonicTime() {
			return System.nanoTime();
		}
	};

	/**
	 * Current wall time in nanoseconds since the epoch. Should not be used to determine durations.
	 *
	 * @return Wall time in nanoseconds
	 */
	long wallTime();

	/**
	 * Current time from a monotonic clock source. The value is only meaningful when compared with another snapshot to determine the elapsed time for an operation.
	 * The difference between two samples will have a unit of nanoseconds.
	 * The returned value is typically equivalent to System.nanoTime.
	 *
	 * @return Monotonic time in nanoseconds
	 */
	long monotonicTime();
}
