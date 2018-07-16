package com.software.server;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

public class PreImages {
	
	    //图片二值化，dirPath为工商信息图片存放的路径
		public String PreImage (String dirPath) throws IOException {

			//输出预处理图片的路径
	        String outPath = dirPath + "\\output\\";
	        
	        File file = new File(dirPath);
	        File outFile = new File(outPath);
	        if (!outFile.exists()) {
	        	//新建output文件夹
	            outFile.mkdir();
	        }

	        //二值化后的图片
	        BufferedImage binaryImage_name = null;
	        BufferedImage binaryImage_number = null;
	        //未二值化的图片
	        BufferedImage unBinaryImage = null;

	        //批处理图片
	        File[] files = file.listFiles();
	        for (File f : files) {
	            if (!f.isDirectory()) {
	            	String name = f.getName();
	                if (name.endsWith("png") || name.endsWith("jpg") || name.endsWith("bmp")) {
	                
	                	ArrayList<Integer> space = new ArrayList<Integer>();
		    	        ArrayList<Integer> space_array = null;
		            	//读图片
		                unBinaryImage = ImageIO.read(f);
		                int max = new Color(255, 255, 255).getRGB();
		                int min = new Color(0, 0, 0).getRGB();

		                //图片裁剪、二值化
		                binaryImage_name = new BufferedImage(600, 38, BufferedImage.TYPE_BYTE_BINARY);
		                binaryImage_number = new BufferedImage(600, 38, BufferedImage.TYPE_BYTE_BINARY);
		                
		                //图片灰度化
		                for (int x = 130 ; x < 550; x++) {
		                	int count = 0;
		                    for (int y = 3; y < 35; y++) {
		                        int RBG_name = getGray(unBinaryImage.getRGB(x, y+40));    
		                        if (RBG_name == 0) {
		                        	 //System.out.print("0");
		                        } else if (RBG_name == 229) {
//		                        	count++;
		                        	 //System.out.print("0");
		                        } else {
		                        	count++;
		                        	 //System.out.print("1");
		                        }
		                    }
		                    //System.out.print(" "+(x-130)+" "+count);
		                   // System.out.println();
		                    space.add(count);
		                }
		               space_array =  change(space);
		                
		                //图片灰度化
		                    for (int x = 0; x < 600; x++) {
		                    	 for (int y = 5 ; y < 43 ; y++) {
		                        int RBG_number = getGray(unBinaryImage.getRGB(x, y));
		                        int RBG_name = getGray(unBinaryImage.getRGB(x, y+40));
		                        
		                        if (RBG_number == 0) {
		                        	binaryImage_number.setRGB(x, y - 5, max);         
		                        } else if (RBG_number == 229) {
		                        	binaryImage_number.setRGB(x, y - 5, max);
		                        } else {
		                        	binaryImage_number.setRGB(x, y - 5, min);
		                        }
		                        
		                        if (RBG_name == 0) {
		                        	binaryImage_name.setRGB(x, y - 5, max);         
		                        } else if (RBG_name == 229) {
		                        	binaryImage_name.setRGB(x, y - 5, max);
		                        } else {
		                        	binaryImage_name.setRGB(x, y - 5, min);
		                        }
		                        
		                    }
		                }
		                    
		                 //System.out.println();
		                 int split_index = 0;
		   	             for(int x = 0 ; x < 600 ; x++) {
		   	            	  if(x==space_array.get(split_index)&& split_index < space_array.size()-1) {
		   	            		  //System.out.println("匹配");
		   	 	            	 for( int y = 5 ; y < 40 ; y++) {
		   	 	 	            	binaryImage_name.setRGB((x+130), y - 5, max);
		   		            	 }
		   	 	            	split_index++;
		   	            	  }

		   	             }

	         	                
		                //获取图片名
		                String fileName = f.getName();
		                String fileName_number = "number"+fileName;
		                String fileName_name = "name"+fileName;
		                String preImage = fileName.substring(fileName.indexOf(".") + 1);
		      
		                //生成预处理后的图片到读取目录的output文件夹下
		                ImageIO.write(binaryImage_number, preImage, new File(outPath + fileName_number));
		                ImageIO.write(binaryImage_name, preImage, new File(outPath + fileName_name));
	                }
	    	        
	                
	            }
	        }
	        return outPath;
	    }

		 public static ArrayList<Integer> change(ArrayList<Integer> space) {
		    	ArrayList<Integer> change = new ArrayList<Integer>();
		    	int l =space.size();
		    	//System.out.println(l);
		    	int index = 24;
		    	
		    	boolean isChange ;
		    	
		    	while(index <= l) {
		    		isChange = false;
			    	int min_number = 0;
			    	int min_index = 0;
		    		int i ;
		    		min_number = space.get(index);
		    		//System.out.println();
		    		//System.out.println("初始化最小值 :"+min_number );
		    		int item = 0 ;
		    		for(i=0 ; i < 12 ; i++) {
		    			item = index + i ;
		    			//System.out.println("item:"+item +" min_number:"+min_number);
		    			if(space.get(item)==0) {
		    				min_index = item;
		    				min_number = 0 ;
		    				isChange = true;
		    				break;
		    			}else if(space.get(item)<min_number) {
//		    				System.out.println("小于");
		    				min_index = item;
		    				min_number = space.get(item);
		    				isChange = true;
		    			}
		    		}
		    		if(isChange==false) {
		    				min_index = index;
		    				min_number = space.get(index);
		    			
		    		}
		    		change.add(min_index);
		    		index = min_index + 24 ;
		    		//System.out.print("min_index:"+min_index+" min_number:"+min_number+" index:"+index);
		    	}
				return change;

		    
		    }

		
		//灰度化
	    public static int getGray(int rgb) {

	        int red, blue, green;
	        Color c = new Color(rgb);
	        red = c.getRed();
	        green = c.getGreen();
	        blue = c.getBlue();

	        int top = (red + green + blue) / 3;
	        return top;

	    }
}
