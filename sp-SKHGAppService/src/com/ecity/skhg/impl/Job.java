package com.ecity.skhg.impl;

import java.util.Date;
import java.util.List;

import org.json.JSONObject;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.ecity.datasource.ConnectionProperty;
import com.ecity.datasource.IWorkspace;
import com.ecity.datasource.WorkspaceFactory;
import com.ecity.datatable.DataRowCollection;
import com.ecity.datatable.DataTable;
import com.ecity.define.common.DB_INFO;
import com.ecity.define.enums.EnumConnectionMode;
import com.ecity.exception.EcityException;
import com.ecity.feature.QueryFilter;
import com.ecity.notification.android.NotificationService4Android;
import com.ecity.notification.exception.InvalidConfigurationException;
import com.ecity.notification.exception.NotificationException;
import com.ecity.table.ITableClass;
import com.ecity.table.Record;

@Component("taskJob")
public class Job {

	private NotificationService4Android service = null;

	public Job() {
		service = NotificationService4Android.getInstance();
		try {
			service.init(2100293425, "b0bd9ce4ef54ba0ee14a68c9745884dc");
		} catch (InvalidConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * APP报警消息推送
	 */
	@Scheduled(cron = "0 */10 * * * ?")
	public void job1() {
		/*
		try {
			
			JSONObject warning = getWarningData();
			IWorkspace iw = getWorkspace("stage");
			ITableClass itc = iw.getTableClass("SK_XG_MAPPING");
			QueryFilter qf = new QueryFilter();
			qf.setWhere("1=1");
			List<Record> list = itc.search(qf);
			for (int i = 0; i < list.size(); i++) {
				Record r = list.get(i);
				String[] keys = r.getStringValue("WARNING").split(",");
				int warningNum = 0;
				for (int j = 0; j < keys.length; j++) {
					warningNum = warningNum + warning.getInt(keys[j]);
				}
				if (warningNum > 0) {
					String sql = "SELECT * FROM U_USER WHERE ORGID='"
							+ r.getStringValue("BMID") + "'";
					IWorkspace iws = getWorkspace("loader");
					DataTable dt = iws.getDb().executeSql(sql);
					DataRowCollection drc = dt.getRows();
					for (int k = 0; k < drc.size(); k++) {
						String user = drc.get(k).getString("GID");
						JSONObject json = new JSONObject();
						json.put("msg", "您有新的报警信息，请及时查收！");
						json.put("sendtime", new Date().toLocaleString());
						try {
							service.push2User(json, user);
						} catch (NotificationException e) {
							// TODO Auto-generated catch block
							System.out.println(e.getMessage());
						}
					}
				}
			}
			
		} catch (EcityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		*/
	}

	/**
	 * 获取报警信息统计数据
	 * 
	 * @return
	 */
	public JSONObject getWarningData() {
		JSONObject waning = new JSONObject();
		try {
			IWorkspace iw = getWorkspace("stage");
			ITableClass itc = iw.getTableClass("MT_ATT_FIELD");
			QueryFilter qf = new QueryFilter();
			qf.setWhere("TABLENAME='IMAP_WARNING' AND VISIBLE=1");
			List<Record> list = itc.search(qf);

			ITableClass itc1 = iw.getTableClass("IMAP_WARNING");
			QueryFilter qf1 = new QueryFilter();
			qf1.setWhere("1=1");
			List<Record> list1 = itc1.search(qf1);

			Record r = list1.get(0);
			for (int i = 0; i < list.size(); i++) {
				String name = list.get(i).getStringValue("NAME");
				waning.put(name, r.getIntValue(name));
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return waning;
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
			conn.dbInfo = new DB_INFO("oracle", "172.18.15.93:1521:OMP", un,
					pw, 10, 10);
			ws = WorkspaceFactory.getWorkspace(conn);
		} catch (EcityException e) {
			e.printStackTrace();
		}
		return ws;
	}
}
