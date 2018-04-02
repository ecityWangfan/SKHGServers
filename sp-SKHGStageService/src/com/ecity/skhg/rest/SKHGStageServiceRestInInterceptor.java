package com.ecity.skhg.rest;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.ws.rs.InternalServerErrorException;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import org.apache.cxf.interceptor.Fault;
import org.apache.cxf.jaxrs.impl.UriInfoImpl;
import org.apache.cxf.jaxrs.provider.ProviderFactory;
import org.apache.cxf.jaxrs.utils.ExceptionUtils;
import org.apache.cxf.jaxrs.utils.JAXRSUtils;
import org.apache.cxf.message.Message;
import org.apache.cxf.phase.AbstractPhaseInterceptor;
import org.apache.cxf.phase.Phase;

public class SKHGStageServiceRestInInterceptor extends AbstractPhaseInterceptor<Message> {

    private static final String ACCEPT_QUERY = "f";
	
	private static final Map<String, String> SHORTCUTS;
	static {
	        SHORTCUTS = new HashMap<String, String>();
	        SHORTCUTS.put("image", "image/png");
	        SHORTCUTS.put("pjson", "application/json");
	        SHORTCUTS.put("json", "application/json");
	        SHORTCUTS.put("text", "text/*");
	        SHORTCUTS.put("xml", "application/xml");
	        SHORTCUTS.put("atom", "application/atom+xml");
	        SHORTCUTS.put("html", "text/html");
	        SHORTCUTS.put("pjson", "application/javascript");
	        SHORTCUTS.put("jsonp", "application/javascript");
	}
	    
	public SKHGStageServiceRestInInterceptor() {
        super(Phase.UNMARSHAL);
    }

	
    @Override
    public void handleFault(Message message) {
        super.handleFault(message);      
    }
    
    public void handleMessage(Message message) {        
        try {
            processRequest(message);
        } catch (Fault ex) {
            convertExceptionToResponseIfPossible(ex.getCause(), message);
        } catch (RuntimeException ex) {
            convertExceptionToResponseIfPossible(ex, message);
        }        
    }
    
    private void processRequest(Message message) {
		preprocess(message, new UriInfoImpl(message, null));          
    }
    
    public void preprocess(Message m, UriInfo u) {              
        MultivaluedMap<String, String> queries = u.getQueryParameters();
        handleTypeQuery(m, queries);
    }
    
    private void handleTypeQuery(Message m, MultivaluedMap<String, String> queries) {
        String type = queries.getFirst(ACCEPT_QUERY);
        if (type != null) {
            if (SHORTCUTS.containsKey(type)) {
                type = SHORTCUTS.get(type);
            }
            updateAcceptTypeHeader(m, type);
        }
    }
    
    @SuppressWarnings("unchecked")
    private void updateAcceptTypeHeader(Message m, String acceptValue) {
        m.put(Message.ACCEPT_CONTENT_TYPE, acceptValue);
        ((Map<String, List<String>>)m.get(Message.PROTOCOL_HEADERS))
        .put(HttpHeaders.ACCEPT, Collections.singletonList(acceptValue));
    }
    
    private void convertExceptionToResponseIfPossible(Throwable ex, Message message) {
        Response excResponse = JAXRSUtils.convertFaultToResponse(ex, message);
        if (excResponse == null) {
            ProviderFactory.getInstance(message).clearThreadLocalProxies();
            message.getExchange().put(Message.PROPOGATE_EXCEPTION,ExceptionUtils.propogateException(message));
            throw ex instanceof RuntimeException ? (RuntimeException)ex : new InternalServerErrorException(ex);
        }
        message.getExchange().put(Response.class, excResponse);
    }   
}
