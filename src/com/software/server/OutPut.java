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
     * ���Excel�ļ��������ʽΪ�������� 
     * @param  
     */  
    public static void createExcel(String dirPath, ArrayList<String> name, ArrayList<String> number) {  
    	FileOutputStream fos;
		try {
			fos = new FileOutputStream(dirPath+"\\ʶ����.xls");	
    	    @SuppressWarnings("resource")
			Workbook wb = new HSSFWorkbook();
    	    Sheet sheet = wb.createSheet("ʶ����");
    	
    	// ʹ�� Sheet �ӿڴ���һ��
  	    Row row = sheet.createRow(0);
  	    // ʹ�����صķ���Ϊ��Ԫ������ֵ
  	    Cell c1 = row.createCell(0);
  	    c1.setCellValue("��ҵ����");
  	    Cell c2 = row.createCell(1);
  	    c2.setCellValue("��ҵע���");
  	    
		for(int j = 0; j < name.size(); j++) {
    		  // ʹ�� Sheet����һ��
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