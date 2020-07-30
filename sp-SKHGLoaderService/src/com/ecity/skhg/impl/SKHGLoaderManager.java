package com.ecity.skhg.impl;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
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
import com.ecity.geometry.Point;

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
import com.ecity.util.BASE64Encoder;

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
                result.put("msg", "缺少元数据：" + proName);
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
            for (int i = 0; i < list.size(); i++) {
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

    /**
     * 执行非查询sql语句
     *
     * @param sql
     * @return
     * @throws JSONException
     */
    public JSONObject excuteSqlNoQuery(String sql) throws JSONException, EcityException {
        JSONObject result = new JSONObject();
        try {
            this.workspace.startEdit();
            String sqls[] = sql.split(";");
            for (int i = 0; i < sqls.length; i++) {
                if (!sqls[i].equals("")) {
                    this.workspace.getDb().executeSqlNoQuery(sqls[i]);
                }
            }
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
     * 旅检移泊申请同意
     *
     * @param mmsi
     * @return
     * @throws JSONException
     * @throws EcityException
     */
    public JSONObject ljybTy(String mmsi) throws JSONException, EcityException {
        JSONObject result = new JSONObject();
        try {
            this.workspace.startEdit();
            String sql1 = "UPDATE SK_LJYBSQB SET VALID='N' WHERE MMSI='%S' AND VALID='Y' AND PERMIT='Y'";
            String sql2 = "UPDATE SK_LJYBSQB SET PERMIT='Y' WHERE MMSI='%S' AND VALID='Y' AND PERMIT IS NULL";
            this.workspace.getDb().executeSqlNoQuery(String.format(sql1, mmsi));
            this.workspace.getDb().executeSqlNoQuery(String.format(sql2, mmsi));
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
     * 旅检移泊申请取消
     *
     * @param mmsi
     * @return
     * @throws JSONException
     * @throws EcityException
     */
    public JSONObject ljybQx(String mmsi) throws JSONException, EcityException {
        JSONObject result = new JSONObject();
        try {
            this.workspace.startEdit();
            String sql = "UPDATE SK_LJYBSQB SET VALID='Y', PERMIT='N' WHERE MMSI='%S' AND VALID='Y' AND PERMIT IS NULL";
            this.workspace.getDb().executeSqlNoQuery(String.format(sql, mmsi));
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
     * 旅检到泊申请同意
     *
     * @param mmsi
     * @return
     * @throws JSONException
     * @throws EcityException
     */
    public JSONObject ljdbTy(String mmsi) throws JSONException, EcityException {
        JSONObject result = new JSONObject();
        try {
            this.workspace.startEdit();
            String sql1 = "UPDATE SK_LJDBSQB SET VALID='N' WHERE MMSI='%S' AND VALID='Y' AND PERMIT='Y'";
            String sql2 = "UPDATE SK_LJDBSQB SET PERMIT='Y' WHERE MMSI='%S' AND VALID='Y' AND PERMIT IS NULL";
            this.workspace.getDb().executeSqlNoQuery(String.format(sql1, mmsi));
            this.workspace.getDb().executeSqlNoQuery(String.format(sql2, mmsi));
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
     * 旅检到泊申请取消
     *
     * @param mmsi
     * @return
     * @throws JSONException
     * @throws EcityException
     */
    public JSONObject ljdbQx(String mmsi) throws JSONException, EcityException {
        JSONObject result = new JSONObject();
        try {
            this.workspace.startEdit();
            String sql = "UPDATE SK_LJDBSQB SET VALID='Y', PERMIT='N' WHERE MMSI='%S' AND VALID='Y' AND PERMIT IS NULL";
            this.workspace.getDb().executeSqlNoQuery(String.format(sql, mmsi));
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
    
	public JSONObject AddOrUpdataVirusData(String country, String country_code, String port,String port_code,
			String virusType, String isUsed, String startTime, String endTime,
			String gid) throws JSONException, EcityException {

		JSONObject result = new JSONObject();

		

		try {
			String sql = "";
			String[] portstr = port.split(",");
			String[] port_codestr = port_code.split(",");
			if ("".equals(gid)) {
				
				for(int i=0;i<portstr.length;i++){
					sql = "INSERT INTO VIRUS_MONITORING(COUNTRY,PORT,VIRUSTYPE,ISUSED,STARTTIME,ENDTIME,GID,COUNTRY_CODE,PORT_CODE)"
							+ " VALUES ('"+ country
							+ "','"+ portstr[i]+ "','"+ virusType+ "','"+ isUsed+ "',to_date('"+ startTime+ "','yyyy-mm-dd hh24:mi:ss'),"
							+ "to_date('"+ endTime+ "','yyyy-mm-dd hh24:mi:ss')"
							+ ",SEQ_VIRUS_MONITORING_GID.NEXTVAL"
							+ ",'"+ country_code +"','"+ port_codestr[i] +"'"
							+ ")";
					this.workspace.getDb().executeSqlNoQuery(sql);
				}
		
			} else {
				sql = "UPDATE  VIRUS_MONITORING SET COUNTRY='" + country
						+ "', PORT='" + portstr[0] + "'," + "VIRUSTYPE='" + virusType
						+ "', COUNTRY_CODE='" + country_code + "'," + "PORT_CODE='" + port_codestr[0]
						+ "'," + "STARTTIME=to_date('" + startTime
						+ "','yyyy-mm-dd hh24:mi:ss')," + "ENDTIME=to_date('"
						+ endTime + "','yyyy-mm-dd hh24:mi:ss'),ISUSED='" + isUsed
						+ "' WHERE GID=" + gid;
				this.workspace.getDb().executeSqlNoQuery(sql);
			}
			

			result.put("status", "0");
			result.put("success", true);
		} catch (Exception e) {
			result.put("success", false);
			result.put("status", "1");
			result.put("msg", e.getMessage());
		}
		return result;

	}
	
	public JSONObject GetNextData()throws JSONException, EcityException{
		JSONObject result = new JSONObject();
		String sql =" SELECT DISTINCT WHARF_ID,WHART_NAME FROM  CAMERA_LOCATION";
		try {
		DataTable tb = this.workspace.getDb().executeSql(sql);
        int count = tb.getRows().size();
        JSONArray json1 = new JSONArray();
        for (int i = 0; i < count; i++) {
            DataRow row = tb.getRows().get(i);
            JSONObject jo = new JSONObject();
            jo.put("WHARF_ID", row.getString("WHARF_ID"));
            jo.put("WHART_NAME", row.getString("WHART_NAME"));
            json1.put(jo);
        }
        result.put("WHARF_DATA", json1); // 保存码头的类型 
        
        sql = " SELECT DISTINCT BERTH_ID,BERTH_PID,BERTH_NAME FROM  CAMERA_LOCATION";
        DataTable tb2 = this.workspace.getDb().executeSql(sql);
        count = tb2.getRows().size();
        JSONArray json2 = new JSONArray();
        for (int i = 0; i < count; i++) {
            DataRow row = tb2.getRows().get(i);
            JSONObject jo = new JSONObject();
            jo.put("BERTH_ID", row.getString("BERTH_ID"));
            jo.put("BERTH_PID", row.getString("BERTH_PID"));
            jo.put("BERTH_NAME", row.getString("BERTH_NAME"));
            json2.put(jo);
        }
        
        result.put("BERTH_DATA", json2); // 保存泊位的类型 
        
        sql = " SELECT DISTINCT REGION_ID,REGION_PID,REGION_NAME FROM  CAMERA_LOCATION";
        DataTable tb3 = this.workspace.getDb().executeSql(sql);
        count = tb3.getRows().size();
        JSONArray json3 = new JSONArray();
        for (int i = 0; i < count; i++) {
            DataRow row = tb3.getRows().get(i);
            JSONObject jo = new JSONObject();
            jo.put("REGION_ID", row.getString("REGION_ID"));
            jo.put("REGION_PID", row.getString("REGION_PID"));
            jo.put("REGION_NAME", row.getString("REGION_NAME"));
            json3.put(jo);
        }
        
        result.put("REGION_DATA", json3); // 保存区域类型
        
        sql = " SELECT DISTINCT CAMERA_ID,CEMERA_PID,CEMERA_NAME FROM  CAMERA_LOCATION";
        DataTable tb4 = this.workspace.getDb().executeSql(sql);
        count = tb4.getRows().size();
        JSONArray json4 = new JSONArray();
        for (int i = 0; i < count; i++) {
            DataRow row = tb4.getRows().get(i);
            JSONObject jo = new JSONObject();
            jo.put("CAMERA_ID", row.getString("CAMERA_ID"));
            jo.put("CEMERA_PID", row.getString("CEMERA_PID"));
            jo.put("CEMERA_NAME", row.getString("CEMERA_NAME"));
            json4.put(jo);
        }
        result.put("CEMERA_DATA", json4); // 保存视频类型
        result.put("status", "0"); 
        
		} catch (Exception e) {
			result.put("status", "1");
			result.put("msg", e.getMessage());
		}
		
		return result;
	}
	
	public JSONObject AddOrUpdataCamera(JSONObject data) throws JSONException, EcityException {

		JSONObject result = new JSONObject();
		
		String GIDsql ="select  seq_HIKVISION_MONITOR_gid.nextval GID from dual ";
		DataTable dt = this.workspace.getDb().executeSql(GIDsql);
		DataRow r = dt.getRows().get(0);
		int GID = r.getInt("GID");
		
		String[] point = data.getString("LOCATION").split(",");
		String sqlGeomary ="";
		if("".equals(data.getString("GEOGID"))){
			sqlGeomary ="INSERT INTO HIKVISION_MONITOR(objectid,CAMERA_ID,MATOU,SHAPE) "+
			    "VALUES(" +GID +"," +data.getInt("CAMERA_ID") + ",'"+ data.getString("BELONG_PORT") +"'"
			    + ",MDSYS.SDO_GEOMETRY(2001,4326,MDSYS.Sdo_Point_Type("
			    +Double.parseDouble(point[0].trim()) + "," +Double.parseDouble(point[1].trim())
			    + ",0),NULL,NULL)) "; 
		}else{
			
			sqlGeomary ="update  HIKVISION_MONITOR set shape="+
				      "MDSYS.SDO_GEOMETRY(2001,4326,MDSYS.Sdo_Point_Type("
				    +Double.parseDouble(point[0].trim()) + "," +Double.parseDouble(point[1].trim())
				    + ",0),NULL,NULL) ,"
				    +" MATOU='"+ data.getString("BELONG_PORT") +"'"
				    + " where objectid="+data.getInt("GEOGID"); 
			
		}
		
		
		String sql ="";
		if ("".equals(data.getString("GID"))) {
			
			sql = "INSERT INTO CAMERA_LOCATION(gid,wharf_id,whart_name,"
					+ "berth_id,berth_pid,berth_name,"
					+ "region_id,region_pid,region_name,"
					+ "camera_id,cemera_pid,cemera_name,"
					+ "BELONG_PORT,"
					+ "BELONG_BERTH,GEOGID,"
					+ "BELONG_VALUE,LOCATION)"
					+ " VALUES (CAMERA_LOCATION_SEQ.nextval,"+ data.getInt("WHARF_ID")+",'"+data.getString("WHART_NAME")+"'"
					+ ","+  data.getInt("BERTH_ID")+ ","+ data.getInt("WHARF_ID")+ ",'"+ data.getString("BERTH_NAME")+ "'"
					+ ","+  data.getInt("REGION_ID")+ ","+ data.getInt("BERTH_ID")+ ",'"+ data.getString("REGION_NAME")+ "'"
					+ ","+  data.getInt("CAMERA_ID")+ ","+ data.getInt("REGION_ID")+ ",'"+ data.getString("CEMERA_NAME")+ "'"
					+ ",'"+ data.getString("BELONG_PORT") +"',"
					+ ",'"+ data.getString("BELONG_BERTH") +"','"+ GID +"'"
					+ ",'"+ data.getString("BELONG_VALUE") +"','"+ data.getString("LOCATION") +"'"
					+ ")";
			
		} else {
			sql = "UPDATE  CAMERA_LOCATION SET BELONG_PORT='" + data.getString("BELONG_PORT")+ "', "
					+" BELONG_BERTH='" +  data.getString("BELONG_BERTH")+ "',"
					+" GEOGID='" + GID+"',"
					+" BELONG_VALUE='" + data.getString("BELONG_VALUE")+"',"
					+" LOCATION='" + data.getString("LOCATION")+"'"
					+" WHERE GID=" + data.getInt("GID");
		}

		try {
			this.workspace.getDb().executeSqlNoQuery(sql);
			this.workspace.getDb().executeSqlNoQuery(sqlGeomary); //跟新空间信息数据；
			result.put("status", "0");
			result.put("success", true);
		} catch (Exception e) {
			result.put("success", false);
			result.put("status", "1");
			result.put("msg", e.getMessage());
		}
		return result;

	}


	
	
    /**
     * 根据地图空间表 id  获取配置的摄像头信息 
     * String GEOGID,String BELONG_VALUE
     * @return
     * @throws JSONException
     * @throws EcityException
     */
	 
	public JSONObject GetCameraInfo(String GEOGID,String BELONG_VALUE) throws JSONException, EcityException {
		JSONObject result = new JSONObject();
		String wheresql ="";
		if(!"".equals(GEOGID) && "".equals(BELONG_VALUE)){
			wheresql ="WHERE GEOGID=" +GEOGID;
		}else if("".equals(GEOGID) && !"".equals(BELONG_VALUE)){
			wheresql ="WHERE BELONG_VALUE='" +BELONG_VALUE+"'";
		}else{
			result.put("status", "1");
			result.put("msg", "请求入参不合法!");
			return result;
		}
		String sql = " SELECT  WHART_NAME,BERTH_NAME,REGION_NAME,CEMERA_NAME,CAMERA_ID, "
				+ " BELONG_PORT,BELONG_BERTH,BELONG_VALUE,LOCATION FROM CAMERA_LOCATION " + wheresql;
		try {
			DataTable tb = this.workspace.getDb().executeSql(sql);
			int count = tb.getRows().size();
			JSONObject jo = new JSONObject();
			if(count>0){
				DataRow row = tb.getRows().get(0);
				jo.put("WHART_NAME", row.getString("WHART_NAME"));
				jo.put("BERTH_NAME", row.getString("BERTH_NAME"));
				jo.put("REGION_NAME", row.getString("REGION_NAME"));
				jo.put("CEMERA_NAME", row.getString("CEMERA_NAME"));
				jo.put("CAMERA_ID", row.getString("CAMERA_ID"));
				jo.put("BELONG_PORT", row.getString("BELONG_PORT"));
				jo.put("BELONG_BERTH", row.getString("BELONG_BERTH"));
				jo.put("BELONG_VALUE", row.getString("BELONG_VALUE"));
				jo.put("LOCATION", row.getString("LOCATION"));
				result.put("msg", "获取摄像头信息成功!");
				result.put("status", "0");
			}else{
				result.put("msg", "摄像头暂未配置位置！");
				result.put("status", "2");
			}
			result.put("DATA", jo); 
		} catch (Exception e) {
			result.put("status", "1");
			result.put("msg", e.getMessage());
		}

		return result;
	}
	
	
	 /**
     * 查询摄像头空间表 
     * @param tableName
     * @param where
     * @return
     * @throws JSONException
     */
    public JSONObject queryGeomCamera(String tableName, String where) throws JSONException {
        JSONObject result = new JSONObject();
        try {
            ITableClass itc = this.workspace.getTableClass(tableName);
            IFeatureClass ifc = this.workspace.getFeatureClass(tableName);
            QueryFilter qf = new QueryFilter();
            where ="MATOU='"+where+"'";
            qf.setWhere(where);
            List<Record> list = itc.search(qf);

            
            
            String sql ="select  C.CEMERA_NAME,C.CAMERA_ID,C.GEOGID from  CAMERA_LOCATION C , HIKVISION_MONITOR H where c.geogid = h.objectid and h."+where;
    		DataTable tb = this.workspace.getDb().executeSql(sql);

            JSONArray ja = new JSONArray();
            for (int i = 0; i < list.size(); i++) {
                Record r = list.get(i);
                JSONObject jo = new JSONObject();

                for (int j= 0; j <  tb.getRows().size(); j++) {
                    DataRow row = tb.getRows().get(j);
                    if(row.getInt("GEOGID")==r.getIntValue("OBJECTID")){
                    	jo.put("name", row.getString("CEMERA_NAME")); //摄像头名称
                    	jo.put("id", row.getString("CAMERA_ID")); //摄像头ID
                    	break;
                    }
                
                }
                Geometry g = ifc.getGeometryBySqlGeometry(r.getObjectValue("SHAPE"));
                jo.put("geom", g.toJSON());
                ja.put(jo);
            }

            JSONObject jo = new JSONObject();

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
