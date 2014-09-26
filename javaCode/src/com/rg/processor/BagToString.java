package com.rg.processor;

import java.io.IOException;
import java.util.Iterator;

import org.apache.pig.EvalFunc;
import org.apache.pig.data.DataBag;
import org.apache.pig.data.Tuple;

@SuppressWarnings("deprecation")
public class BagToString extends EvalFunc<String> {

	@Override
	public String exec(Tuple input) throws IOException {
		StringBuilder sb = null;
		DataBag bag = null;
		if ((input == null) || (input.size() == 0))
			return null;
		try {
			bag = (DataBag) input.get(0); 
			Iterator<Tuple> bagIT = bag.iterator();
	        String delimiter = ";";

	        sb = new StringBuilder();
	        while(bagIT.hasNext()){
	            Tuple tupleInBag = bagIT.next();
	            sb.append(tupleInBag.toDelimitedString(delimiter)).append(delimiter);
	        }		        
		} catch (Exception e) {
			throw new IOException(e);
		}		
		return sb.toString();
	}
	
	 
	

}
