package com.ecity.skhg.rest;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Response;

import org.codehaus.jettison.json.JSONObject;

import com.ecity.exception.EcityException;
import com.ecity.server.response.ResponseTool;
import com.ecity.server.response.json.ServerJSON;
import com.ecity.skhg.impl.SKHGStageManager;

public class SKHGStageServerRestImpl implements SKHGStageServerRest {
    private ServiceCore CORE;

    public ServiceCore getCORE() {
        return this.CORE;
    }

    public void setCORE(ServiceCore CORE) {
        this.CORE = CORE;
    }

    @Override
    public Object upImg(HttpServletRequest req, String f, long gid) throws Exception {
        Response response = null;
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject = new SKHGStageManager(CORE).upImg(req, gid);
        } catch (Exception ex) {
            ex.printStackTrace();
            jsonObject = ServerJSON.ecityException(new EcityException(ex.getMessage()));
        }
        response = ResponseTool.jsonObjectResponse(jsonObject, req);
        return response;
    }

    @Override
    public Object insertArea(HttpServletRequest req, String f, String datas) throws Exception {
        Response response = null;
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject = new SKHGStageManager(CORE).insertArea(datas);
        } catch (Exception ex) {
            ex.printStackTrace();
            jsonObject = ServerJSON.ecityException(new EcityException(ex.getMessage()));
        }
        response = ResponseTool.jsonObjectResponse(jsonObject, req);
        return response;
    }

    @Override
    public Object getAreaByWhere(HttpServletRequest req, String f, String where) throws Exception {
        Response response = null;
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject = new SKHGStageManager(CORE).getAreaByWhere(where);
        } catch (Exception ex) {
            ex.printStackTrace();
            jsonObject = ServerJSON.ecityException(new EcityException(ex.getMessage()));
        }
        response = ResponseTool.jsonObjectResponse(jsonObject, req);
        return response;
    }

    @Override
    public Object updateGeomByGid(HttpServletRequest req, String f, int gid, String rings) throws Exception {
        Response response = null;
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject = new SKHGStageManager(CORE).updateGeomByGid(gid, rings);
        } catch (Exception ex) {
            ex.printStackTrace();
            jsonObject = ServerJSON.ecityException(new EcityException(ex.getMessage()));
        }
        response = ResponseTool.jsonObjectResponse(jsonObject, req);
        return response;
    }

    @Override
    public Object queryTableByWhere(HttpServletRequest req, String f, String tableName, String where) throws Exception {
        Response response = null;
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject = new SKHGStageManager(CORE).queryTableByWhere(tableName, where);
        } catch (Exception ex) {
            ex.printStackTrace();
            jsonObject = ServerJSON.ecityException(new EcityException(ex.getMessage()));
        }
        response = ResponseTool.jsonObjectResponse(jsonObject, req);
        return response;
    }

    @Override
    public Object queryGeomTable(HttpServletRequest req, String f, String tableName, String where) throws Exception {
        Response response = null;
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject = new SKHGStageManager(CORE).queryGeomTable(tableName, where);
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
            jsonObject = new SKHGStageManager(CORE).excuteSqlNoQuery(sql);
        } catch (Exception ex) {
            ex.printStackTrace();
            jsonObject = ServerJSON.ecityException(new EcityException(ex.getMessage()));
        }
        response = ResponseTool.jsonObjectResponse(jsonObject, req);
        return response;
    }

}