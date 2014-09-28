package com.rg.kafke.handler;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

import com.rg.kafke.CommitteeLoader;


public class SimulatorThread implements Runnable{
	public static final String DELIMITER = "|";
	public static final StringBuilder PART2 = new StringBuilder("|N|M4||13961944423|15|IND|SIMMONS, ROBERT R.|");
	public static final StringBuilder PART4 = new StringBuilder("||AB4CDE18AD0CB44D8843|868172|||4042220131188215950");
	public boolean flag = true;
	public static int count = 0;
	public SimulatorThread(){
		CommitteeLoader.getInstance();
	}

	@Override
	public void run() {
		ArrayList<String> comm = CommitteeLoader.committeeList;
		ArrayList<String> zipList = CommitteeLoader.zipCodeList;
		int size = comm.size();
		int zipSize = zipList.size();
		SimpleDateFormat sdf = new SimpleDateFormat("MMddyyyy");
		String zipInfo = zipList.get(randomNumberGenerator(zipSize));	
		int zipCount = 0;
		while(count < 1000){
			StringBuilder st = new StringBuilder();
			int index = randomNumberGenerator(size);
			String committee = comm.get(index);			
			if (zipCount > 100){
				zipCount = 0;
				zipInfo = zipList.get(randomNumberGenerator(zipSize));
			}
			
			zipCount++;
			
			st.append(committee).append(PART2).append(zipInfo).append("|||").append(sdf.format(new Date()))
			.append(DELIMITER).append(randomNumberGenerator(100)).append(PART4);
			System.out.println(st.toString());
			try {
				Thread.sleep(30 * 1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			count++;
		}
	}
	
	public int randomNumberGenerator(int size){
		Random random = new Random();
		int amt = 0;
		//while(amt < 100){
			amt = random.nextInt(size);
		//}
		return amt;
	}
	
	public void setFlag(){
		flag = false;
	}
}
