package com.ecity.skhg.utils;

import java.io.IOException;
import java.io.OutputStream;
import java.net.MalformedURLException;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;



public class PdfUtil {
	
	  public Font getChineseFont(int type) {

	        BaseFont simpChinese;

	        Font ChineseFont = null;

	        try {
                if(type==0){
                	simpChinese = BaseFont.createFont("STSongStd-Light", "UniGB-UCS2-H", BaseFont.NOT_EMBEDDED);

    	            ChineseFont = new Font(simpChinese, 16, Font.NORMAL, BaseColor.BLACK);
                }else if(type==2){
                	simpChinese = BaseFont.createFont("STSongStd-Light", "UniGB-UCS2-H", BaseFont.EMBEDDED);

    	            ChineseFont = new Font(simpChinese, 16,Font.NORMAL, BaseColor.BLACK);
                }else{
                	BaseFont bfChinese = BaseFont.createFont("STSong-Light", "UniGB-UCS2-H",
    	                    BaseFont.NOT_EMBEDDED);
                	ChineseFont= new Font(bfChinese, 30, Font.NORMAL,BaseColor.BLACK);
                }
	            
	      
	        } catch (DocumentException e) {

	            e.printStackTrace();

	        } catch (IOException e) {

	            e.printStackTrace();

	        }

	        return ChineseFont;

	    }


	    public void test(OutputStream out) throws DocumentException, MalformedURLException, IOException {

	    	//OutputStream out = new FileOutputStream("D:\\春.pdf");// 设置页面的属性 

	        Rectangle tRectangle = new Rectangle(PageSize.A4); // 页面大小 

	        tRectangle.setBackgroundColor(BaseColor.WHITE); // 页面背景色 

	        tRectangle.setBorder(1220);// 边框 

	        tRectangle.setBorderColor(BaseColor.BLUE);// 边框颜色 

	        tRectangle.setBorderWidth(244.2f);// 边框宽度 

	        Document doc = new Document(tRectangle,100, 100, 200, 100);// 定义文档  

	        doc = new Document(tRectangle.rotate());// 横向打印 

	        PdfWriter writer = PdfWriter.getInstance(doc, out);// 书写器 

	        writer.setPdfVersion(PdfWriter.PDF_VERSION_1_2);//版本(默认1.4) //设置PDF文档属性 

	        doc.addTitle("通关时效检测报告");// 标题 

	        

	        Paragraph tParagraph = new Paragraph("通关时效检测报告", getChineseFont(1));

	        tParagraph.setAlignment(Element.ALIGN_JUSTIFIED);// 对齐方式 

	        tParagraph.setAlignment(Element.ALIGN_CENTER);// 居中

	        tParagraph.setIndentationLeft(12);// 左缩进 

	        tParagraph.setIndentationRight(12);// 右缩进 

	        tParagraph.setFirstLineIndent(24);// 首行缩进 
	        
	        
	        Paragraph tParagraphs = new Paragraph("时间: 2020年6月", getChineseFont(0));
	        tParagraphs.setAlignment(Element.ALIGN_RIGHT);// 对齐方式 
	        tParagraphs.setAlignment(Element.ALIGN_RIGHT);// 右侧
	        tParagraphs.setIndentationRight(150);// 右缩进 
	        
	        
	        Paragraph tParagraphs1 = new Paragraph("出口通过整体时效", getChineseFont(0));
	        tParagraphs1.setAlignment(Element.ALIGN_LEFT);// 对齐方式 
	        tParagraphs1.setAlignment(Element.ALIGN_LEFT);// 左侧
	        tParagraphs1.setIndentationLeft(50);// 左缩进 


	        doc.setMargins(10, 20, 30, 40);// 页边空白 

	        Image img = Image.getInstance("C:\\Users\\wangfan\\Desktop\\test1.png");

	        img.setAlignment(Image.MIDDLE);//设置图片居中

	        img.setBorder(Image.BOX);

	        img.setBorderWidth(10);

	        img.setBorderColor(BaseColor.WHITE);

	        img.scaleToFit(800, 40);// 设置图片大小 
	        
	        
	        
	        PdfPTable t = new PdfPTable(6);
	        
	        t.setSpacingBefore(25);
	         
	        t.setSpacingAfter(25);
	       // mergeColAndRow "海关全口径（入闸到单证放行）
	        PdfPCell c1 =  new PdfPCell(new Paragraph("海关全口径（入闸到单证放行）", getChineseFont(2))); 
	        c1.setColspan(3);
	        c1.setHorizontalAlignment(com.lowagie.text.Element.ALIGN_CENTER);
	        c1.setVerticalAlignment(com.lowagie.text.Element.ALIGN_MIDDLE);
	        t.addCell(c1);
	         
	        PdfPCell c2 =new PdfPCell(new Paragraph("货物提离",getChineseFont(2)));//new PdfPCell(new Phrase("Header1")); 
	        c2.setColspan(3);
	        c2.setHorizontalAlignment(com.lowagie.text.Element.ALIGN_CENTER);
	        c2.setVerticalAlignment(com.lowagie.text.Element.ALIGN_MIDDLE);
	        t.addCell(c2);
	         
	         
	        c1 =  new PdfPCell(new Paragraph("平均时间", getChineseFont(2))); 
	        c1.setHorizontalAlignment(com.lowagie.text.Element.ALIGN_CENTER);
	        c1.setVerticalAlignment(com.lowagie.text.Element.ALIGN_MIDDLE);
	        t.addCell(c1); 
	        
	        c1 =  new PdfPCell(new Paragraph("比去年均值", getChineseFont(2))); 
	        c1.setHorizontalAlignment(com.lowagie.text.Element.ALIGN_CENTER);
	        c1.setVerticalAlignment(com.lowagie.text.Element.ALIGN_MIDDLE);
	        t.addCell(c1); 
	        
	        c1 =  new PdfPCell(new Paragraph("比目标值", getChineseFont(2))); 
	        c1.setHorizontalAlignment(com.lowagie.text.Element.ALIGN_CENTER);
	        c1.setVerticalAlignment(com.lowagie.text.Element.ALIGN_MIDDLE);
	        t.addCell(c1); 
	        
	        c1 =  new PdfPCell(new Paragraph("平均时间", getChineseFont(2))); 
	        c1.setHorizontalAlignment(com.lowagie.text.Element.ALIGN_CENTER);
	        c1.setVerticalAlignment(com.lowagie.text.Element.ALIGN_MIDDLE);
	        t.addCell(c1); 
	        
	        c1 =  new PdfPCell(new Paragraph("比去年均值", getChineseFont(2))); 
	        c1.setHorizontalAlignment(com.lowagie.text.Element.ALIGN_CENTER);
	        c1.setVerticalAlignment(com.lowagie.text.Element.ALIGN_MIDDLE);
	        t.addCell(c1); 
	        
	        c1 =  new PdfPCell(new Paragraph("比目标值", getChineseFont(2))); 
	        c1.setHorizontalAlignment(com.lowagie.text.Element.ALIGN_CENTER);
	        c1.setVerticalAlignment(com.lowagie.text.Element.ALIGN_MIDDLE);
	        t.addCell(c1); 
	        
	        
	        
	        c1 =  new PdfPCell(new Paragraph("0.88天", getChineseFont(2))); 
	        c1.setHorizontalAlignment(com.lowagie.text.Element.ALIGN_CENTER);
	        c1.setVerticalAlignment(com.lowagie.text.Element.ALIGN_MIDDLE);
	        t.addCell(c1); 
	        
	        c1 =  new PdfPCell(new Paragraph("低 0.45 天 45%", getChineseFont(2))); 
	        c1.setHorizontalAlignment(com.lowagie.text.Element.ALIGN_CENTER);
	        c1.setVerticalAlignment(com.lowagie.text.Element.ALIGN_MIDDLE);
	        t.addCell(c1); 
	        
	        c1 =  new PdfPCell(new Paragraph("低 0.20 天 18%", getChineseFont(2))); 
	        c1.setHorizontalAlignment(com.lowagie.text.Element.ALIGN_CENTER);
	        c1.setVerticalAlignment(com.lowagie.text.Element.ALIGN_MIDDLE);
	        t.addCell(c1); 
	        
	        
	        c1 =  new PdfPCell(new Paragraph("3.18 天", getChineseFont(2))); 
	        c1.setHorizontalAlignment(com.lowagie.text.Element.ALIGN_CENTER);
	        c1.setVerticalAlignment(com.lowagie.text.Element.ALIGN_MIDDLE);
	        t.addCell(c1); 
	        
	        c1 =  new PdfPCell(new Paragraph("低 3.18 天 23%", getChineseFont(2))); 
	        c1.setHorizontalAlignment(com.lowagie.text.Element.ALIGN_CENTER);
	        c1.setVerticalAlignment(com.lowagie.text.Element.ALIGN_MIDDLE);
	        t.addCell(c1); 
	        
	        c1 =  new PdfPCell(new Paragraph("低 0.20 天 20%", getChineseFont(2))); 
	        c1.setHorizontalAlignment(com.lowagie.text.Element.ALIGN_CENTER);
	        c1.setVerticalAlignment(com.lowagie.text.Element.ALIGN_MIDDLE);
	        t.addCell(c1); 

	        
	        doc.open();// 打开文档 

	       //doc.add(tRectangle);


	        
	        doc.add(tParagraph); //添加段落

	        doc.add(tParagraphs);

	        doc.add(img); //添加img
	        
	        doc.add(tParagraphs1);
	        doc.add(t);

	        doc.close(); //记得关闭document

	    }
	    
	  //同时合并列和行的静态函数
	    public static PdfPCell mergeColAndRow(String str, Font font, int i, int j) {
	        PdfPCell cell = new PdfPCell(new Paragraph(str, font));
	        cell.setMinimumHeight(25);//设置最小高度
	        cell.setHorizontalAlignment(Element.ALIGN_CENTER);//设置水平对齐方式，居中对齐
	        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);//设置垂直对齐方式，居中对齐
	        //将该单元格所在行包括该单元格在内的i列单元格合并为一个单元格
	        cell.setColspan(i);//合拼列
	        cell.setRowspan(j);//跨行数
	        return cell;
	    }


}
