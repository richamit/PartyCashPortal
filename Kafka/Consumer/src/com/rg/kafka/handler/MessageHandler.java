package com.rg.kafka.handler;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;

public class MessageHandler {
	private static MessageHandler messageHandler = null;
	PrintWriter br;
	public static int MESSAGE_COUNT = 5000;
	int count = 0;
	String fileName;
	String closeFileName;
	private static long startTime = 0;
	private static long endTime = 0;
	
	private MessageHandler(){
		
	}
	
	public static  MessageHandler getInstance(){
		if(messageHandler == null)
			messageHandler = new MessageHandler();
		return messageHandler;
	}
	public void addMessage(String message){
		if(br == null)
			initFile();
		br.println(message.replaceAll(";", "|"));
		count++;
		if(count >= MESSAGE_COUNT || new Date().getTime() >= endTime)
			flush();
	}//end of addMessage
	
	public void initFile(){
		count = 0;
		Date d = new Date();
		fileName = String.valueOf(d.getTime()) + ".pre";
		closeFileName = String.valueOf(d.getTime()) + ".out";
		startTime = d.getTime();
		endTime = startTime + (3 * 60 * 1000);
		try {
			br  = new PrintWriter(new File(fileName), "UTF-8");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}//end of initFile
	
	public void flush(){
		if(br != null){
			br.flush();
			br.close();
			br = null;
		}
		File f = new File(fileName);
		f.renameTo(new File(closeFileName));
		initFile();
	}//end of flush

}
