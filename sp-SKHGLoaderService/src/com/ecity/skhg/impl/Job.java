package com.ecity.skhg.impl;

import com.ecity.datasource.ConnectionProperty;
import com.ecity.datasource.IWorkspace;
import com.ecity.datasource.Workspace;
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
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component("taskJob")
public class Job {

    private Workspace workspace;
    private JSONObject berth = new JSONObject();

    public Job() {
        try {
            ConnectionProperty conn = new ConnectionProperty();
            conn.connectionmode = EnumConnectionMode.EnumConnectionModeGdb;
            conn.dbInfo = new DB_INFO("oracle", "172.18.15.93:1521:OMP", "skhg_loader", "loader", 5, 10);
            this.workspace = new Workspace(conn);
            IFeatureClass ifc = this.workspace.getFeatureClass("YLMG_BERTH");
            ITableClass itc = this.workspace.getTableClass("YLMG_BERTH");
            QueryFilter qf = new QueryFilter();
            qf.setWhere("1=1");
            System.out.println("===============>" + itc);
            List<Record> list = itc.search(qf);
            for (int i = 0; i < list.size(); i++) {
                Record r = list.get(i);
                JSONObject jo = new JSONObject();
                jo.put("CODE", r.getStringValue("CODE"));
                jo.put("NAME", r.getStringValue("NAME"));
                Geometry g = ifc.getGeometryBySqlGeometry(r.getObjectValue("GEOM"));
                jo.put("GEOM", g.toJSON());
                berth.put(r.getStringValue("CODE"), jo);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Scheduled(cron = "*/10 * * * * ?")
    public void job1() {
        System.out.println("========>任务进行中1。。。");
        System.out.println("========>" + berth.toString());
    }

    @Scheduled(cron = "*/10 * * * * ?")
    public void job2() {
        System.out.println("========>任务进行中2。。。");

    }

}


