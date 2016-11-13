package bgu.spl.app.passive;

public class PurchaseSchedule {
	private String shoeType;
	private int tick;
	  /**
     * build the Schedule of one Purchase
     * @param shoeType the type of the shoe
     * @param tick the tick to try to buy the shoe
     */
	public PurchaseSchedule(String shoeType, int tick) {
		this.shoeType = shoeType;
		this.tick = tick;
	}
	 /**
     * @return  The shoe-Type .
     */
	public String getShoeType() {
		return shoeType;
	}
	 /**
     * set The shoe-Type .
     */
	public void setShoeType(String shoeType) {
		this.shoeType = shoeType;
	} /**
     * @return  The tick .
     */

	public int getTick() {
		return tick;
	}
	 /**
     * set  The tick .
     */
	public void setTick(int tick) {
		this.tick = tick;
	}
	
	
}
