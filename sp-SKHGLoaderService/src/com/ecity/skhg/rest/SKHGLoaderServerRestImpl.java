package com.ecity.skhg.rest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

import com.ecity.skhg.impl.IcommandManager;









import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.codehaus.jettison.json.JSONObject;

import com.ecity.exception.EcityException;
import com.ecity.server.response.ResponseTool;
import com.ecity.server.response.json.ServerJSON;
import com.ecity.skhg.impl.SKHGLoaderManager;
import com.ecity.skhg.utils.PdfUtil;

import java.io.File;
import java.io.OutputStream;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class SKHGLoaderServerRestImpl implements SKHGLoaderServerRest {
    private ServiceCore CORE;

    public ServiceCore getCORE() {
        return this.CORE;
    }

    public void setCORE(ServiceCore CORE) {
        this.CORE = CORE;
    }

    @Override
    public Object queryTableByWhere(HttpServletRequest req, String f, String tableName, String where) throws Exception {
        Response response = null;
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject = new SKHGLoaderManager(CORE).queryTableByWhere(tableName, where);
        } catch (Exception ex) {
            ex.printStackTrace();
            jsonObject = ServerJSON.ecityException(new EcityException(ex.getMessage()));
        }
        response = ResponseTool.jsonObjectResponse(jsonObject, req);
        return response;
    }

    @Override
    public Object querProGL(HttpServletRequest req, String f, String proName, String parms) throws Exception {
        Response response = null;
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject = new SKHGLoaderManager(CORE).querProGL(proName, parms);
        } catch (Exception ex) {
            ex.printStackTrace();
            jsonObject = ServerJSON.ecityException(new EcityException(ex.getMessage()));
        }
        response = ResponseTool.jsonObjectResponse(jsonObject, req);
        return response;
    }

    @Override
    public Object excuteSqlNoQuery(HttpServletRequest req, String f, String sql) throws Exception {
        Response response = null;
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject = new SKHGLoaderManager(CORE).excuteSqlNoQuery(sql);
        } catch (Exception ex) {
            ex.printStackTrace();
            jsonObject = ServerJSON.ecityException(new EcityException(ex.getMessage()));
        }
        response = ResponseTool.jsonObjectResponse(jsonObject, req);
        return response;
    }

    @Override
    public Object ljybTy(HttpServletRequest req, String f, String mmsi) throws Exception {
        Response response = null;
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject = new SKHGLoaderManager(CORE).ljybTy(mmsi);
        } catch (Exception ex) {
            ex.printStackTrace();
            jsonObject = ServerJSON.ecityException(new EcityException(ex.getMessage()));
        }
        response = ResponseTool.jsonObjectResponse(jsonObject, req);
        return response;
    }

    @Override
    public Object ljybQx(HttpServletRequest req, String f, String mmsi) throws Exception {
        Response response = null;
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject = new SKHGLoaderManager(CORE).ljybQx(mmsi);
        } catch (Exception ex) {
            ex.printStackTrace();
            jsonObject = ServerJSON.ecityException(new EcityException(ex.getMessage()));
        }
        response = ResponseTool.jsonObjectResponse(jsonObject, req);
        return response;
    }

    @Override
    public Object ljdbTy(HttpServletRequest req, String f, String mmsi) throws Exception {
        Response response = null;
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject = new SKHGLoaderManager(CORE).ljdbTy(mmsi);
        } catch (Exception ex) {
            ex.printStackTrace();
            jsonObject = ServerJSON.ecityException(new EcityException(ex.getMessage()));
        }
        response = ResponseTool.jsonObjectResponse(jsonObject, req);
        return response;
    }

    @Override
    public Object ljdbQx(HttpServletRequest req, String f, String mmsi) throws Exception {
        Response response = null;
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject = new SKHGLoaderManager(CORE).ljdbQx(mmsi);
        } catch (Exception ex) {
            ex.printStackTrace();
            jsonObject = ServerJSON.ecityException(new EcityException(ex.getMessage()));
        }
        response = ResponseTool.jsonObjectResponse(jsonObject, req);
        return response;
    }

    @Override
    public Object updateGeomByGid(HttpServletRequest req, String f,String tableName, int gid, String rings) throws Exception {
        Response response = null;
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject = new SKHGLoaderManager(CORE).updateGeomByGid(tableName, gid, rings);
        } catch (Exception ex) {
            ex.printStackTrace();
            jsonObject = ServerJSON.ecityException(new EcityException(ex.getMessage()));
        }
        response = ResponseTool.jsonObjectResponse(jsonObject, req);
        return response;
    }

    @Override
    public Object getUserSig(HttpServletRequest req, String f, String user) throws Exception {
        Response response = null;
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject = new IcommandManager(CORE).getUserSig(user);
        } catch (Exception ex) {
            ex.printStackTrace();
            jsonObject = ServerJSON.ecityException(new EcityException(ex.getMessage()));
        }
        response = ResponseTool.jsonObjectResponse(jsonObject, req);
        return response;
    }
    
    @Override
    public Object AddOrUpdataVirusData(
             HttpServletRequest req,
             String country,
             String country_code,
             String port,
             String port_code,
             String virusType,
             String isUsed,
             String startTime,
             String endTime,
             String gid) throws Exception{
    	 Response response = null;
         JSONObject jsonObject = new JSONObject();
         try {
             jsonObject = new SKHGLoaderManager(CORE).AddOrUpdataVirusData(country,country_code, port,port_code, virusType, isUsed, startTime, endTime, gid);
            		 
            		
         } catch (Exception ex) {
             ex.printStackTrace();
             jsonObject = ServerJSON.ecityException(new EcityException(ex.getMessage()));
         }
         response = ResponseTool.jsonObjectResponse(jsonObject, req);
         return response;
    	
    }
    
 

	@Override
	public Object GetNextData(HttpServletRequest req) throws Exception {
		 Response response = null;
         JSONObject jsonObject = new JSONObject();
         try {
             jsonObject = new SKHGLoaderManager(CORE).GetNextData();
            		 
            		
         } catch (Exception ex) {
             ex.printStackTrace();
             jsonObject = ServerJSON.ecityException(new EcityException(ex.getMessage()));
         }
         response = ResponseTool.jsonObjectResponse(jsonObject, req);
         return response;
	}

	@Override
	public Object AddOrUpdataCamera(HttpServletRequest req,String WHART_NAME,
                                    String WHARF_ID,String BERTH_NAME,
                                    String BERTH_ID,String REGION_NAME,
                                    String REGION_ID,String CEMERA_NAME,
                                    String CAMERA_ID,
                                    String BELONG_PORT,
                                    String BELONG_BERTH,
                                    String BELONG_VALUE,
                                    String LOCATION,
                                    String GEOGID,
                                    String GID)
			throws Exception {
		Response response = null;
        JSONObject jsonObject = new JSONObject();
        try {
        	JSONObject data = new JSONObject();
        	data.put("WHART_NAME", WHART_NAME);
        	data.put("WHARF_ID", WHARF_ID);data.put("BERTH_NAME", BERTH_NAME);
        	data.put("BERTH_ID", BERTH_ID);data.put("REGION_NAME", REGION_NAME);
        	data.put("REGION_ID", REGION_ID);data.put("CEMERA_NAME", CEMERA_NAME);
        	data.put("CAMERA_ID", CAMERA_ID);
        	data.put("BELONG_PORT", BELONG_PORT);
        	data.put("BELONG_BERTH", BELONG_BERTH);
        	data.put("BELONG_VALUE", BELONG_VALUE);
        	data.put("LOCATION", LOCATION);
        	data.put("GEOGID", GEOGID);
        	data.put("GID", GID);
            jsonObject = new SKHGLoaderManager(CORE).AddOrUpdataCamera(data);
           		 
           		
        } catch (Exception ex) {
            ex.printStackTrace();
            jsonObject = ServerJSON.ecityException(new EcityException(ex.getMessage()));
        }
        response = ResponseTool.jsonObjectResponse(jsonObject, req);
        return response;
	}
    
    
	   /**
     * 获取摄像头信息
     * @return
     * @throws Exception
     */
    @Override
    public  Object GetCameraInfo(  HttpServletRequest req,String GEOGID,String BELONG_VALUE) throws Exception{
    	 Response response = null;
         JSONObject jsonObject = new JSONObject();
         try {
             jsonObject = new SKHGLoaderManager(CORE).GetCameraInfo(GEOGID,BELONG_VALUE);
            		 
            		
         } catch (Exception ex) {
             ex.printStackTrace();
             jsonObject = ServerJSON.ecityException(new EcityException(ex.getMessage()));
         }
         response = ResponseTool.jsonObjectResponse(jsonObject, req);
         return response;
    }

	
    @Override
	public void GetPdfInfo(HttpServletRequest req, HttpServletResponse response)
			throws Exception {
		
		this.setResponseHeader(response, "test.pdf");
		OutputStream os = response.getOutputStream();
		PdfUtil pdf =new PdfUtil();
		pdf.test(os);
	}
    
    // 发送响应流方法
    public void setResponseHeader(HttpServletResponse response, String fileName) {
       
  
          try {
             fileName = new String(fileName.getBytes(), "ISO8859-1");
             response.setContentType("application/pdf;charset=utf-8");
             response.setHeader("Content-Disposition", "attachment;filename=" + fileName);
             response.addHeader("Pargam", "no-cache");
             response.addHeader("Cache-Control", "no-cache");
          } catch (Exception e) {
             // TODO Auto-generated catch block
             e.printStackTrace();
          }
          
       
    }

    
    
    
	@Override
	public Object queryGeomCamera(HttpServletRequest req,String f,String tableName,String where) throws Exception {
		 Response response = null;
         JSONObject jsonObject = new JSONObject();
         try {
             jsonObject = new SKHGLoaderManager(CORE).queryGeomCamera(tableName,where);
            		 
            		
         } catch (Exception ex) {
             ex.printStackTrace();
             jsonObject = ServerJSON.ecityException(new EcityException(ex.getMessage()));
         }
         response = ResponseTool.jsonObjectResponse(jsonObject, req);
         return response;
	}
   
}