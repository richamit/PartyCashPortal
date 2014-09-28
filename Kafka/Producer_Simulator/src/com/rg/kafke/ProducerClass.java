package com.rg.kafke;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.Random;

import kafka.javaapi.producer.Producer;
import kafka.producer.KeyedMessage;
import kafka.producer.ProducerConfig;


public class ProducerClass {
	
	public static void main(String[] args){
		Properties props = new Properties();
		Random rnd = new Random();
		props.put("metadata.broker.list", "localhost:9092");
		props.put("serializer.class", "kafka.serializer.StringEncoder");
		//props.put("partitioner.class", "example.producer.SimplePartitioner");
		props.put("request.required.acks", "1");
		 
     	ProducerConfig config = new ProducerConfig(props);
		
     	Producer<String, String> producer = new Producer<String, String>(config);

     	for (long nEvents = 0; nEvents < 100; nEvents++) {
     		long runtime = new Date().getTime(); 
     		String ip = "192.168.2." + rnd.nextInt(255);
     		String msg = runtime + ",www.example.com," + ip;
     		KeyedMessage<String, String> data = new KeyedMessage<String, String>("test", ip, msg);
     		producer.send(data);
     	}	
     	producer.close();
	}

}
