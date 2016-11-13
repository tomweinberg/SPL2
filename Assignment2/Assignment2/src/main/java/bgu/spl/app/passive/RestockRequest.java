package bgu.spl.app.passive;

import bgu.spl.app.active.SellingService;
import bgu.spl.mics.Request;

public class RestockRequest<R> implements Request<R> {
	private boolean requestComplete;
	private String shoeType;
	private SellingService requestingSeller;
	private String customerName;
	  /**
     * build Restock request
     * @param shoeType the type of the shoe to Restock
     * @param requestingSeller the selling server that ask for the request
     */

	public RestockRequest(String shoeType, SellingService requestingSeller, String customer) {
		this.shoeType = shoeType;
		this.requestComplete=false;
		this.requestingSeller= requestingSeller;
		this.customerName= customer;
	}
	  /**
     * @return  The customer Name.
     */
	public String getCustomerName() {
		return customerName;
	}
	  /**
     * @return  The Selling Service that ask the request.
     */
	public SellingService getRequestingSeller() {
		return requestingSeller;
	}
	 /**
     * @return  if the request complete return true.
     */
	public boolean isRequestComplete() {
		return requestComplete;
	}
	 /**
     * set true or false if the request complete return true.
     * @param  requestComplete if the request complete
     */
	public void setRequestComplete(boolean requestComplete) {
		this.requestComplete = requestComplete;
	}
	 /**
     * @return  the shoe Type.
     */
	public String getShoeType() {
		return shoeType;
	}
	
	
	 /**
     *set the shoe Type.
     * @param  shoeType type of shoe
     */
	public void setShoeType(String shoeType) {
		this.shoeType = shoeType;
	}
	
	
	
}
