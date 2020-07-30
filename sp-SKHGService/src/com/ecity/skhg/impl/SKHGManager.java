package com.ecity.skhg.impl;

import java.io.File;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import com.ecity.datatable.DataRow;
import com.ecity.datatable.DataRowCollection;
import com.ecity.datatable.DataTable;
import com.ecity.feature.Feature;
import com.ecity.feature.IFeatureClass;
import com.ecity.feature.QueryFilter;
import com.ecity.geometry.Geometry;
import org.apache.commons.fileupload.FileItem;
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

public class SKHGManager {

    /**
     * 服务入口
     */
    ServiceCore serviceCore;

    /**
     * 工作空间
     */
    IWorkspace workspace;

    private ServiceCore CORE;

    public SKHGManager() {

    }

    public SKHGManager(ServiceCore serviceCore) throws EcityException {
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

    /**
     * 上传文件
     *
     * @param req
     * @return
     * @throws JSONException
     */

    public JSONObject upFile(HttpServletRequest req) throws JSONException {
        JSONObject result = new JSONObject();
        String home = serviceCore.getServer().getHome();
        String uploadPath = home.replace("ServiceEngine", "upload"); // 上传文件的目录
        try {
            // Create a factory for disk-based file items
            DiskFileItemFactory factory = new DiskFileItemFactory();
            // Set factory constraints
            factory.setSizeThreshold(4096); // 设置缓冲区大小，这里是4kb
            // Create a new file upload handler
            ServletFileUpload upload = new ServletFileUpload(factory);
            // Set overall request size constraint
            upload.setSizeMax(4194304); // 设置最大文件尺寸，这里是4MB
            List<FileItem> items = upload.parseRequest(req);// 得到所有的文件
            Iterator<FileItem> i = items.iterator();
            JSONArray ja = new JSONArray();
            while (i.hasNext()) {
                FileItem fi = (FileItem) i.next();
                String fileName = fi.getName();
                if (fileName != null) {
                    String date = DateUtils.date2Str(new Date(), DateUtils.yyyymmddhhmmss);
                    File fullFile = new File(new String(fi.getName().getBytes(), "utf-8")); // 解决文件名乱码问题
                    String saveName = date + fullFile.getName().substring(fullFile.getName().indexOf("."));
                    File savedFile = new File(uploadPath, saveName);
                    fi.write(savedFile);
                    ja.put(saveName);
                }
            }
            result.put("paths", ja);
            result.put("success", true);
        } catch (Exception e) {
            result.put("success", false);
            result.put("msg", e.getMessage());
        }
        return result;
    }

    /**
     * 上传图片
     *
     * @param req
     * @return
     * @throws JSONException
     */
    public JSONObject upImg(HttpServletRequest req, long gid) throws JSONException {
        JSONObject result = new JSONObject();
        try {
            JSONObject jo = upFile(req);
            if (jo.getBoolean("success")) {
                JSONArray ja = (JSONArray) jo.get("paths");
                ITableClass itc = workspace.getTableClass("SK_WSRYS");
                String path = (String) ja.get(0);
                Record r = itc.createRecord();
                r.setValue("TP", path);
                itc.update(gid, r);
                result.put("path", path);
                result.put("success", true);
            } else {
                result.put("success", false);
                result.put("msg", jo.get("msg"));
            }
        } catch (Exception e) {
            result.put("success", false);
            result.put("msg", e.getMessage());
        }
        return result;
    }

    /**
     * 插入区域信息
     *
     * @param datas
     * @return
     * @throws JSONException
     */
    public JSONObject insertArea(String datas) throws JSONException {
        JSONObject result = new JSONObject();
        JSONArray ja = new JSONArray(datas);
        try {
            this.workspace.startEdit();
            IFeatureClass ifc = this.workspace.getFeatureClass("SK_AREA");
            for (int i = 0; i < ja.length(); i++) {
                JSONObject jo = ja.getJSONObject(i);
                JSONObject attr = jo.getJSONObject("attr");
                String geom = jo.getJSONObject("geom").toString();
                Feature f = ifc.createFeatureByJSON(attr, geom);
                ifc.append(f);
            }
            this.workspace.endEdit();
            result.put("success", true);
        } catch (Exception e) {
            result.put("success", false);
            result.put("msg", e.getMessage());
        }
        return result;
    }

    /**
     * 通过条件获取区域信息
     *
     * @param where
     * @return
     * @throws JSONException
     */
    public JSONObject getAreaByWhere(String where) throws JSONException {
        JSONObject result = new JSONObject();
        try {
            ITableClass itc = this.workspace.getTableClass("SK_AREA");
            IFeatureClass ifc = this.workspace.getFeatureClass("SK_AREA");
            QueryFilter qf = new QueryFilter();
            qf.setWhere(where);
            List<Record> list = itc.search(qf);
            JSONArray ja = new JSONArray();
            for (int i = 0; i < list.size(); i++) {
                Record r = list.get(i);
                JSONObject jo = new JSONObject();
                jo.put("gid", r.getLongValue("GID"));
                jo.put("name", r.getStringValue("NAME"));
                jo.put("code", r.getStringValue("CODE"));
                jo.put("type", r.getStringValue("TYPE"));
                jo.put("ssdw", r.getStringValue("SSDW"));
                JSONObject j = new JSONObject();
                j.put("xmin", r.getStringValue("XMIN"));
                j.put("xmax", r.getStringValue("XMAX"));
                j.put("ymin", r.getStringValue("YMIN"));
                j.put("ymax", r.getStringValue("YMAX"));
                jo.put("mapExtent", j);
                jo.put("layer", r.getStringValue("LAYER"));
                Geometry g = ifc.getGeometryBySqlGeometry(r.getObjectValue("GEOM"));
                jo.put("geom", g.toJSON());
                ja.put(jo);
            }
            result.put("data", ja);
            result.put("success", true);
        } catch (Exception e) {
            result.put("success", false);
            result.put("msg", e.getMessage());
        }
        return result;
    }

    /**
     * 通过gid更新区域地理信息
     *
     * @param gid
     * @param rings
     * @return
     * @throws JSONException
     */
    public JSONObject updateGeomByGid(String tableName, int gid, String rings) throws JSONException {
        JSONObject result = new JSONObject();
        JSONArray ja = new JSONArray("[" + rings + "]");
        try {
            this.workspace.startEdit();
            IFeatureClass ifc = this.workspace.getFeatureClass(tableName);
            JSONObject attr = new JSONObject();
            JSONObject geom = new JSONObject();
            geom.put("rings", ja);
            Feature f = ifc.createFeatureByJSON(attr, geom.toString());
            ifc.updateGeometry(gid, f);
            this.workspace.endEdit();
            result.put("success", true);
        } catch (Exception e) {
            result.put("success", false);
            result.put("msg", e.getMessage());
        }
        return result;
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
            for (int i = 0; i < list1.size(); i++) {
                Record r = list1.get(i);
                JSONObject jo = new JSONObject();
                for (int j = 0; j < list.size(); j++) {
                    String name = list.get(j).getStringValue("NAME");
                    jo.put(name, r.getStringValue(name));
                }
                ja.put(jo);
            }

            JSONObject jo = new JSONObject();
            for (int i = 0; i < list.size(); i++) {
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

    /**
     * 查询空间表
     *
     * @param tableName
     * @param where
     * @return
     * @throws JSONException
     */
    public JSONObject queryGeomTable(String tableName, String where) throws JSONException {
        JSONObject result = new JSONObject();
        try {
            ITableClass itc = this.workspace.getTableClass(tableName);
            IFeatureClass ifc = this.workspace.getFeatureClass(tableName);
            QueryFilter qf = new QueryFilter();
            qf.setWhere(where);
            List<Record> list = itc.search(qf);

            ITableClass itc1 = this.workspace.getTableClass("MT_ATT_FIELD");
            QueryFilter qf1 = new QueryFilter();
            qf1.setWhere("TABLENAME='" + tableName + "' AND VISIBLE=1");
            List<Record> field = itc1.search(qf1);

            JSONArray ja = new JSONArray();
            for (int i = 0; i < list.size(); i++) {
                Record r = list.get(i);
                JSONObject jo = new JSONObject();
                for (int j = 0; j < field.size(); j++) {
                    String name = field.get(j).getStringValue("NAME");
                    if (!name.equals("GEOM")) {
                        jo.put(name.toLowerCase(), r.getStringValue(name));
                    }
                }
                Geometry g = ifc.getGeometryBySqlGeometry(r.getObjectValue("GEOM"));
                jo.put("geom", g.toJSON());
                ja.put(jo);
            }

            JSONObject jo = new JSONObject();
            for (int i = 0; i < field.size(); i++) {
                Record r = field.get(i);
                jo.put(r.getStringValue("NAME").toLowerCase(), r.getStringValue("ALIAS"));
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

    /**
     * 智能指挥发送消息
     *
     * @param sender
     * @param senderId
     * @param msg
     * @return
     * @throws JSONException
     * @throws EcityException
     */
    public JSONObject sendICMsg(String sender, String senderId, String msg) throws JSONException, EcityException {
        JSONObject result = new JSONObject();
        try {
            this.workspace.startEdit();
            ITableClass itc = this.workspace.getTableClass("SK_ICOMMAND");
            Record r = itc.createRecord();
            r.setValue("SENDER", sender);
            r.setValue("SENDERID", senderId);
            r.setValue("MSG", msg);
            itc.append(r);
            this.workspace.endEdit();
            result.put("success", true);
        } catch (Exception e) {
            this.workspace.rollbackEdit();
            result.put("success", false);
            result.put("msg", e.getMessage());
        }
        return result;
    }

    /**
     * 获取最新的智能指挥聊天记录
     *
     * @param number
     * @return
     * @throws JSONException
     * @throws EcityException
     */
    public JSONObject getNewestICMsgs(int number) throws JSONException, EcityException {
        JSONObject result = new JSONObject();
        try {
            DataTable dt = this.workspace.getDb().executeSql(String.format("SELECT * FROM (SELECT * FROM SK_ICOMMAND ORDER BY SENDTIME DESC) WHERE ROWNUM <= %S ORDER BY SENDTIME ASC", number));
            DataRowCollection drc = dt.getRows();
            JSONArray ja = new JSONArray();
            for (int i = 0; i < drc.size(); i++) {
                DataRow dr = drc.get(i);
                JSONObject jo = new JSONObject();
                jo.put("gid", dr.getLong("GID"));
                jo.put("sender", dr.getString("SENDER"));
                jo.put("senderId", dr.getString("SENDERID"));
                jo.put("sendTime", dr.getString("SENDTIME"));
                jo.put("msg", dr.getString("MSG"));
                jo.put("img", dr.getString("IMG"));
                ja.put(jo);
            }
            result.put("data", ja);
            result.put("success", true);
        } catch (Exception e) {
            result.put("success", false);
            result.put("msg", e.getMessage());
        }
        return result;
    }
    
   

}
