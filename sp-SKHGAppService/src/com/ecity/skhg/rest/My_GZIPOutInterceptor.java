package com.ecity.skhg.rest;

import org.apache.cxf.interceptor.Fault;
import org.apache.cxf.transport.common.gzip.GZIPOutInterceptor;

public class My_GZIPOutInterceptor extends GZIPOutInterceptor {
	
	public My_GZIPOutInterceptor() {
		super();		
	}

	public My_GZIPOutInterceptor(int threshold) {
		super(threshold);
	}
	
	public void handleMessage(org.apache.cxf.message.Message message)
			throws Fault {
		String s=(String)message.getContextualProperty("Content-Type");
		if(s==null){
			super.handleMessage(message);
			return;
		}
		s=s.toLowerCase();
		if(	s.startsWith("image/")==false &&
				s.contains("application/pdf")==false){
			super.handleMessage(message);
		}
	}
}