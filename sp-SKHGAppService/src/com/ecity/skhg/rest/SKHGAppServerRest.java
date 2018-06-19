package com.ecity.skhg.rest;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;

public abstract interface SKHGAppServerRest {

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
}
