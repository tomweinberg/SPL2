package bgu.spl.app.passive;

import bgu.spl.mics.Request;

public class PurchaseOrderRequest<R> implements Request<R> {
	private Receipt receipt;
	private String shoeType;
	private boolean discount;
	private String customer;
	private int requestTick;
	private int amountSold;
	  /**
     * build Request to do Purchase Order
     * @param shoeType the shoe type
     * @param discount if the customer  want the shoe in discount
     * @param customer  the name of the customer 
     * @param tick the tick of the request
     * @param amount the amount of the shoe
     *
    
     *     
     */
	
	public PurchaseOrderRequest(String shoeType, boolean discount, String customer , int tick, int amount) {
		this.shoeType = shoeType;
		this.receipt= null;
		this.discount = discount;
		this.customer = customer;
		requestTick = tick;
		amountSold = amount;
	}

	 /**
     * @return  The receipt of the sell.
     */
	public Receipt getReceipt() {
		return receipt;
	}

	 /**
     * set   The receipt of the sell.
     */
	public void setReceipt(Receipt receipt) {
		this.receipt = receipt;
	}
	 /**
     * @return  true if the customer want the shoe in discount
     */
	public boolean getDiscount() {
		return discount;
	}
	 /**
     * @return  the shoe-Type
     */
	public String getShoeType() {
		return shoeType;
	}

	 /**
     * set  the shoe-Type
     */
	public void setShoeType(String shoeType) {//need it?
		this.shoeType = shoeType;
	}
	 /**
     * @return  the customer name
     */
	public String getCostumer() {
		return customer;
	}

	 /**
     * @return  the request-Tick
     */
	public int getRequestTick() {
		return requestTick;
	}

	 /**
     * @return  the amount-Sold
     */
	public int getAmountSold() {
		return amountSold;
	}


	
}
