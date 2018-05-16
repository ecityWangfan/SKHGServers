package com.ecity.skhg.impl;

import com.ecity.datasource.ConnectionProperty;
import com.ecity.datasource.IWorkspace;
import com.ecity.datasource.Workspace;
import com.ecity.datasource.WorkspaceFactory;
import com.ecity.datatable.DataRow;
import com.ecity.datatable.DataRowCollection;
import com.ecity.datatable.DataTable;
import com.ecity.db.EcityDbFactory;
import com.ecity.db.IECityDb;
import com.ecity.define.common.DB_INFO;
import com.ecity.define.enums.EnumConnectionMode;
import com.ecity.exception.EcityException;
import com.ecity.feature.IFeatureClass;
import com.ecity.feature.QueryFilter;
import com.ecity.geometry.Geometry;
import com.ecity.se.core.Server;
import com.ecity.skhg.rest.SKHGLoaderServerRestImpl;
import com.ecity.skhg.rest.ServiceCore;
import com.ecity.table.ITableClass;
import com.ecity.table.Record;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@Component("taskJob")
public class Job {

    IWorkspace workspace = null;
    private JSONArray berth = new JSONArray();

    public Job() {
        try {
            this.workspace = getWorkspace("loader");
            IFeatureClass ifc = this.workspace.getFeatureClass("YLMG_BERTH");
            ITableClass itc = this.workspace.getTableClass("YLMG_BERTH");
            QueryFilter qf = new QueryFilter();
            qf.setWhere("1=1");
            List<Record> list = itc.search(qf);
            for (int i = 0; i < list.size(); i++) {
                Record r = list.get(i);
                JSONObject jo = new JSONObject();
                jo.put("CODE", r.getStringValue("CODE"));
                jo.put("NAME", r.getStringValue("NAME"));
                Geometry g = ifc.getGeometryBySqlGeometry(r.getObjectValue("GEOM"));
                jo.put("GEOM", g.toJSON());
                this.berth.put(jo);
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 监控异常：旅检船舶未申报即移泊
     */
    @Scheduled(cron = "*/10 * * * * ?")
    public void job1() {
        try {
            //查询当前生效的移泊申请
            String sql = "SELECT * FROM SK_LJYBSQB WHERE VALID='Y' AND PERMIT='Y'";
            DataTable dt = this.workspace.getDb().executeSql(sql);
            DataRowCollection drc = dt.getRows();
            JSONObject apply = new JSONObject();
            for (int i = 0; i < drc.size(); i++) {
                DataRow dr = drc.get(i);
                JSONObject jo = new JSONObject();
                jo.put("MMSI", dr.getString("MMSI"));
                jo.put("CURBERTH", dr.getString("CURBERTH"));
                jo.put("TARBERTH", dr.getString("TARBERTH"));
                jo.put("MOVETIME", dr.getDate("MOVETIME"));
                apply.put(dr.getString("MMSI"), jo);
            }
            //查询船舶当前位置信息
            ITableClass itc = this.workspace.getTableClass("YLMG_SHIP");
            QueryFilter qf = new QueryFilter();
            qf.setWhere("1=1");
            List<Record> list = itc.search(qf);
            JSONArray ships = new JSONArray();
            for (int i = 0; i < list.size(); i++) {
                Record r = list.get(i);
                JSONObject jo = new JSONObject();
                jo.put("MMSI", r.getStringValue("MMSI"));
                jo.put("LONGITUDE", r.getStringValue("LONGITUDE"));
                jo.put("LATITUDE", r.getStringValue("LATITUDE"));
                jo.put("SHIPOWNER", r.getStringValue("SHIPOWNER"));
                jo.put("SHIPNAME_CN", r.getStringValue("SHIPNAME_CN"));
                jo.put("SHIPNAME_EN", r.getStringValue("SHIPNAME_EN"));
                jo.put("IMO", r.getStringValue("IMO"));
                jo.put("CALLS", r.getStringValue("CALLS"));
                jo.put("WEIGHT", r.getStringValue("WEIGHT"));
                jo.put("WEIGHT_NET", r.getStringValue("WEIGHT_NET"));
                jo.put("SHIPLONG", r.getStringValue("SHIPLONG"));
                jo.put("SHIPWIDTH", r.getStringValue("SHIPWIDTH"));
                jo.put("HEADING", r.getStringValue("HEADING"));
                jo.put("SPEED", r.getStringValue("SPEED"));
                jo.put("DTM", r.getStringValue("DTM"));
                ships.put(jo);
            }
            //确认船舶位置是否合法
            for (int i = 0; i < ships.length(); i++) {
                JSONObject ship = ships.getJSONObject(i);
                String mmsi = ship.getString("MMSI");
                if (apply.has(mmsi)) {
                    JSONObject sp = apply.getJSONObject(mmsi);
                    String cb = sp.getString("CURBERTH");
                    String tb = sp.getString("TARBERTH");
                    Date mt = (Date) sp.get("MOVETIME");
                    for (int j = 0; j < this.berth.length(); j++) {
                        JSONObject bh = this.berth.getJSONObject(j);
                        String code = bh.getString("CODE");
                        boolean ok = false;
                        ok = new Date().compareTo(mt) <= 0 ? !code.equals(cb) && !code.equals(tb) : !code.equals(tb);
                        if (ok) {
                            JSONArray temp = (JSONArray) bh.getJSONObject("GEOM").getJSONArray("rings").get(0);
                            boolean flag = isPtInPoly(ship.getDouble("LONGITUDE"), ship.getDouble("LATITUDE"), temp);
                            if (flag) {
                                addWarning(ship, tb, code);
                                System.out.println("============>" + mmsi);
                                break;
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * 监控是否有新增的调拨通道途中监管异常报警
     *
     * @throws EcityException
     */
    @Scheduled(cron = "*/10 * * * * ?")
    public void job2() throws EcityException {
        IWorkspace stage = null;
        try {
            stage = getWorkspace("stage");
            stage.startEdit();
            String sql = "SELECT * FROM IMAP_WARNING_LOG2 WHERE ISHANDLED='N'";
            long toal = stage.getDb().executeSqlForTotal(sql);
            String update = "UPDATE IMAP_WARNING SET WARNING2=%S";
            stage.getDb().executeSqlNoQuery(String.format(update, toal));
            stage.endEdit();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            stage.rollbackEdit();
            e.printStackTrace();
        }
    }

    /**
     * 监控是否有新增的行政通道车辆识别异常报警
     *
     * @throws EcityException
     */
    @Scheduled(cron = "*/10 * * * * ?")
    public void job3() throws EcityException {
        IWorkspace stage = null;
        try {
            stage = getWorkspace("stage");
            stage.startEdit();
            String sql = "SELECT * FROM IMAP_WARNING_LOG3 WHERE ISHANDLED='N'";
            long toal = stage.getDb().executeSqlForTotal(sql);
            String update = "UPDATE IMAP_WARNING SET WARNING3=%S";
            stage.getDb().executeSqlNoQuery(String.format(update, toal));
            stage.endEdit();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            stage.rollbackEdit();
            e.printStackTrace();
        }
    }

    /**
     * 判断点是否在面上
     *
     * @param ALon
     * @param ALat
     * @param APoints
     * @return
     */
    public boolean isPtInPoly(double ALon, double ALat, JSONArray APoints) throws JSONException {
        int iSum = 0, iCount;
        double dLon1, dLon2, dLat1, dLat2, dLon;
        if (APoints.length() < 3)
            return false;
        iCount = APoints.length() - 1;
        for (int i = 0; i < iCount; i++) {
            if (i == iCount - 1) {
                dLon1 = APoints.getJSONArray(i).getDouble(0);
                dLat1 = APoints.getJSONArray(i).getDouble(1);
                dLon2 = APoints.getJSONArray(0).getDouble(0);
                dLat2 = APoints.getJSONArray(0).getDouble(1);
            } else {
                dLon1 = APoints.getJSONArray(i).getDouble(0);
                dLat1 = APoints.getJSONArray(i).getDouble(1);
                dLon2 = APoints.getJSONArray(i + 1).getDouble(0);
                dLat2 = APoints.getJSONArray(i + 1).getDouble(1);
            }
            // 以下语句判断A点是否在边的两端点的水平平行线之间，在则可能有交点，开始判断交点是否在左射线上
            if (((ALat >= dLat1) && (ALat < dLat2)) || ((ALat >= dLat2) && (ALat < dLat1))) {
                if (Math.abs(dLat1 - dLat2) > 0) {
                    // 得到 A点向左射线与边的交点的x坐标：
                    dLon = dLon1 - ((dLon1 - dLon2) * (dLat1 - ALat)) / (dLat1 - dLat2);
                    if (dLon < ALon)
                        iSum++;
                }
            }
        }
        if (iSum % 2 != 0)
            return true;
        return false;
    }

    /**
     * 移泊预警
     *
     * @param ship
     * @param croBerth
     * @param errBerth
     */
    public void addWarning(JSONObject ship, String croBerth, String errBerth) throws EcityException {
        try {
            ship.put("CROBERTH", croBerth);
            ship.put("ERRBERTH", errBerth);
            IWorkspace workspace = getWorkspace("stage");
            workspace.startEdit();
            workspace.getDb().executeSqlNoQuery("UPDATE IMAP_WARNING SET WARNING10 = WARNING10 + 1");
            ITableClass itc = workspace.getTableClass("IMAP_WARNING_LOG10");
            Record r = itc.createRecordByJSON(ship);
            itc.append(r);
            workspace.endEdit();
        } catch (Exception e) {
            workspace.rollbackEdit();
            e.printStackTrace();
        }
    }

    /**
     * 获取数据库连接
     *
     * @param name
     * @return
     */
    public IWorkspace getWorkspace(String name) {
        IWorkspace ws = null;
        try {
            String un = "";
            String pw = "";
            if (name.equals("skhg")) {
                un = "skhg";
                pw = "skhg";
            }
            if (name.equals("loader")) {
                un = "skhg_loader";
                pw = "loader";
            }
            if (name.equals("stage")) {
                un = "skhg_stage";
                pw = "stage";
            }
            ConnectionProperty conn = new ConnectionProperty();
            conn.connectionmode = EnumConnectionMode.EnumConnectionModeGdb;
            conn.dbInfo = new DB_INFO("oracle", "172.18.15.93:1521:OMP", un, pw, 10, 10);
            ws = WorkspaceFactory.getWorkspace(conn);
        } catch (EcityException e) {
            e.printStackTrace();
        }
        return ws;
    }
}
