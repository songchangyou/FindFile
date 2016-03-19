package com.itqimeng.findFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.itqimeng.findFile.helper.StringHelper;

public class Entry {

	/**
	 * 把指定日期内修改过的文件，拷贝到另一个目录
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		//要查找的文件路径
		String inputPath = "D:/zdfl/svn/jeesite/trunk/";
		String outputPath = "D:/tmp/findFile";
		String startDateStr = "2016-03-17";
		String endDateStr = "";
		String datePattern = "yyyy-MM-dd";
		SimpleDateFormat sdf = new SimpleDateFormat(datePattern);
		File inputDirectory = new File(inputPath);
		Date startDate = null;
		Date endDate = null;
		if(StringHelper.isNotBlank(startDateStr)){
			startDate = sdf.parse(startDateStr);
		}
		if(StringHelper.isNotBlank(endDateStr)){
			endDate = sdf.parse(endDateStr);
		}
		System.out.println("开始查找……");
		if(inputDirectory.exists()){
			findAndCopyFile(inputDirectory,inputPath,outputPath,startDate,endDate);
			System.out.println("查找结束，文件在："+outputPath);
		}else{
			System.out.println("要查找的文件路径不存在");
		}
	}
	
	/**
	 * 遍历文件，
	 * 	如果文件的修改日期在开始与结束日期之间，拷贝到指定目录下，与源文件相同的目录
	 * @param inputFile
	 * @param outputPath
	 * @param startDate
	 * @param enddate
	 */
	private static void findAndCopyFile(File inputFile,String inputPath,String outputPath,Date startDate,Date endDate){
		if(inputFile.exists()){
			if(inputFile.isDirectory()){//目录，遍历其下的子目录
				File[] children = inputFile.listFiles();
				for(int index=0;index<children.length;index++){
					File child = children[index];
					findAndCopyFile(child,inputPath,outputPath,startDate,endDate);
				}
			}else{//文件则获取修改日期，与开始结束日期做比较
				long modifiedTime = inputFile.lastModified();
				boolean ifCopy = false;
				if(startDate != null){
					long start = startDate.getTime();
					ifCopy = modifiedTime >= start;
				}
				if(ifCopy && endDate != null){
					long end = startDate.getTime();
					ifCopy = modifiedTime <= end;
				}
				if(ifCopy){
					//拼接输出文件路径，输出目录字符串（去掉结尾的'/'）+'/'+文件路径(去掉输入路径后再去掉开始的'/')
					String outFileDirectoryPaht = outputPath;
					if(outFileDirectoryPaht.endsWith("/")){
						outFileDirectoryPaht = outFileDirectoryPaht.substring(1);
					}
					String outFilePath = inputFile.getAbsolutePath().substring(inputPath.length());
					if(outFilePath.startsWith("/")){
						outFilePath = outFilePath.substring(1);
					}
					String outFileFullPath = outFileDirectoryPaht + "/" + outFilePath;
					copyFile(inputFile,outFileFullPath);
				}
			}
		}
	}
	
	/**  
     * 复制单个文件  
     *   
     * @param oldFile  
     *            String 原文件路径 如：c:/fqf.txt  
     * @param newFilePath  
     *            String 复制后路径 如：f:/fqf.txt  
     * @return boolean  
     */ 
    private static void copyFile(File oldFile, String newFilePath) {  
        try {  
            if (oldFile.exists()) { // 文件存在时  
            	//建新文件的文件夹
            	File newFile = new File(newFilePath);
            	newFile.getParentFile().mkdirs();
            	int bytesum = 0;  
            	int byteread = 0;
                InputStream inStream = new FileInputStream(oldFile); // 读入原文件  
                FileOutputStream fs = new FileOutputStream(newFilePath);  
                byte[] buffer = new byte[1024];  
                while ((byteread = inStream.read(buffer)) != -1) {  
                    bytesum += byteread; // 字节数 文件大小  
                    fs.write(buffer, 0, byteread);  
                }  
                inStream.close(); 
                fs.flush();
                fs.close();
            }  
        } catch (Exception e) {  
            System.out.println("复制单个文件操作出错 ");  
            e.printStackTrace();  
 
        }  
 
    }  

}
