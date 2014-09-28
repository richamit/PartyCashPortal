package com.rg.kafke;

import com.rg.kafke.handler.SimulatorThread;

public class TestSimulation {
	public static void main(String[] args){
		CommitteeLoader.getInstance();
		(new Thread(new SimulatorThread())).start();
		//new SimulatorThread().setFlag();
	}

}
