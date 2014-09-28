package com.rg.kafka.handler;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.MasterNotRunningException;
import org.apache.hadoop.hbase.ZooKeeperConnectionException;
import org.apache.hadoop.hbase.client.HBaseAdmin;

import com.rg.kafka.handler.CommitteeLoader;

public class CommitteeLoader {
	private static CommitteeLoader committeeLoader = null;
	public static HashMap<String, String> committeeList = new HashMap<String, String>();
	
	private CommitteeLoader(){
		
	}
	
	public static CommitteeLoader getInstance(){
		if(committeeLoader == null){
			committeeLoader = new CommitteeLoader();
			loadResourceFile();
			createHTable();
		}
		return committeeLoader;
	}
	
	public static void loadResourceFile(){
		BufferedReader buf = null;
		 
		try{
			buf = new BufferedReader(new FileReader(new File("resource"+File.separator+"committee_party.txt")));
			String sb = "";
			while((sb = buf.readLine()) != null){
				String[] line = sb.split(";");
				committeeList.put(line[0], line[2]+ " "+line[1]);
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
	
   public static void createHTable(){
       @SuppressWarnings("deprecation")
       HBaseConfiguration conf = new HBaseConfiguration();
	   conf.addResource("resource\\hbase-site.xml");
	   conf.set("hbase.master","localhost:60000");
	 
	   HBaseAdmin hbase;
	   try {
		   hbase = new HBaseAdmin(conf);
		   if (hbase.tableExists("committee_daily")) {
			   System.out.println("committee_daily table already exists");
		   } else{
			   HTableDescriptor desc = new HTableDescriptor("committee_daily");
			   HColumnDescriptor cf1 = new HColumnDescriptor("cf1".getBytes());
			   desc.addFamily(cf1);
			   hbase.createTable(desc);
		   }
		  
	   } catch (Exception e) {
			e.printStackTrace();
	   }    
    }    
	    

}
