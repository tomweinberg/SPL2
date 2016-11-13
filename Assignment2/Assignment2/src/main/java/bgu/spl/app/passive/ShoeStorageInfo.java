package bgu.spl.app.passive;

public class ShoeStorageInfo {
	private String shoeType;
	private int amountOnStorage;
	private int discountAmount;
	
	
	  /**
     * build the info of one shoe
     * @param shoeType the type of the shoe
     * @param amountOnStorage the amount of the shoe in storage
     * @param discountAmount how many of the shoe is in discount
     */
	
	public ShoeStorageInfo(String shoeType, int amountOnStorage, int discountAmount) {
		this.shoeType = shoeType;
		this.amountOnStorage = amountOnStorage;
		this.discountAmount = discountAmount;
	}
	
	  /**
     * @return  The shoe type.
     */
	public String getShoeType() {
		return shoeType;
	}
	  /**
     *  set The shoe type.
     */
	public void setShoeType(String shoeType) {
		this.shoeType = shoeType;
	}
	  /**
     * @return  The amount of this shoe type.
     */
	public int getAmountOnStorage() {
		return amountOnStorage;
	}
	  /**
     * @return  set the amount of this shoe type in the storage.
     */
	public void setAmountOnStorage(int amountOnStorage) {
		this.amountOnStorage = amountOnStorage;
	}
	  /**
     * @return  The amount of shoes in discount of this shoe type.
     */
	public int getDiscountAmount() {
		return discountAmount;
	}
	  /**
     * set The the amount of shoe in  discount of this type.
     */
	public void setDiscountAmount(int discountAmount) {
		this.discountAmount = discountAmount;
	}
	
	

}
