package com.ecity.skhg.impl;

import com.ecity.datasource.ConnectionProperty;
import com.ecity.datasource.IWorkspace;
import com.ecity.datasource.Workspace;
import com.ecity.datasource.WorkspaceFactory;
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

	IWorkspace workspace = null;
	private JSONObject berth = new JSONObject();

	public Job() {
		try {
			ConnectionProperty conn = new ConnectionProperty();
			conn.connectionmode = EnumConnectionMode.EnumConnectionModeGdb;
			conn.dbInfo = new DB_INFO("oracle", "172.18.15.93:1521:OMP", "skhg_loader", "loader", 10, 10);
			this.workspace = WorkspaceFactory.getWorkspace(conn);
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
				this.berth.put(r.getStringValue("CODE"), jo);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
	}

	@Scheduled(cron = "*/10 * * * * ?")
	public void job1() {
		try {
			String sql = "";
			this.workspace.getDb().executeSql(sql);
		} catch (EcityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Scheduled(cron = "*/10 * * * * ?")
	public void job2() {
		System.out.println("========>任务进行中2。。。");
	}

	public boolean isPtInPoly(double ALon, double ALat, double[][] APoints) {
		int iSum = 0, iCount;
		double dLon1, dLon2, dLat1, dLat2, dLon;
		if (APoints.length < 3)
			return false;
		iCount = APoints.length;
		for (int i = 0; i < iCount; i++) {
			if (i == iCount - 1) {
				dLon1 = APoints[i][0];
				dLat1 = APoints[i][1];
				dLon2 = APoints[0][0];
				dLat2 = APoints[0][1];
			} else {
				dLon1 = APoints[i][0];
				dLat1 = APoints[i][1];
				dLon2 = APoints[i + 1][0];
				dLat2 = APoints[i + 1][1];
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

}
