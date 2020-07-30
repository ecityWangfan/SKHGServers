package com.ecity.skhg.rest;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;

public abstract interface SKHGLoaderServerRest {

    /**
     * 通过条件查询表
     *
     * @param req
     * @param f
     * @param tableName
     * @param where
     * @return
     * @throws Exception
     */
    @GET
    @Path("/queryTableByWhere")
    @Produces({"application/json", "application/xml", "text/html"})
    public abstract Object queryTableByWhere(
            @Context HttpServletRequest req,
            @QueryParam("f") @DefaultValue("json") String f,
            @QueryParam("tableName") @DefaultValue("") String tableName,
            @QueryParam("where") @DefaultValue("1=1") String where) throws Exception;

    @GET
    @Path("/queryPro")
    @Produces({"application/json", "application/xml", "text/html"})
    public abstract Object querProGL(
            @Context HttpServletRequest req,
            @QueryParam("f") @DefaultValue("json") String f,
            @QueryParam("proName") @DefaultValue("") String proName,
            @QueryParam("parms") @DefaultValue("") String parms) throws Exception;

    /**
     * 执行非查询sql语句
     *
     * @param req
     * @param f
     * @param sql
     * @return
     * @throws Exception
     */
    @GET
    @Path("/excuteSqlNoQuery")
    @Produces({"application/json", "application/xml", "text/html"})
    public abstract Object excuteSqlNoQuery(
            @Context HttpServletRequest req,
            @QueryParam("f") @DefaultValue("json") String f,
            @QueryParam("sql") @DefaultValue("") String sql) throws Exception;

    /**
     * 同意旅检移泊申请
     *
     * @param req
     * @param f
     * @param mmsi
     * @return
     * @throws Exception
     */
    @GET
    @Path("/ljybTy")
    @Produces({"application/json", "application/xml", "text/html"})
    public abstract Object ljybTy(
            @Context HttpServletRequest req,
            @QueryParam("f") @DefaultValue("json") String f,
            @QueryParam("mmsi") @DefaultValue("") String mmsi) throws Exception;

    /**
     * 取消旅检移泊申请
     *
     * @param req
     * @param f
     * @param mmsi
     * @return
     * @throws Exception
     */
    @GET
    @Path("/ljybQx")
    @Produces({"application/json", "application/xml", "text/html"})
    public abstract Object ljybQx(
            @Context HttpServletRequest req,
            @QueryParam("f") @DefaultValue("json") String f,
            @QueryParam("mmsi") @DefaultValue("") String mmsi) throws Exception;

    /**
     * 同意旅检到泊申请
     *
     * @param req
     * @param f
     * @param mmsi
     * @return
     * @throws Exception
     */
    @GET
    @Path("/ljdbTy")
    @Produces({"application/json", "application/xml", "text/html"})
    public abstract Object ljdbTy(
            @Context HttpServletRequest req,
            @QueryParam("f") @DefaultValue("json") String f,
            @QueryParam("mmsi") @DefaultValue("") String mmsi) throws Exception;

    /**
     * 取消旅检到泊申请
     *
     * @param req
     * @param f
     * @param mmsi
     * @return
     * @throws Exception
     */
    @GET
    @Path("/ljdbQx")
    @Produces({"application/json", "application/xml", "text/html"})
    public abstract Object ljdbQx(
            @Context HttpServletRequest req,
            @QueryParam("f") @DefaultValue("json") String f,
            @QueryParam("mmsi") @DefaultValue("") String mmsi) throws Exception;

    /**
     * 通过gid更新地理信息
     *
     * @param req
     * @param f
     * @param gid
     * @param rings
     * @return
     * @throws Exception
     */
    @GET
    @Path("/updateGeomByGid")
    @Produces({"application/json", "application/xml", "text/html"})
    public abstract Object updateGeomByGid(
            @Context HttpServletRequest req,
            @QueryParam("f") @DefaultValue("json") String f,
            @QueryParam("tableName") @DefaultValue("") String tableName,
            @QueryParam("gid") @DefaultValue("0") int gid,
            @QueryParam("rings") @DefaultValue("[]") String rings) throws Exception;

    /**
     * 获取互动直播用户签名
     *
     * @param req
     * @param f
     * @param user
     * @return
     * @throws Exception
     */
    @GET
    @Path("/getUserSig")
    @Produces({"application/json", "application/xml", "text/html"})
    public abstract Object getUserSig(
            @Context HttpServletRequest req,
            @QueryParam("f") @DefaultValue("json") String f,
            @QueryParam("user") @DefaultValue("") String user) throws Exception;
    
  
    /**
     * 新增或修改疫情
     * @return
     * @throws Exception
     */
    @GET
    @Path("/AddOrUpdataVirusData")
    @Produces({"application/json", "application/xml", "text/html"})
    public abstract Object AddOrUpdataVirusData(
            @Context HttpServletRequest req,
            @QueryParam("country") @DefaultValue("") String country,
            @QueryParam("country_code") @DefaultValue("") String country_code,
            @QueryParam("port") @DefaultValue("") String port,
            @QueryParam("port_code") @DefaultValue("") String port_code,
            @QueryParam("virusType") @DefaultValue("") String virusType,
            @QueryParam("isUsed") @DefaultValue("") String isUsed,
            @QueryParam("startTime") @DefaultValue("") String startTime,
            @QueryParam("endTime") @DefaultValue("") String endTime,
            @QueryParam("gid") @DefaultValue("") String gid) throws Exception;
    
    
    /**
     * 获取摄像头上下级关系
     * @return
     * @throws Exception
     */
    @GET
    @Path("/GetNextData")
    @Produces({"application/json", "application/xml", "text/html"})
    public abstract Object GetNextData( @Context HttpServletRequest req) throws Exception;
    
    
    /**
     * 新增或修改摄像头配置
     * @return
     * @throws Exception
     */
    @GET
    @Path("/AddOrUpdataCamera")
    @Produces({"application/json", "application/xml", "text/html"})
    public abstract Object AddOrUpdataCamera(
            @Context HttpServletRequest req,
            @QueryParam("WHART_NAME") @DefaultValue("") String WHART_NAME,
            @QueryParam("WHARF_ID") @DefaultValue("") String WHARF_ID,
            @QueryParam("BERTH_NAME") @DefaultValue("") String BERTH_NAME,
            @QueryParam("BERTH_ID") @DefaultValue("") String BERTH_ID,
            @QueryParam("REGION_NAME") @DefaultValue("") String REGION_NAME,
            @QueryParam("REGION_ID") @DefaultValue("") String REGION_ID,
            @QueryParam("CEMERA_NAME") @DefaultValue("") String CEMERA_NAME,
            @QueryParam("CAMERA_ID") @DefaultValue("") String CAMERA_ID,            
            @QueryParam("BELONG_PORT") @DefaultValue("") String BELONG_PORT,
            @QueryParam("BELONG_BERTH") @DefaultValue("") String BELONG_BERTH,
            @QueryParam("BELONG_VALUE") @DefaultValue("") String BELONG_VALUE,
            @QueryParam("LOCATION") @DefaultValue("") String LOCATION,   
            @QueryParam("GEOGID") @DefaultValue("") String GEOGID,        
            @QueryParam("GID") @DefaultValue("") String GID) throws Exception;
    
    
    /**
     * 获取摄像头信息
     * @return
     * @throws Exception
     */
    @GET
    @Path("/GetCameraInfo")
    @Produces({"application/json", "application/xml", "text/html"})
    public abstract Object GetCameraInfo( @Context HttpServletRequest req,
    		 @QueryParam("GEOGID") @DefaultValue("") String GEOGID,   
             @QueryParam("BELONG_VALUE") @DefaultValue("") String BELONG_VALUE) throws Exception;
    
    
    
    /**
     * 获取摄像头信息
     * @return
     * @throws Exception
     */
    @GET
    @Path("/GetPdfInfo")
    @Produces({"application/json", "application/xml", "text/html"})
    public abstract void GetPdfInfo( @Context HttpServletRequest req,
            @Context HttpServletResponse response) throws Exception;
    
    
    
    /**
     * 获取摄像头信息
     * @return
     * @throws Exception
     */
    @GET
    @Path("/queryGeomCamera")
    @Produces({"application/json", "application/xml", "text/html"})
    public abstract Object queryGeomCamera( @Context HttpServletRequest req,
    		 @QueryParam("f") @DefaultValue("json") String f,
    		 @QueryParam("tableName") @DefaultValue("") String tableName,
    		 @QueryParam("where") @DefaultValue("") String where) throws Exception;
    
    
   
    
    

}
