package bgu.spl.app.passive;

public class DiscountSchedule {
	private String shoeType;
	private int tick;
	private int amount;
	  /**
     * build new discount Schedule 
     * @param shoeType the shoe type 
     * @param amount the amount of shoe in discount  
     */
	
	public DiscountSchedule(String shoeType, int tick, int amount) {
		this.shoeType = shoeType;
		this.tick = tick;
		this.amount = amount;
	}

	 /**
     * @return  The shoe-Type
     */
	public String getShoeType() {
		return shoeType;
	}
	 /**
     * @return  The tick to send the new discount
     */

	public int getTick() {
		return tick;
	}
	 /**
     * @return  The amount of shoe in discount
     */

	public int getAmount() {
		return amount;
	}
	
	
}
