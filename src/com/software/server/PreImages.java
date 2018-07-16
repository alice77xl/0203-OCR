package com.software.server;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

public class PreImages {
	
	    //ͼƬ��ֵ����dirPathΪ������ϢͼƬ��ŵ�·��
		public String PreImage (String dirPath) throws IOException {

			//���Ԥ����ͼƬ��·��
	        String outPath = dirPath + "\\output\\";
	        
	        File file = new File(dirPath);
	        File outFile = new File(outPath);
	        if (!outFile.exists()) {
	        	//�½�output�ļ���
	            outFile.mkdir();
	        }

	        //��ֵ�����ͼƬ
	        BufferedImage binaryImage_name = null;
	        BufferedImage binaryImage_number = null;
	        //δ��ֵ����ͼƬ
	        BufferedImage unBinaryImage = null;

	        //������ͼƬ
	        File[] files = file.listFiles();
	        for (File f : files) {
	            if (!f.isDirectory()) {
	            	String name = f.getName();
	                if (name.endsWith("png") || name.endsWith("jpg") || name.endsWith("bmp")) {
	                
	                	ArrayList<Integer> space = new ArrayList<Integer>();
		    	        ArrayList<Integer> space_array = null;
		            	//��ͼƬ
		                unBinaryImage = ImageIO.read(f);
		                int max = new Color(255, 255, 255).getRGB();
		                int min = new Color(0, 0, 0).getRGB();

		                //ͼƬ�ü�����ֵ��
		                binaryImage_name = new BufferedImage(600, 38, BufferedImage.TYPE_BYTE_BINARY);
		                binaryImage_number = new BufferedImage(600, 38, BufferedImage.TYPE_BYTE_BINARY);
		                
		                //ͼƬ�ҶȻ�
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
		                
		                //ͼƬ�ҶȻ�
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
		   	            		  //System.out.println("ƥ��");
		   	 	            	 for( int y = 5 ; y < 40 ; y++) {
		   	 	 	            	binaryImage_name.setRGB((x+130), y - 5, max);
		   		            	 }
		   	 	            	split_index++;
		   	            	  }

		   	             }

	         	                
		                //��ȡͼƬ��
		                String fileName = f.getName();
		                String fileName_number = "number"+fileName;
		                String fileName_name = "name"+fileName;
		                String preImage = fileName.substring(fileName.indexOf(".") + 1);
		      
		                //����Ԥ������ͼƬ����ȡĿ¼��output�ļ�����
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
		    		//System.out.println("��ʼ����Сֵ :"+min_number );
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
//		    				System.out.println("С��");
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

		
		//�ҶȻ�
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
