package com.ecity.skhg.rest;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Response;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.codehaus.jettison.json.JSONObject;

import com.ecity.exception.EcityException;
import com.ecity.server.response.ResponseTool;
import com.ecity.server.response.json.ServerJSON;
import com.ecity.skhg.impl.SKHGLoaderManager;

import java.io.File;
import java.util.Iterator;
import java.util.List;

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

}