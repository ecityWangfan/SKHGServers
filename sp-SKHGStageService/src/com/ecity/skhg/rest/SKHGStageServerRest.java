package com.ecity.skhg.rest;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;

public abstract interface SKHGStageServerRest {

    /**
     * 上传文件
     *
     * @param req
     * @return
     * @throws Exception
     */
    @POST
    @Path("/upImg")
    @Produces({"application/json", "application/xml", "text/html"})
    public abstract Object upImg(
            @Context HttpServletRequest req,
            @QueryParam("f") @DefaultValue("json") String f,
            @QueryParam("gid") @DefaultValue("0") long gid) throws Exception;

    /**
     * 插入区域信息
     *
     * @param req
     * @param f
     * @param datas
     * @return
     * @throws Exception
     */
    @POST
    @Path("/insertArea")
    @Produces({"application/json", "application/xml", "text/html"})
    public abstract Object insertArea(
            @Context HttpServletRequest req,
            @QueryParam("f") @DefaultValue("json") String f,
            @QueryParam("datas") @DefaultValue("") String datas) throws Exception;

    /**
     * 通过条件获取区域信息
     *
     * @param req
     * @param f
     * @param where
     * @return
     * @throws Exception
     */
    @GET
    @Path("/getAreaByWhere")
    @Produces({"application/json", "application/xml", "text/html"})
    public abstract Object getAreaByWhere(
            @Context HttpServletRequest req,
            @QueryParam("f") @DefaultValue("json") String f,
            @QueryParam("where") @DefaultValue("1=1") String where) throws Exception;

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
            @QueryParam("gid") @DefaultValue("0") int gid,
            @QueryParam("rings") @DefaultValue("[]") String rings) throws Exception;

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

    /**
     * 查询空间表
     *
     * @param req
     * @param f
     * @param tableName
     * @param where
     * @return
     * @throws Exception
     */
    @GET
    @Path("/queryGeomTable")
    @Produces({"application/json", "application/xml", "text/html"})
    public abstract Object queryGeomTable(
            @Context HttpServletRequest req,
            @QueryParam("f") @DefaultValue("json") String f,
            @QueryParam("tableName") @DefaultValue("") String tableName,
            @QueryParam("where") @DefaultValue("1=1") String where) throws Exception;
}
