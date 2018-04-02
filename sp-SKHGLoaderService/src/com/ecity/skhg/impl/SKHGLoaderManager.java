package com.ecity.skhg.impl;

import java.io.File;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import com.ecity.feature.Feature;
import com.ecity.feature.IFeatureClass;
import com.ecity.feature.QueryFilter;
import com.ecity.geometry.Geometry;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUpload;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import com.ecity.datasource.IWorkspace;
import com.ecity.exception.EcityException;
import com.ecity.skhg.rest.ServiceCore;
import com.ecity.skhg.utils.DateUtils;
import com.ecity.table.ITableClass;
import com.ecity.table.Record;

import javax.servlet.http.HttpServletRequest;

public class SKHGLoaderManager {

    /**
     * 服务入口
     */
    ServiceCore serviceCore;

    /**
     * 工作空间
     */
    IWorkspace workspace;

    private ServiceCore CORE;

    public SKHGLoaderManager() {

    }

    public SKHGLoaderManager(ServiceCore serviceCore) throws EcityException {
        this.serviceCore = serviceCore;
        this.workspace = serviceCore.getWorkspace();
    }

    public ServiceCore getCORE() {
        return this.CORE;
    }

    public void setCORE(ServiceCore CORE) {
        this.CORE = CORE;
    }

    public IWorkspace getWorkspace() {
        return workspace;
    }

    public void setWorkspace(IWorkspace workspace) {
        this.workspace = workspace;
    }

    public JSONObject queryTableByWhere(String tableName, String where) throws JSONException {
        JSONObject result = new JSONObject();
        try {
            ITableClass itc = this.workspace.getTableClass("MT_ATT_FIELD");
            QueryFilter qf = new QueryFilter();
            qf.setWhere("TABLENAME='" + tableName + "'");
            List<Record> list = itc.search(qf);

            ITableClass itc1 = this.workspace.getTableClass(tableName);
            QueryFilter qf1 = new QueryFilter();
            qf1.setWhere(where);
            List<Record> list1 = itc1.search(qf1);

            JSONArray ja = new JSONArray();
            for (int i = 0; i < list1.size();i++) {
                Record r = list1.get(i);
                JSONObject jo = new JSONObject();
                for (int j = 0; j < list.size();j++){
                    String name = list.get(j).getStringValue("NAME");
                    jo.put(name, r.getStringValue(name));
                }
                ja.put(jo);
            }

            JSONObject jo = new JSONObject();
            for (int i = 0; i < list.size();i++){
                Record r = list.get(i);
                jo.put(r.getStringValue("NAME"), r.getStringValue("ALIAS"));
            }
            result.put("data", ja);
            result.put("attr", jo);
            result.put("success", true);
        } catch (Exception e) {
            result.put("success", false);
            result.put("msg", e.getMessage());
        }
        return result;
    }

}
