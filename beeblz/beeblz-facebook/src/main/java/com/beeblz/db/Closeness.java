/**
 * Copyright (c) 2010, beeblz.com
 * All rights reserved.
 */
package com.beeblz.db;

import java.io.Serializable;

/**
 * This is a closeness model.
 * 
 * @author YoungGue Bae
 */
public class Closeness implements Serializable {
	
	private Integer seq;
	private String id1;
	private String id2;
	private String object;
	private String responseType;
	
	public Integer getSeq() {
		return seq;
	}
	public String getId1() {
		return id1;
	}
	public String getId2() {
		return id2;
	}
	public String getObject() {
		return object;
	}
	public String getResponseType() {
		return responseType;
	}
	public void setSeq(Integer seq) {
		this.seq = seq;
	}
	public void setId1(String id1) {
		this.id1 = id1;
	}
	public void setId2(String id2) {
		this.id2 = id2;
	}
	public void setObject(String object) {
		this.object = object;
	}
	public void setResponseType(String responseType) {
		this.responseType = responseType;
	}	

}
