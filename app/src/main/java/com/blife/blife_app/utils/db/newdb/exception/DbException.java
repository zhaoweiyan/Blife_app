package com.blife.blife_app.utils.db.newdb.exception;

/**
 * 执行异常,extands Exception
 * 
 * @author ByZhao </br> Date: 13-11-18
 */
public class DbException extends Exception {
	private static final long serialVersionUID = 1L;

	public DbException() {
	}

	public DbException(String detailMessage) {
		super(detailMessage);
	}

	public DbException(String detailMessage, Throwable throwable) {
		super(detailMessage, throwable);
	}

	public DbException(Throwable throwable) {
		super(throwable);
	}
}
