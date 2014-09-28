package com.rg.kafke;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class CommitteeLoader {
	
	private static CommitteeLoader committeeLoader = null;
	public static ArrayList<String> committeeList = new ArrayList<String>();
	public static ArrayList<String> zipCodeList = new ArrayList<String>();
	
	private CommitteeLoader(){}
	
	public static CommitteeLoader getInstance(){
		if(committeeLoader == null){
			committeeLoader = new CommitteeLoader();
			loadResourceFile();
			loadZipCodeDB();
		}
		return committeeLoader;
	}//end of constructor
	
	public static void loadResourceFile(){
		BufferedReader buf = null;
		String path = "";
		 
		try{
			if(isWindows()){
				path = "C://Users//rgupta//workspace//KafkaProducer//resource//committee.txt";
			}
			else
				path = "/home/ubuntu/richa/Simulator/resource/committee.txt";
			buf = new BufferedReader(new FileReader(new File(path)));
			String sb = "";
			while((sb = buf.readLine()) != null){
				committeeList.add(sb);
			}
		} catch(Exception e){
			System.out.println("Error reading file");
			e.printStackTrace();
		}finally{
			try{
				if(buf != null)
					buf.close();
			}catch(IOException e){
				System.out.println("Error closing file");
				e.printStackTrace();
			}
			
		}
	}//end of loadFile
	
	public static void loadZipCodeDB(){
		String path = "";
		BufferedReader buf = null;
		try{
			if(isWindows()){
				path = "C://Users//rgupta//workspace//KafkaProducer//resource//zip_code_database.csv";
			}
			else
				path = "/home/ubuntu/richa/Simulator/resource/zip_code_database.csv";
			buf = new BufferedReader(new FileReader(new File(path)));
			String sb = "";
			while((sb = buf.readLine()) != null){
				zipCodeList.add(sb);
			}
		} catch(Exception e){
			System.out.println("Error reading file");
			e.printStackTrace();
		}finally{
			try{
				if(buf != null)
					buf.close();
			}catch(IOException e){
				System.out.println("Error closing file");
				e.printStackTrace();
			}			
		}		
	}
	
	public static boolean isWindows()
	{
		return (System.getProperty("os.name").startsWith("Windows"));
	}
}
