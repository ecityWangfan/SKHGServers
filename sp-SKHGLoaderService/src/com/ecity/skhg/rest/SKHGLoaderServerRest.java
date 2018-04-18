package com.ecity.skhg.rest;

import javax.servlet.http.HttpServletRequest;
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
}
