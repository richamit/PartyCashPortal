package com.rg.kafke.handler;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Properties;
import java.util.Random;

import com.rg.kafke.CommitteeLoader;

import kafka.javaapi.producer.Producer;
import kafka.producer.KeyedMessage;
import kafka.producer.ProducerConfig;

public class SimulateContribution implements Runnable {
	public static final String DELIMITER = ";";
	public static final StringBuilder PART2 = new StringBuilder(";N;M4;;13961944423;15;IND;SIMMONS, ROBERT R.;");
	public static final StringBuilder PART4 = new StringBuilder(";;AB4CDE18AD0CB44D8843;868172;;;4042220131188215950");
	public boolean flag = true;
	public static Random random = new Random();
	public static int count = 0;
	public SimulateContribution(){		 
	}
	
	public static void main(String[] args){
		CommitteeLoader.getInstance();
		(new Thread(new SimulateContribution())).start();
		//new SimulateContribution().setFlag();
	}
	
	@Override
	public void run() {
		Properties props = new Properties();
		props.put("metadata.broker.list", "localhost:9092");
		props.put("serializer.class", "kafka.serializer.StringEncoder");
		props.put("request.required.acks", "1");
		 
     	ProducerConfig config = new ProducerConfig(props);
		
     	Producer<String, String> producer = new Producer<String, String>(config);
		ArrayList<String> comm = CommitteeLoader.committeeList;
		ArrayList<String> zipList = CommitteeLoader.zipCodeList;
		int size = comm.size();
		int zipSize = zipList.size();
		String zipInfo = zipList.get(randomNumberGenerator(zipSize));	
		int zipCount = 0;
		SimpleDateFormat sdf = new SimpleDateFormat("MMddyyyy");
		try{
			while(count < 200){
				StringBuilder st = new StringBuilder();
				int index = randomNumberGenerator(size);
				String committee = comm.get(index);
				if (zipCount > 10){
					zipCount = 0;
					zipInfo = zipList.get(randomNumberGenerator(zipSize));
				}
				
				zipCount++;
				st.append(committee).append(PART2).append(zipInfo).append(";;;").append(sdf.format(new Date()))
				.append(DELIMITER).append(randomNumberGenerator(100)).append(PART4);
				KeyedMessage<String, String> data = new KeyedMessage<String, String>("contribution", st.toString());
	     		producer.send(data);
				System.out.println(st.toString());
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				count++;
			}
		} catch(Exception e){
			e.printStackTrace();
		} finally{
			producer.close();
		}		 
	}
	
	public int randomNumberGenerator(int size){
		int num = 0;
		num = random.nextInt(size);
		return num;
	}
	
	public void setFlag(){
		flag = false;
	}



}
