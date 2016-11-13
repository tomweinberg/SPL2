package bgu.spl.app.passive;

import bgu.spl.mics.Broadcast;

public class NewDiscountBroadcast implements Broadcast {
	private String shoeType;
	private int amount;
	  /**
     * build new discount broadcast 
     * @param shoeType the shoe type 
     * @param amount the amount of shoe in discount  
     */
	
	public NewDiscountBroadcast(String shoeType, int amount) {
		this.shoeType = shoeType;
		this.amount = amount;
	}
	 /**
     * @return  The shoe-Type
     */

	public String getShoeType() {
		return shoeType;
	}
	 /**
     * @return  The amount
     */

	public int getAmount() {
		return amount;
	}
	
	
}
