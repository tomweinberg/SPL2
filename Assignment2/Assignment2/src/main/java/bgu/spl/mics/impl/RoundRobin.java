package bgu.spl.mics.impl;

import java.util.ArrayList;
import java.util.Iterator;

public class RoundRobin<E> {
	private ArrayList<E> theList;
	private ArrayList<E> orderOfInsert;
	private int lastUsed;
	
	public RoundRobin(){
		theList = new ArrayList<E>();
		orderOfInsert = new ArrayList<E>();
		lastUsed= -1;
	}
	
	
	public synchronized void add(E micro){
		if(theList.size() == 0 && orderOfInsert.size()==0){
			theList.add(micro);
			orderOfInsert.add(micro);
		}
		else{
			E lastInserted = orderOfInsert.get(orderOfInsert.size()-1);
			int indexOfLastInsertedInTheList = 0;
			if(lastUsed != -1 && orderOfInsert.get(lastUsed) == micro){
				theList.add(micro);
			}
			else{
				for(int i=0; i<theList.size(); i++){
					E tmp = theList.get(i);
					if(orderOfInsert.indexOf(tmp) == orderOfInsert.indexOf(lastInserted)){
						indexOfLastInsertedInTheList = i;
					}
				}
				theList.add(indexOfLastInsertedInTheList+1, micro);
				orderOfInsert.add(micro);
			}
		}
	}
	
	public synchronized E poll(){
		if(!theList.isEmpty()){
			E ret=theList.remove(0);
			lastUsed = orderOfInsert.indexOf(ret);
			theList.add(ret);
			return ret ;
		}
		else{
			return null;
		}
	}
	
	
	public synchronized void remove(E micro){
		theList.remove(micro);
		orderOfInsert.remove(micro);
	}
	
	public Iterator<E> iterator(){
		return theList.iterator();
		
	}
	
}
