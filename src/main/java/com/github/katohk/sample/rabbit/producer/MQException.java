package com.github.katohk.sample.rabbit.producer;

public class MQException extends Exception {
	private static final long serialVersionUID = 1L;
	
	public MQException(String message) {
		super(message);
	}

	public MQException(String message, Throwable e) {
		super(message,e);
	}
}
