package com.ecity.skhg.impl;

import java.io.File;
import java.util.*;

import com.ecity.datatable.DataRow;
import com.ecity.datatable.DataRowCollection;
import com.ecity.datatable.DataTable;
import com.ecity.define.common.PRODUCE_PARAM;
import com.ecity.define.common.PRODUCE_RESULT;
import com.ecity.define.enums.EnumDataType;
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
            qf.setWhere("TABLENAME='" + tableName + "' AND VISIBLE=1");
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

    public JSONObject querProGL(String proName, String parms) throws JSONException {
        JSONObject result = new JSONObject();
        try {
            List<PRODUCE_PARAM> paramList = new ArrayList();
            ITableClass itc = this.workspace.getTableClass("MT_ATT_FIELD");
            QueryFilter qf = new QueryFilter();
            qf.setWhere("TABLENAME='" + proName + "' AND VISIBLE=1");
            List<Record> list = itc.search(qf);
            if (list.size() <= 0) {
                result.put("success", false);
                result.put("msg",  "缺少元数据：" + proName);
                return result;
            }
            JSONArray jsn = new JSONArray(parms);
            for (int o = 0; o < jsn.length(); o++) {
                PRODUCE_PARAM p1 = new PRODUCE_PARAM();
                JSONObject obj = jsn.getJSONObject(o);
                String paramName = obj.get("paramName").toString();
                p1.setParamInOrOut(PRODUCE_PARAM.EnumParamInOrOut.enumParam_IN);
                p1.setParamName(paramName);
                p1.setParamType(EnumDataType.enumDATA_CHAR);
                p1.setValue(obj.get("value"));
                paramList.add(p1);
            }
            PRODUCE_PARAM p2 = new PRODUCE_PARAM();
            p2.setParamInOrOut(PRODUCE_PARAM.EnumParamInOrOut.enumParam_OUT);
            p2.setParamName("CUR_A");
            p2.setParamType(EnumDataType.enumDATA_CURSOR);
            paramList.add(p2);
            List<PRODUCE_RESULT> listp = this.workspace.getDb().executeCall(proName, paramList);
            JSONArray ja = new JSONArray();
            if (listp.size() > 0) {
                DataTable dt = (DataTable) listp.get(0).getResult();
                DataRowCollection drc = dt.getRows();
                for (int j = 0; j < drc.size(); j++) {
                    DataRow dr = drc.get(j);
                    JSONObject a = new JSONObject();
                    for (int k = 0; k < list.size(); k++) {
                        String name = list.get(k).getStringValue("NAME");
                        a.put(name, dr.getString(name));
                    }
                    ja.put(a);
                }
            }
            JSONObject attr = new JSONObject();
            for (int i=0; i<list.size();i++) {
                String name = list.get(i).getStringValue("NAME");
                String alias = list.get(i).getStringValue("ALIAS");
                attr.put(name, alias);
            }
            result.put("data", ja);
            result.put("attr", attr);
            result.put("success", true);
        } catch (Exception e) {
            result.put("success", false);
            result.put("msg", e.getMessage());
        }
        return result;
    }

}
