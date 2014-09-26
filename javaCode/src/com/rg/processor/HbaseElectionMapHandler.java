package com.rg.processor;
/**
 * @author Richa Gupta
 * Purpose of the class is to drop, create table
 */
import java.io.IOException;

import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.client.HBaseAdmin;

public class HbaseElectionMapHandler {
	public static void dropAndCreateHTable(){
       @SuppressWarnings("deprecation")
       HBaseConfiguration conf = new HBaseConfiguration();
	   conf.set("hbase.master","localhost:60000");
	 
	   HBaseAdmin hbase = null;
	   try {
		   hbase = new HBaseAdmin(conf);
		   if (hbase.tableExists("map_latlong")) {
			   System.out.println("map_latlong table exists");
			   hbase.disableTable("map_latlong");
			   hbase.deleteTable("map_latlong");
			   HTableDescriptor desc = new HTableDescriptor("map_latlong");
			   HColumnDescriptor cf1 = new HColumnDescriptor("cf1".getBytes());
			   desc.addFamily(cf1);
			   hbase.createTable(desc);
		   } 		  
	   } catch (IOException e) {
			e.printStackTrace();
	   } catch(Exception e){
		   e.printStackTrace();
	   } finally{
		   try{
			   if(hbase != null)
				hbase.close();  
		   } catch(IOException e){
			   e.printStackTrace();
		   }
		
	   }
    }// drop table
	
	public static void main(String[] args){
		dropAndCreateHTable();
	}

}
