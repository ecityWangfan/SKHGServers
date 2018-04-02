package com.ecity.skhg.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import org.apache.cxf.jaxrs.ext.multipart.Attachment;
import org.springframework.util.StringUtils;

/**
 * 文件操作工具类
 * @author DYLEE
 *
 */
public class FileUtils {
	/**
	 * 获取文件扩展名
	 * 
	 * @param filename
	 * @return
	 */
	public static String getExtend(String filename) {
		return getExtend(filename, "");
	}

	/**
	 * 获取文件扩展名
	 * 
	 * @param filename
	 * @return
	 */
	public static String getExtend(String filename, String defExt) {
		if ((filename != null) && (filename.length() > 0)) {
			int i = filename.lastIndexOf('.');

			if ((i > 0) && (i < (filename.length() - 1))) {
				return (filename.substring(i+1)).toLowerCase();
			}
		}
		return defExt.toLowerCase();
	}

	/**
	 * 获取文件名称[不含后缀名]
	 * 
	 * @param
	 * @return String
	 */
	public static String getFilePrefix(String fileName) {
		int splitIndex = fileName.lastIndexOf(".");
		return fileName.substring(0, splitIndex).replaceAll("\\s*", "");
	}
	
	/**
	 * 获取文件名称[不含后缀名]
	 * 不去掉文件目录的空格
	 * @param
	 * @return String
	 */
	public static String getFilePrefix2(String fileName) {
		int splitIndex = fileName.lastIndexOf(".");
		return fileName.substring(0, splitIndex);
	}
	
	/**
	 * 文件复制
	 *方法摘要：这里一句话描述方法的用途
	 *@param
	 *@return void
	 */
	public static void copyFile(String inputFile,String outputFile) throws FileNotFoundException{
		File sFile = new File(inputFile);
		File tFile = new File(outputFile);
		FileInputStream fis = new FileInputStream(sFile);
		FileOutputStream fos = new FileOutputStream(tFile);
		int temp = 0;  
		byte[] buf = new byte[10240];
        try {  
        	while((temp = fis.read(buf))!=-1){   
        		fos.write(buf, 0, temp);   
            }   
        } catch (IOException e) {  
            e.printStackTrace();  
        } finally{
            try {
            	fis.close();
            	fos.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
        } 
	}
	/**
	 * 判断文件是否为图片<br>
	 * <br>
	 * 
	 * @param filename
	 *            文件名<br>
	 *            判断具体文件类型<br>
	 * @return 检查后的结果<br>
	 * @throws Exception
	 */
	public static boolean isPicture(String filename) {
		// 文件名称为空的场合
		if (filename==null || filename.equals("")) {
			// 返回不和合法
			return false;
		}
		// 获得文件后缀名
		//String tmpName = getExtend(filename);
		String tmpName = filename;
		// 声明图片后缀名数组
		String imgeArray[][] = { { "bmp", "0" }, { "dib", "1" },
				{ "gif", "2" }, { "jfif", "3" }, { "jpe", "4" },
				{ "jpeg", "5" }, { "jpg", "6" }, { "png", "7" },
				{ "tif", "8" }, { "tiff", "9" }, { "ico", "10" } };
		// 遍历名称数组
		for (int i = 0; i < imgeArray.length; i++) {
			// 判断单个类型文件的场合
			if (imgeArray[i][0].equals(tmpName.toLowerCase())) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 判断文件是否为DWG<br>
	 * <br>
	 * 
	 * @param filename
	 *            文件名<br>
	 *            判断具体文件类型<br>
	 * @return 检查后的结果<br>
	 * @throws Exception
	 */
	public static boolean isDwg(String filename) {
		// 文件名称为空的场合
		if (filename==null || filename.equals("")) {
			// 返回不和合法
			return false;
		}
		// 获得文件后缀名
		String tmpName = getExtend(filename);
		// 声明图片后缀名数组
		if (tmpName.equals("dwg")) {
			return true;
		}
		return false;
	}
	
	/**
	 * 删除指定的文件
	 * 
	 * @param strFileName
	 *            指定绝对路径的文件名
	 * @return 如果删除成功true否则false
	 */
	public static boolean delete(String strFileName) {
		File fileDelete = new File(strFileName);

		if (!fileDelete.exists() || !fileDelete.isFile()) {
			
			return false;
		}
		
		return fileDelete.delete();
	}
	
	/**
	 * 
	* @Title: encodingFileName
	* @Description: 防止文件名中文乱码含有空格时%20 
	* @param @param fileName
	* @param @return    设定文件 
	* @return String    返回类型 
	* @throws
	 */
	public static String encodingFileName(String fileName) {
        String returnFileName = "";
        try {
            returnFileName = URLEncoder.encode(fileName, "UTF-8");
            returnFileName = StringUtils.replace(returnFileName, "+", "%20");
            if (returnFileName.length() > 150) {
                returnFileName = new String(fileName.getBytes("GB2312"), "ISO8859-1");
                returnFileName = StringUtils.replace(returnFileName, " ", "%20");
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return returnFileName;
    }
	
	/**
	 * 文件保存
	 * 
	 * @param attachment
	 * @param savePath
	 * @return 文件名字
	 * @throws IOException
	 */
	public static String saveAttachment(Attachment attachment, String savePath)throws IOException {

		String newFileName = "";
		InputStream inStream = null;
		OutputStream outStream = null;
		try {

			String fileName = attachment.getDataHandler().getName();
			fileName = new String(fileName.getBytes("ISO8859-1"), "UTF-8");
			String prefix = fileName.substring(fileName.lastIndexOf(".") + 1);
			newFileName = System.currentTimeMillis() + "." + prefix;
			String strFilePath = String.format("%s%s%s", savePath,File.separator, newFileName);
			File dataFile = new File(strFilePath);

			if (!dataFile.getParentFile().exists()) {
				dataFile.getParentFile().mkdirs();
			}

			inStream = attachment.getDataHandler().getInputStream();
			outStream = new FileOutputStream(new File(strFilePath));
			int read = 0;
			byte[] bytes = new byte[1024];

			while ((read = inStream.read(bytes)) != -1) {
				outStream.write(bytes, 0, read);
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (outStream != null)outStream.flush();
			if (outStream != null)outStream.close();
			if (inStream != null)inStream.close();
		}

		return newFileName;
	}
}
