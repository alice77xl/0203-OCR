package com.software.server;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;  
  
public class OutPut {  
      
    /** 
     * 输出Excel文件，输出格式为多行两列 
     * @param  
     */  
    public static void createExcel(String dirPath, ArrayList<String> name, ArrayList<String> number) {  
    	FileOutputStream fos;
		try {
			fos = new FileOutputStream(dirPath+"\\识别结果.xls");	
    	    @SuppressWarnings("resource")
			Workbook wb = new HSSFWorkbook();
    	    Sheet sheet = wb.createSheet("识别结果");
    	
    	// 使用 Sheet 接口创建一行
  	    Row row = sheet.createRow(0);
  	    // 使用重载的方法为单元格设置值
  	    Cell c1 = row.createCell(0);
  	    c1.setCellValue("企业名称");
  	    Cell c2 = row.createCell(1);
  	    c2.setCellValue("企业注册号");
  	    
		for(int j = 0; j < name.size(); j++) {
    		  // 使用 Sheet创建一行
			  Row rowoutput = sheet.createRow(j+1);
		      rowoutput.createCell(0).setCellValue(name.get(j));
		      rowoutput.createCell(1).setCellValue(number.get(j));
    	}
		wb.write(fos); 
    	fos.close();
    	
		} catch (IOException e) {
			e.printStackTrace();
		}

    }  
}  