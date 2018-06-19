package com.ecity.skhg.rest;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import org.apache.cxf.jaxrs.provider.json.JSONProvider;
import org.apache.cxf.jaxrs.utils.HttpUtils;

public class SKHGAppServerJsonpProvider<T> extends JSONProvider<T> { 
	@Context 
	HttpServletRequest request; 

	@Override
	public void writeTo(T obj, Class<?> cls, Type genericType, Annotation[] anns, MediaType m, MultivaluedMap<String, Object> headers, OutputStream os) throws IOException { 
		String prefix = getContext().getHttpServletRequest().getParameter("callback"); 
		boolean hasPrefix = false;
		if( prefix != null && !prefix.isEmpty()){
			hasPrefix = true;
		}
		if (hasPrefix) { 
			os.write(prefix.getBytes(HttpUtils.getSetEncoding(m, headers, "UTF-8"))); 
			os.write('('); 
		} 
		
		super.writeTo(obj, cls, genericType, anns, m, headers, os); 
		if (hasPrefix) { 
			os.write(')'); 
		} 
	} 
} 



