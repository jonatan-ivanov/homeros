package com.develotters.homeros.time;

/**
 * From: https://github.com/micrometer-metrics/micrometer/blob/main/micrometer-core/src/main/java/io/micrometer/core/instrument/Clock.java
 */
public interface Clock {
	Clock SYSTEM = new Clock() {
		@Override
		public long wallTime() {
			return System.currentTimeMillis();
		}

		@Override
		public long monotonicTime() {
			return System.nanoTime();
		}
	};

	/**
	 * Current wall time in milliseconds since the epoch. Typically equivalent to System.currentTimeMillis.
	 * Should not be used to determine durations.
	 *
	 * @return Wall time in milliseconds
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
