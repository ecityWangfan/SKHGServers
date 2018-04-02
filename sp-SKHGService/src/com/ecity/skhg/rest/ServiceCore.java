package com.ecity.skhg.rest;

import java.util.Collection;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import com.ecity.datasource.ConnectionProperty;
import com.ecity.datasource.IWorkspace;
import com.ecity.datasource.WorkspaceFactory;
import com.ecity.define.common.DB_INFO;
import com.ecity.define.enums.EnumConnectionMode;
import com.ecity.exception.EcityException;
import com.ecity.se.core.ConfigGroup;
import com.ecity.se.core.IService;
import com.ecity.se.core.Logger;
import com.ecity.se.core.Server;
import com.ecity.se.token.ITokenService;
import com.ecity.server.util.HttpServletUtil;
import com.ecity.server.util.ServerLoggerUtil;
import com.ecity.skhg.utils.StringUtil;

/**
 * 服务入口
 */
public class ServiceCore implements IService {
	
	private ConfigGroup mainGroup = null;
	private Server SEServer;
	private boolean needToken;
	private ServletContext context=null;
	private Logger logger = null;
	private ConnectionProperty conn = null;
	
	private String uploadFilePath;//存储附件路径
	private String defaultUploadPath="uploadAq";
	private String filePathFinal;
	
	public ServiceCore() {}

	public Server getServer() {	
		return this.SEServer;
	}
	
	/**
	 * 服务初始化
	 */
	public void setServer(Server seServer) {
		this.SEServer = seServer;
		this.logger = this.getServer().getLogger();
		this.mainGroup = SEServer.getConfig().getAt("SKHGSERVICE-CONFIG");
		
		String strUploadPath=mainGroup.getValue("UPLOADPATH");
		
		filePathFinal = (StringUtil.isEmpty(strUploadPath)?defaultUploadPath:strUploadPath);
		this.uploadFilePath = this.SEServer.getHome().substring(0, this.SEServer.getHome().lastIndexOf("\\", this.SEServer.getHome().length()-2)) + "\\map\\" + filePathFinal;
					
		try {
			//1.读取workspace的数据库连接配置
			loadWorkspaceConf();
			
			//2.初始化workspace,这样第一次访问服务时不会等待
			getWorkspace();
		}catch (Exception e) {
			e.printStackTrace();		
        }
	}
	
	
	/**
	 * 读取workspace的数据库配置
	 */
	private void loadWorkspaceConf(){
		String dbtype = this.mainGroup.getValue("DBTYPE");
		String dburl = this.mainGroup.getValue("DBURL");
		String name = this.mainGroup.getValue("DBNAME");
		String psw = this.mainGroup.getValue("DBPSW");
		int minnum = (int) this.mainGroup.getLongValue("MINNUM");
		int maxnum = (int) this.mainGroup.getLongValue("MAXNUM");
		
		conn = new ConnectionProperty();
		conn.connectionmode = EnumConnectionMode.EnumConnectionModeGdb;
		conn.dbInfo = new DB_INFO(dbtype, dburl, name, psw, minnum, maxnum);
	}
	
	/**
	 * 加载服务引用的jar文件，一般当前服务对应的jar文件放在services目录下的一个自定义文件夹中
	 */
    private void loadOwnerJar(){
        String servicePath=SEServer.getServicePath();
        String personalJarPath=servicePath+"/wxlib/";
        String typicalClass = "com.thoughtworks.xstream.XStream";//修改成引用jar中的任意存在的一个类路径
        
        if(SEServer.loadCustomLibs(typicalClass, personalJarPath))
        	System.out.println("依赖包加载成功");
    }
	
	
	/**
	 * 获取workspace
	 * @return
	 * @throws EcityException
	 */
	public IWorkspace getWorkspace() throws EcityException{
		return WorkspaceFactory.getWorkspace(conn);
	}
	
    /**
     * 写日志EcityException
     * @param req
     * @param e
     */
    public void writeLog(HttpServletRequest req, EcityException e){
        ServerLoggerUtil.writeServerLog(this.logger, Logger.FATAL, req, e);
    }

	/**
     * 写日志Exception
     * @param req
     * @param e
     */
    public void writeLog(HttpServletRequest req, Exception e){
        ServerLoggerUtil.writeServerLog(this.logger, Logger.FATAL, req, e);
    }
    
    /**
     * 写日志message
     * @param req
     * @param e
     */
    public void writeLog(HttpServletRequest req, String message){
        ServerLoggerUtil.writeServerLog(this.logger, Logger.FATAL, req, message);
    }
    
	public void setServletContext(ServletContext context) {
		this.context=context;	
	}

	public Collection<ConfigGroup> getConfigGroup() {
		return null;
	}

	public String getServiceName() {
		return "NBPortWeiXinServer(服务描述)";
	}

	public String getServiceDescription() {
		if ((this.mainGroup == null)
				|| (this.mainGroup.getAt("Name") == null)) {
			return "NBPortWeiXinServer(服务描述)";
		}
		return this.mainGroup.getAt("Name").description;
	}

	public String getServiceBrief() {
		if ((this.mainGroup == null)
				|| (this.mainGroup.getAt("Name") == null)) {
			return "NBPortWeiXinServer";
		}
		return this.mainGroup.getAt("Name").value;
	}

	public void clear() {
		System.out.println("NBPortWeiXinServer close completed！");
	}
	
	/**
	 * 获取文件上传路径
	 * @return
	 */
	public String getUploadFilePath(){ 
		return this.uploadFilePath;
	}
	
	public String getFilePathFinal() {
		return this.filePathFinal;
	}
	
	
	/**
	 * 验证token
	 * @param req
	 * @param token
	 * @throws EcityException
	 */
	public void verifyToken(HttpServletRequest req) throws EcityException{
		if( this.needToken){
		    String token = null;
			if( token == null || token.length() == 0 ){
				token = req.getParameter("token") == null ? "" : req.getParameter("token");
			}
			ITokenService tokenService = this.SEServer.getTokenService();
			String clientId = HttpServletUtil.getReferUrl(req);
			clientId += ",http://" + HttpServletUtil.getRealClentIP(req) + "," + req.getRequestURI();
			if( HttpServletUtil.isMobileDevice(req) ){
				clientId += ",mobile";
			}
			if( !tokenService.verifyToken(token, clientId) ){
				throw new EcityException("Token is not verified or expired.");
			}
		}
	}

}