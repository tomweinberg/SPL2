package bgu.spl.app.passive;

import bgu.spl.mics.Request;

public class ManufacturingOrderRequest<R> implements Request<R> {
	private Receipt receipt;
	private String shoeType;
	private int amount;
	private int requestTick;
	  /**
     * build new  Manufacturing Order Request 
     * @param shoeType the shoe type 
     * @param amount the amount of shoe in discount 
     * @param tick the tick ask the request
     */
	public ManufacturingOrderRequest(String shoeType, int amount, int tick) {
		this.shoeType = shoeType;
		this.receipt = null;
		this.amount=amount;
		this.requestTick= tick;
	}
	 /**
     * @return  The request Tick
     */

	public int getRequestTick() {
		return requestTick;
	}
	 /**
     * @return  The amount
     */
	public int getAmount() {
		return amount;
	}
	 /**
     * @return  The receipt of the Manufacturing Order Request 
     */
	public Receipt getReceipt() {
		return receipt;
	}
	 /**
     * set  The receipt
     */
	public void setReceipt(Receipt receipt) {
		this.receipt = receipt;
	}
	 /**
     * @return  The shoeType
     */
	public String getShoeType() {
		return shoeType;
	}
	
	
	
}
