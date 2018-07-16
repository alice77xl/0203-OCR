package com.software.ui;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
 
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.table.DefaultTableModel;

import com.software.server.CreateExcel;
import com.software.server.OCR;
import com.software.server.PreImages;

public class MainFrame extends JFrame implements ActionListener {
	private String[] columns= {"Path","Name","Size(kb)","ModifyDate"};
	private Object[][] cell= {};
	private DefaultTableModel model;
	private JTable table;
	
	private JPanel buttonPanel2;
	private JButton buttonFolder;
	private JButton buttonOCR;
	
	JFileChooser jfc = new JFileChooser();// 文件选择器 
	String path = null;
	Lock tablelock;
	
	public MainFrame()
	{
		setTitle("0203_OCR") ;
		setSize(1800,1200);
		setLayout(new GridBagLayout());
		jfc.setCurrentDirectory(new File("f://"));// 文件选择器的初始目录定为f盘
		
		//表格的初始化开始
		model = new DefaultTableModel(cell,columns)
				{
					public Class<?> getColumnclass(int c)
					{
						return cell.getClass();
					}
					public boolean isCellEditable(int row,int column)
					{
						return false;
					}
				};
				table = new JTable(model);
				//使用windows观感开始
				try
				{
					UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
				}
				catch(Exception e)
				{
					
				}
				//使用windows观感结束
		//表格的初始化结束
			tablelock = new ReentrantLock();
			
		//菜单初始化开始
			buttonPanel2 = new JPanel();
			buttonFolder = new JButton("选择文件夹");
			buttonOCR = new JButton("一键识别");
			buttonPanel2.add(buttonFolder);
			buttonPanel2.add(buttonOCR);
			buttonFolder.addActionListener(this);
			buttonOCR.addActionListener(this);
	        add(buttonPanel2,new GBC(3,0).setFill(GBC.WEST));
	    //菜单结束
				
	        add(new JScrollPane(table),new GBC(0,5,7,7).setWeight(100, 100).setFill(GridBagConstraints.BOTH));
			pack();
			
		
			
	}
	


	public void LoadFolder() {
		//加载文件之前，应该先清空表格
		int rowcount = model.getRowCount();
		while(rowcount!=0)
		{
			model.removeRow(rowcount-1);
			rowcount = model.getRowCount();
		}
		//System.out.println("wenjian");
		
		jfc.setFileSelectionMode(1);// 设定只能选择到文件夹  
        int state = jfc.showOpenDialog(null);// 此句是打开文件选择器界面的触发语句  
        if (state == 1) {  
            return ;  
        } else {  
            File f = jfc.getSelectedFile();// f为选择到的目录  
            path = f.getAbsolutePath();
            File file =new File(path);;
            RealLoadFile(file);
        }  
		
	}
	public void RealLoadFile(File file) throws NullPointerException
	{
		File contents[] = file.listFiles();
		if(contents != null)
		{
			for(File files:contents)
			{
				if(files.isDirectory())//每一个文件夹起一个线程
				{
					try
					{
							Runnable search=()->
							{
								RealLoadFile(files);
							};
							new Thread(search).start();
					}
					catch(NullPointerException e)
					{
						System.out.println("error"+files.getName());
					}	
				}
				else
				{
					String name = files.getName();
					//String tytle=name.substring(name.lastIndexOf("."),name.length());
					String path = files.getParent();
					String size=String.valueOf(files.length()/1024);
					SimpleDateFormat timeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					String time = timeFormat.format(files.lastModified());
					String row[] = {path,name,size,time};
					tablelock.lock();
                    if(name.endsWith("png") || name.endsWith("jpg") || name.endsWith("bmp")){
						
						model.addRow(row);
					}
					tablelock.unlock();
				}
			}
		}
	}
	
	
	public void ImgOCR() {
		String outPath = null;
		try {
			//预处理
			//System.out.println(path);
			PreImages preImage = new PreImages();
			outPath = preImage.PreImage(path);
			//System.out.println(outPath);
		} catch (IOException e) {
			e.printStackTrace();
		}
		//识别
		OCR.convertAllImages(outPath);
		System.out.println("已识别");
		
		//输出到表格
        CreateExcel.createExcel(outPath, OCR.name, OCR.number);	
	}
	
	public void actionPerformed(ActionEvent e) {  
        // TODO Auto-generated method stub  
        if (e.getSource().equals(buttonFolder)) {// 判断触发方法的按钮是哪个  
            LoadFolder();
        }  
        //   
        if (e.getSource().equals(buttonOCR)) {
            ImgOCR();
            // 弹出对话框
            JOptionPane.showMessageDialog(null, "表格存储在图像文件夹下output文件夹中！", "识别完成", 2);  
        }  
    }  
	
 
}