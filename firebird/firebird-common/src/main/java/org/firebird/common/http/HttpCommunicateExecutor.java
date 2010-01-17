/*
 * Copyright (c) 2009-2010, Young-Gue Bae
 * All rights reserved.
 */
package org.firebird.common.http;

import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import javax.servlet.ServletContext;

/**
 * Http communicate executor.
 * 
 * @author Young-Gue Bae
 */
public class HttpCommunicateExecutor {    
 
    private static HttpCommunicateExecutor instance;
    private ServletContext context;

    /**
	 * Http communication executor.
	 * 
	 * @param context the servlet context
	 */
	public HttpCommunicateExecutor(ServletContext context) {
	    this.context = context;
	}

	/**
	 * Inializes the http communicate client.
	 *
	 * @param context the servlet context
	 */
	public static void init(ServletContext context) {
		if (instance == null) {
		    instance = new HttpCommunicateExecutor(context);
		}
	}

	/**
	 * Clients call this method to get a reference to the single instance.
	 */
	public static HttpCommunicateExecutor getInstance() throws Exception {
		if (instance == null)
			throw new Exception("HttpCommunicateExecutor is not initialized.");

		return instance;
	}
	
	public ServletContext getServletContext() {
	    return this.context;
	}
	
	/**
	 * Executes the class to invoke.
	 *
	 * @param comm the communicate model
	 * @return Object the object returned from server
	 */
	public Object execute(HttpCommunicate comm) throws Exception {
		try {
			String className = comm.getClassName();
			Object[] classParams = comm.getClassParams();
			
			String methodName = comm.getMethodName();
			Object[] methodParams = comm.getMethodParams();
	
			Object objTarget = loadClass(className, classParams);
			if(methodName == null)
				return objTarget;
	
			Method mTarget = getMethod(className, methodName, methodParams);
	
			Object result = mTarget.invoke(objTarget, methodParams);
			return result;
		} catch(InvocationTargetException e) {
		    System.out.println("HttpCommunicateExecutor :: "  + e.getMessage());
			e.printStackTrace();
			throw (Exception)(e.getTargetException());
		} catch(Exception e) {
			System.out.println("HttpCommunicateExecutor :: "  + e.getMessage());
		    e.printStackTrace();
		    throw e;
		}
	}

	/**
	 * Loads the server-side class dynamically.
	 *
	 * @param className the class name to invoke
	 * @param classParams the class params of constructor
	 * @return Object the loaded object
	 * @exception InvocationTargetException if it fails to invoke target class
	 * @exception Exception if it fails to load the class
	 */
	public Object loadClass(String className, Object[] classParams) throws InvocationTargetException, Exception {
		try {
			Class clsTarget = Class.forName(className);

			if (classParams.length == 0)
				return clsTarget.newInstance();

			// gets the type of constructor's arguments
			Class[] clsParams = getClass(classParams);
			// gets a constructor
			Constructor targetConstructor = clsTarget.getConstructor(clsParams);
			// make instance of target class
			Object obj = targetConstructor.newInstance(classParams);
			return obj;
		} catch(ClassNotFoundException e) {
		    e.printStackTrace();
			throw new Exception(e.getMessage());
		} catch(InstantiationException e) {
		    e.printStackTrace();
		    throw new Exception(e.getMessage());
		} catch(NoSuchMethodException e) {
		    e.printStackTrace();
		    throw new Exception(e.getMessage());
		} catch(IllegalAccessException e) {
		    e.printStackTrace();
		    throw new Exception(e.getMessage());
		} catch(Exception e) {
		    e.printStackTrace();
		    throw e;
		}
	}

	/**
	 * Gets the method.
	 *
	 * @param className the class name
	 * @param methodName the method name
	 * @param methodParams the method params
	 * @return Method the method object
	 */
	public Method getMethod(String className, String methodName, Object[] methodParams) throws Exception {
		try {
			Class clsTarget = Class.forName(className);
	
			// gets the type of parameters
			Class[] clsParams = getClass(methodParams);	
			// gets a method
			Method m = clsTarget.getMethod(methodName, clsParams);
			return m;
		} catch(ClassNotFoundException e) {
		    throw new Exception(e.getMessage());
		} catch(NoSuchMethodException e) {
		    throw new Exception(e.getMessage());
		} catch(Exception e) {
		    throw e;
		}
	}

	/**
	 * Gets the type of parameters.
	 */
	private Class[] getClass(Object[] params) {
		Class[] clsParams = new Class[params.length];
		for (int i = 0; i < params.length; i++) {
			Class cls = params[i].getClass();
			String clsType = cls.getName();

			if(clsType.equals("java.lang.Byte"))
				cls = Byte.TYPE;
			else if(clsType.equals("java.lang.Short"))
				cls = Short.TYPE;
			else if(clsType.equals("java.lang.Integer"))
				cls = Integer.TYPE;
			else if(clsType.equals("java.lang.Long"))
				cls = Long.TYPE;
			else if(clsType.equals("java.lang.Float"))
				cls = Float.TYPE;
			else if(clsType.equals("java.lang.Double"))
				cls = Double.TYPE;
			else if(clsType.equals("java.lang.Character"))
				cls = Character.TYPE;
			else if(clsType.equals("java.lang.Boolean"))
				cls = Boolean.TYPE;
			else if(cls.isArray()) {
				Class compType = cls.getComponentType();
				Object obj = Array.newInstance(compType,0);
				cls = obj.getClass();
			}
			clsParams[i] = cls;
		}

		return clsParams;
	}	
}
	