package com.develotters.homeros.event.context;

@FunctionalInterface
public interface ContextFactory<T> {
	T createContext();
}
