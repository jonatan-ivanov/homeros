package com.develotters.homeros.event.lmt;

import brave.Tracer;

class LmtContext {
	private Tracer.SpanInScope spanInScope;

	Tracer.SpanInScope getSpanInScope() {
		return spanInScope;
	}

	void setSpanInScope(Tracer.SpanInScope spanInScope) {
		this.spanInScope = spanInScope;
	}
}
