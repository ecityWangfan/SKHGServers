package com.ecity.skhg.impl;

import com.ecity.datasource.IWorkspace;
import com.ecity.exception.EcityException;
import com.ecity.skhg.rest.ServiceCore;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class IcommandManager {
    /**
     * 服务入口
     */
    ServiceCore serviceCore;

    /**
     * 工作空间
     */
    IWorkspace workspace;

    private ServiceCore CORE;

    public IcommandManager() {

    }

    public IcommandManager(ServiceCore serviceCore) throws EcityException {
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

    public JSONObject getUserSig(String user) throws JSONException {
        //传入的参数
        JSONObject result = null;
        try {
            JSONObject loginData = login(user);
            int code = loginData.getInt("errorCode");
            if (code == 10005) { //直播账户不存在
                JSONObject registData = regist(user);
                int errorCode = registData.getInt("errorCode");
                if (errorCode == 0) { // 新建直播账户成功
                    result = login(user);
                } else {
                    result = registData;
                }
            } else {
                result = loginData;
            }
        } catch (Exception e) {
            e.printStackTrace();
            result = new JSONObject();
            result.put("errorCode", -1);
            result.put("errorInfo", e.getMessage());
        }
        return result;
    }

    public JSONObject login(String user) throws JSONException {
        JSONObject result = null;
        try {
            JSONObject jo = new JSONObject();
            jo.put("id", user);
            jo.put("pwd", "123456");
            String loginUrl = "http://skhg.eor.cc/index.php?svc=account&cmd=login";
            String data = sendPostRequest(loginUrl, jo.toString());
            data = data.substring(data.indexOf("{"));
            result = new JSONObject(data);
        } catch (Exception e) {
            e.printStackTrace();
            result = new JSONObject();
            result.put("errorCode", -1);
            result.put("errorInfo", e.getMessage());
        }
        return result;
    }

    public JSONObject regist(String user) throws JSONException {
        JSONObject result = null;
        try {
            JSONObject jo = new JSONObject();
            jo.put("id", user);
            jo.put("pwd", "123456");
            String registUrl = "http://skhg.eor.cc/index.php?svc=account&cmd=regist";
            String data = sendPostRequest(registUrl, jo.toString());
            data = data.substring(data.indexOf("{"));
            result = new JSONObject(data);
        } catch (Exception e) {
            e.printStackTrace();
            result = new JSONObject();
            result.put("errorCode", -1);
            result.put("errorInfo", e.getMessage());
        }
        return result;
    }

    public String sendPostRequest(String url, String param) {
        HttpURLConnection httpURLConnection = null;
        OutputStream out = null; //写
        InputStream in = null;   //读
        int responseCode = 0;    //远程主机响应的HTTP状态码
        String result = "";
        try {
            URL sendUrl = new URL(url);
            httpURLConnection = (HttpURLConnection) sendUrl.openConnection();
            //post方式请求
            httpURLConnection.setRequestMethod("POST");
            //设置头部信息
            httpURLConnection.setRequestProperty("headerdata", "ceshiyongde");
            //一定要设置 Content-Type 要不然服务端接收不到参数
            httpURLConnection.setRequestProperty("Content-Type", "application/Json; charset=UTF-8");
            //指示应用程序要将数据写入URL连接,其值默认为false（是否传参）
            httpURLConnection.setDoOutput(true);
            //httpURLConnection.setDoInput(true);

            httpURLConnection.setUseCaches(false);
            httpURLConnection.setConnectTimeout(30000); //30秒连接超时
            httpURLConnection.setReadTimeout(30000);    //30秒读取超时
            //传入参数
            out = httpURLConnection.getOutputStream();
            out.write(param.getBytes());
            out.flush(); //清空缓冲区,发送数据
            out.close();
            responseCode = httpURLConnection.getResponseCode();
            //获取请求的资源
            BufferedReader br = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream(), "UTF-8"));
            result = br.readLine();
        } catch (Exception e) {
            e.printStackTrace();

        }
        return result;
    }
}
