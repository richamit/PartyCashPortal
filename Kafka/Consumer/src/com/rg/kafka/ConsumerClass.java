package com.rg.kafka;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.KeyValue;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.util.Bytes;

import com.rg.kafka.handler.CommitteeLoader;
import com.rg.kafka.handler.MessageHandler;

import kafka.consumer.Consumer;
import kafka.consumer.ConsumerConfig;
import kafka.consumer.ConsumerIterator;
import kafka.consumer.KafkaStream;
import kafka.javaapi.consumer.ConsumerConnector;


public class ConsumerClass implements Runnable{

	public static void main(String[] args) {   
		CommitteeLoader.getInstance();
		new Thread(new ConsumerClass()).start();
	}

	@Override
	public void run() {
		Properties props = new Properties();
	    props.put("zookeeper.connect", "localhost:2181");
	    props.put("group.id", "mygroupid2");
	    props.put("zookeeper.session.timeout.ms", "413");
	    props.put("zookeeper.sync.time.ms", "203");
	    props.put("auto.commit.interval.ms", "1000"); 
	    ConsumerConfig cf = new ConsumerConfig(props) ;
	    ConsumerConnector consumer = Consumer.createJavaConsumerConnector(cf) ;
	    String topic = "contribution"; 
	    Map<String, Integer> topicCountMap = null;
	    HashMap<String, String> committeeList = CommitteeLoader.committeeList;
	    HTable htable = null;
	    Configuration config = HBaseConfiguration.create();
	    try{
	    	 htable = new HTable(config, "committee_daily");
		     while(true){
		    	 topicCountMap = new HashMap<String, Integer>();
		    	 topicCountMap.put(topic, new Integer(1));
		         Map<String,List<KafkaStream<byte[],byte[]>>> consumerMap =
		                 consumer.createMessageStreams(topicCountMap);
		         List<KafkaStream<byte[],byte[]>> streams = consumerMap.get(topic);
		        
		         for(KafkaStream<byte[],byte[]> stream  : streams) {
		        	 ConsumerIterator<byte[], byte[]> it = stream.iterator();
		        	 while (it.hasNext()) {
		             	String value = new String(it.next().message());
		             	System.out.println("message is -----------------------------"+value);
		             	MessageHandler.getInstance().addMessage(value);
		            	String[] split = value.split(";");
		            	StringBuilder key = new StringBuilder(committeeList.get(split[0]));
		            	key.append(" ").append(split[13]).append(" ").append(split[9]);
		            	 		            	
		            	int amount = Integer.parseInt(split[14]);
		            	try{		            		 
		            		Get get = new Get(Bytes.toBytes(key.toString()));
		            		Result rs = htable.get(get);
		            		Put put = new Put(Bytes.toBytes(key.toString()));
		            		int oldAmt = 0;
		            		if(rs != null){
		            			for(KeyValue kv : rs.raw()){
		            				oldAmt = Integer.parseInt(Bytes.toString(kv.getValue()));
		            	        }
			            		put.add(Bytes.toBytes("cf1"), Bytes.toBytes("val"), Bytes.toBytes(String.valueOf(amount + oldAmt)));
		            		}		            		 
		            		else
		            			put.add(Bytes.toBytes("cf1"), Bytes.toBytes("val"), Bytes.toBytes(String.valueOf(amount)));
		                    htable.put(put);	           		
		            	}catch(Exception e){
		            		e.printStackTrace();
		            	}		            	 
		             }//while loop
		         }//for loop
		     }
	    } catch(IOException io){
	    	io.printStackTrace();
	    } finally{	    	
			try {
				if(htable != null)
					htable.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
	    }
	   
	}

}


