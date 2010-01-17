/*
 * Copyright (c) 2009-2010, Young-Gue Bae
 * All rights reserved.
 */
package org.firebird.common.http;

import java.io.Serializable;

/**
 * Http communicate model.
 * 
 * @author Young-Gue Bae
 */
public class HttpCommunicate implements Serializable {

	private static final long serialVersionUID = -3902736071245349086L;
	private String className, methodName;
	private Object[] classParams, methodParams;
	
	/**
	 * Default constructor.
	 *
	 */
	public HttpCommunicate() {}

	/**
	 * Constructor.
	 *
     * @param className the class name
	 */
	public HttpCommunicate(String className) {
		this.className = className;
		this.classParams = new Object[]{};
		this.methodName = null;
		this.methodParams = new Object[]{};
	}

	/**
	 * Constructor.
	 *
     * @param className the class name
     * @param classParams the class params
	 */
	public HttpCommunicate(String className, Object[] classParams) {
		this.className = className;
		this.classParams = classParams;
		this.methodName = null;
		this.methodParams = new Object[]{};
	}

	/**
	 * Constructor.
	 *
     * @param className the class name
     * @param methodName the method name
	 */
	public HttpCommunicate(String className, String methodName) {
		this.className = className;
		this.classParams = new Object[]{};
		this.methodName = methodName;
		this.methodParams = new Object[]{};
	}

	/**
	 * Constructor.
	 *
     * @param className the class name
     * @param methodName the method name
     * @param methodParams the method parameters
	 */
	public HttpCommunicate(String className, String methodName, Object[] methodParams) {
		this.className = className;
		this.classParams = new Object[]{};
		this.methodName = methodName;
		this.methodParams = methodParams;
	}

	/**
	 * Constructor.
	 *
     * @param className the class name
     * @param classParams the initial arguments
     * @param methodName the method name
	 */
	public HttpCommunicate(String className, Object[] classParams, String methodName) {
		this.className = className;
		this.classParams = classParams;
		this.methodName = methodName;
		this.methodParams = new Object[]{};
	}

	/**
	 * Constructor.
	 *
     * @param className the class name
     * @param classParams the initial arguments
     * @param methodName the method name
     * @param methodParams the method parameters
	 */
	public HttpCommunicate(String className, Object[] classParams, String methodName, Object[] methodParams) {
		this.className = className;
		this.classParams = classParams;
		this.methodName = methodName;
		this.methodParams = methodParams;
	}

    /**
     * Sets the class name.
     *
     * @param className the class name
     */
	public void setClassName(String className) {
		this.className = className;
	}

    /**
     * Sets the class params.
     *
     * @param classParams the class params
     */
	public void setClassParams(Object[] classParams) {
		this.classParams = classParams;
	}

    /**
     * Sets the method name.
     *
     * @param methodName the method name
     */
	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}

    /**
     * Sets the method parameters.
     *
     * @param methodParams the method parameters
     */
	public void setMethodParams(Object[] methodParams) {
		this.methodParams = methodParams;
	}
	
    /**
     * Gets the class name.
     *
     */
	public String getClassName() {
		return className;
	}

    /**
     * Gets the class params.
     *
     */
	public Object[] getClassParams() {
		return classParams;
	}

    /**
     * Gets the method name.
     *
     */
	public String getMethodName() {
		return methodName;
	}

    /**
     * Gets the method parameters.
     *
     */
	public Object[] getMethodParams() {
		return methodParams;
	}
}
