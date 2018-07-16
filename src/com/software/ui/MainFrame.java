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
	
	JFileChooser jfc = new JFileChooser();// �ļ�ѡ���� 
	String path = null;
	Lock tablelock;
	
	public MainFrame()
	{
		setTitle("0203_OCR") ;
		setSize(1800,1200);
		setLayout(new GridBagLayout());
		jfc.setCurrentDirectory(new File("f://"));// �ļ�ѡ�����ĳ�ʼĿ¼��Ϊf��
		
		//���ĳ�ʼ����ʼ
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
				//ʹ��windows�۸п�ʼ
				try
				{
					UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
				}
				catch(Exception e)
				{
					
				}
				//ʹ��windows�۸н���
		//���ĳ�ʼ������
			tablelock = new ReentrantLock();
			
		//�˵���ʼ����ʼ
			buttonPanel2 = new JPanel();
			buttonFolder = new JButton("ѡ���ļ���");
			buttonOCR = new JButton("һ��ʶ��");
			buttonPanel2.add(buttonFolder);
			buttonPanel2.add(buttonOCR);
			buttonFolder.addActionListener(this);
			buttonOCR.addActionListener(this);
	        add(buttonPanel2,new GBC(3,0).setFill(GBC.WEST));
	    //�˵�����
				
	        add(new JScrollPane(table),new GBC(0,5,7,7).setWeight(100, 100).setFill(GridBagConstraints.BOTH));
			pack();
			
		
			
	}
	


	public void LoadFolder() {
		//�����ļ�֮ǰ��Ӧ������ձ��
		int rowcount = model.getRowCount();
		while(rowcount!=0)
		{
			model.removeRow(rowcount-1);
			rowcount = model.getRowCount();
		}
		//System.out.println("wenjian");
		
		jfc.setFileSelectionMode(1);// �趨ֻ��ѡ���ļ���  
        int state = jfc.showOpenDialog(null);// �˾��Ǵ��ļ�ѡ��������Ĵ������  
        if (state == 1) {  
            return ;  
        } else {  
            File f = jfc.getSelectedFile();// fΪѡ�񵽵�Ŀ¼  
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
				if(files.isDirectory())//ÿһ���ļ�����һ���߳�
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
			//Ԥ����
			//System.out.println(path);
			PreImages preImage = new PreImages();
			outPath = preImage.PreImage(path);
			//System.out.println(outPath);
		} catch (IOException e) {
			e.printStackTrace();
		}
		//ʶ��
		OCR.convertAllImages(outPath);
		System.out.println("��ʶ��");
		
		//��������
        CreateExcel.createExcel(outPath, OCR.name, OCR.number);	
	}
	
	public void actionPerformed(ActionEvent e) {  
        // TODO Auto-generated method stub  
        if (e.getSource().equals(buttonFolder)) {// �жϴ��������İ�ť���ĸ�  
            LoadFolder();
        }  
        //   
        if (e.getSource().equals(buttonOCR)) {
            ImgOCR();
            // �����Ի���
            JOptionPane.showMessageDialog(null, "���洢��ͼ���ļ�����output�ļ����У�", "ʶ�����", 2);  
        }  
    }  
	
 
}