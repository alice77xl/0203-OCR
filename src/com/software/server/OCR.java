package com.software.server;

import javax.imageio.ImageIO;
import net.sourceforge.tess4j.ITessAPI.TessPageSegMode;
import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by CJ on 2018/6/1.
 */
public class OCR {
	
	public static ArrayList<String> name = new ArrayList<String>();
	public static ArrayList<String> number = new ArrayList<String>();
	private static List<File> fileList = new ArrayList<File>();
	
    /**
     * OCR识别
     * @param srcImage 图片路径
     * @return 识别结果
     */
	public static String FindOCR(String srcImage) {
        try {
            System.out.println("time:");
            double start=System.currentTimeMillis();
            File imageFile = new File(srcImage);
            //输出图片名
            //System.out.println(imageFile.getName());
            if (!imageFile.exists()) {
                return "图片不存在";
            }
            BufferedImage textImage = ImageIO.read(imageFile);
            //Tesseract instance=Tesseract.getInstance();
            ITesseract instance = new Tesseract();
            instance.setDatapath("tesseract");//设置训练库
            instance.setLanguage("chi_sim+eng");//中文识别
            instance.setPageSegMode(TessPageSegMode.PSM_SINGLE_LINE);//设置识别模式
            String result = null;
            result = instance.doOCR(textImage);
            double end=System.currentTimeMillis();
            System.out.println("耗时"+(end-start)/1000+" s");
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return "发生未知错误";
        }
    }
	
	
	/**
	 * 读取文件夹所有图片
	 * @param dir 文件夹路径
     * @return 
	 */
	public static void convertAllImages(String dir) {
        File dirFile = new File(dir);
        dir = dirFile.getAbsolutePath();
        loadImages(new File(dir));
        int count=0;
        for (File file : fileList) {
            String filePath = file.getAbsolutePath(); 
            String str = FindOCR(filePath);
            if(count < fileList.size()/2)
            {	
            	//System.out.println(str.substring(str.indexOf(":")+2));
            	name.add(str.substring(str.indexOf(":")+2));
            	count++;
            }
            else
            {
            	//System.out.println(str.substring(str.indexOf(":")+2));
            	number.add(str.substring(str.indexOf(":")+2));
            }
            //System.out.println(FindOCR(filePath));
        }
    }
	  
		
	public static void loadImages(File f) {
        if (f != null) {
            if (f.isDirectory()) {
                File[] fileArray = f.listFiles();
                if (fileArray != null) {
                    for (int i = 0; i < fileArray.length; i++) {
                        //递归调用
                        loadImages(fileArray[i]);
                    }
                }
            } else {
                String name = f.getName();
                if (name.endsWith("png") || name.endsWith("jpg") || name.endsWith("bmp")) {
                    fileList.add(f);
                }
            }
        }
    }
	
}