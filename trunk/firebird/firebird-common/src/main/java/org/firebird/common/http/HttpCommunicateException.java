/*
 * Copyright (c) 2009-2010, Young-Gue Bae
 * All rights reserved.
 */
package org.firebird.common.http;

/**
 * An exception that is thrown by http communicate client. 
 * This is used to wrap Spring's various exception
 * so it's checked or catched in the client-side layer.
 * 
 * @author Young-Gue Bae
 */
public class HttpCommunicateException extends Exception {

	private static final long serialVersionUID = -1720680700045973060L;

	/**
     * Constructor for HttpCommunicateException.
     *
     * @param message the exception message
     */
    public HttpCommunicateException(String message) {
        super(message);
    }
}
