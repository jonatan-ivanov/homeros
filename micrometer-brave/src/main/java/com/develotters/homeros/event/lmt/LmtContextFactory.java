package com.develotters.homeros.event.lmt;

import com.develotters.homeros.event.context.ContextFactory;

public class LmtContextFactory implements ContextFactory<LmtContext> {
	@Override
	public LmtContext createContext() {
		return new LmtContext();
	}
}
