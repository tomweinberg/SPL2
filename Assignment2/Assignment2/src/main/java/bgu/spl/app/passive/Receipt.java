package bgu.spl.app.passive;

public class Receipt {
	private String seller;
	private String costumer;
	private String shoeType;
	private boolean discount;
	private int issuedTick;
	private int requestTick;
	private int amountSold;
	  /**
     * build Receipt request
     * @param seller the name of the seller
     * @param costumer the name of the costumer
     * @param shoeType the type of the shoe
     * @param discount true if the costumer want the shoe in discount
     * @param issuedTick the tick that the request complete
     * @param requestTick  the tick that the request request
     * @param amountSold the amount of the shoes
     *     
     */
	
	public Receipt(String seller, String costumer, String shoeType, boolean discount, int issuedTick, int requestTick, int amountSold) {
		this.seller = seller;
		this.costumer = costumer;
		this.shoeType = shoeType;
		this.discount = discount;
		this.issuedTick = issuedTick;
		this.requestTick = requestTick;
		this.amountSold = amountSold;
	}
	 /**
     * @return  The Seller Name.
     */
	public String getSeller() {
		return seller;
	}
	 /**
     * @return  The costumer Name.
     */

	public String getCostumer() {
		return costumer;
	}

	 /**
     * @return  The shoe Type .
     */
	public String getShoeType() {
		return shoeType;
	}

	 /**
     * @return true if the costumer want in discount .
     */
	public boolean isDiscount() {
		return discount;
	}

	 /**
     * @return  The issued-Tick  .
     */
	public int getIssuedTick() {
		return issuedTick;
	}
	 /**
     * @return  The request-Tick  .
     */

	public int getRequestTick() {
		return requestTick;
	}
	 /**
     * @return  The amount  .
     */


	public int getAmountSold() {
		return amountSold;
	}
	
	
}
