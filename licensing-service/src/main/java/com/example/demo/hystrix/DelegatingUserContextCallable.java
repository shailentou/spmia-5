package com.example.demo.hystrix;

import java.util.concurrent.Callable;

import com.example.demo.util.UserContext;

public class DelegatingUserContextCallable<V> implements Callable<V> {

	private final Callable<V> delegate;
	private UserContext originalUserContext;

	public DelegatingUserContextCallable(Callable<V> delegate, UserContext userContext) {
		this.delegate = delegate;
		this.originalUserContext = userContext;
	}

	public static <V> Callable<V> create(Callable<V> delegate, UserContext userContext) {
		return new DelegatingUserContextCallable<V>(delegate, userContext);
	}

	@Override
	public V call() throws Exception {

		try {
			return delegate.call();
		} finally {
			this.originalUserContext = null;
		}
	}

}
