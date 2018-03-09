package com.eg.baba.cms.alert;

import java.io.Serializable;

public class CMSAlert implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private final int type;
	private final CMSAlertMessage msg;

	public CMSAlert(int type, CMSAlertMessage msg) {
		super();
		
		this.type = type;
		this.msg = msg;
	}

	public int getType() {
		return type;
	}
	
	public CMSAlertMessage getMsg() {
		return msg;
	}
	
}
