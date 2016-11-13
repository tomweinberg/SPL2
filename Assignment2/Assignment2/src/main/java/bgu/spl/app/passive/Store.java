package bgu.spl.app.passive;

import java.util.Hashtable;
import java.util.Iterator;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.Logger;

public class Store {
	private static class SingletonHolder {
        private static Store instance = new Store();
        private static Logger thelogger = Logger.getLogger("Store");
    }
	private Hashtable<String, ShoeStorageInfo> shoes;  //shoeType - the info
	private CopyOnWriteArrayList<Receipt> receipts;
	private Store(){
		shoes = new Hashtable<String, ShoeStorageInfo>();
		receipts = new CopyOnWriteArrayList<Receipt>();
	}
	
	/**
	* @return return the singleton store, if it did not existent build it.
	*/
	public static Store getInstance() {
        return SingletonHolder.instance;
    }
	
	
    /**
     * load the shoes to the storage of the store .
     */
	
	public void load(ShoeStorageInfo[] storage){      //shallow copy
		Logger.global.info("Load Shose to the store");
		for(ShoeStorageInfo shoe: storage){
			shoes.put(shoe.getShoeType(), shoe);
		}
	}
	
    /**
     * take one shoe from the store.
     *
     * @param shoeType  the shoe type
     * @param onlyDiscount if the costumer want to buy in discount
     *  @return the result of the buy.
     */
	public BuyResult take(String shoeType, boolean onlyDiscount){
		if(shoes.containsKey(shoeType)){                      
			ShoeStorageInfo reqShoe = shoes.get(shoeType);
			if(reqShoe.getAmountOnStorage()>0){              
				if(reqShoe.getDiscountAmount()>0){          
					reqShoe.setAmountOnStorage(reqShoe.getAmountOnStorage()-1);
					reqShoe.setDiscountAmount(reqShoe.getDiscountAmount()-1);
					return BuyResult.DISCOUNTED_PRICE;
				}
				else if(!onlyDiscount){                    
					reqShoe.setAmountOnStorage(reqShoe.getAmountOnStorage()-1);
					return BuyResult.REGULAR_PRICE;
				}
				else if(onlyDiscount){                    
					return BuyResult.NOT_ON_DISCOUNT;
				}
			}
		}
		return BuyResult.NOT_IN_STOCK;
	}
	  /**
     * add the amount of  shoes to the store.
     * @param shoeType  the shoe type
     * @param amount the amount of shoes to add
     */
	public void add(String shoeType, int amount){
		if(shoes.containsKey(shoeType)){                   
			ShoeStorageInfo reqShoe = shoes.get(shoeType);
			reqShoe.setAmountOnStorage(reqShoe.getAmountOnStorage()+amount);
		}
		else{
			ShoeStorageInfo addIt = new ShoeStorageInfo(shoeType, amount, 0);
			shoes.put(shoeType, addIt);
		}
	}
	  /**
     * add the amount of shoes to the store.
     * @param shoeType  the shoe type
     * @param amount the amount of shoes to add discount
     */
	
	public void addDiscount(String shoeType, int amount){
		if(shoes.containsKey(shoeType)){                     
			ShoeStorageInfo reqShoe = shoes.get(shoeType);
			if(amount+reqShoe.getDiscountAmount() <= reqShoe.getAmountOnStorage()){
				reqShoe.setDiscountAmount(reqShoe.getDiscountAmount()+amount);
			Logger.global.info("Now "+reqShoe.getDiscountAmount()+amount+" of Type  "+shoeType+" is in Discount");}
			else{
				reqShoe.setDiscountAmount(reqShoe.getAmountOnStorage());
				Logger.global.info("Now "+reqShoe.getDiscountAmount()+" of Type "+shoeType+" is in Discount");
			}
		}
	}
	  /**
     * add a  Receipt to the store
     * @param receipt of Type to add the store
     */
	public void file(Receipt receipt){
		receipts.add(receipt);
	}
	  /**
     * print all the receipt in the store and the shoes that stay in the store.
     */
	public void print(){
		shoes.forEach((shoe, info) ->
		{
			System.out.println("Shoe type: "+ shoe+ ",   Amount: "+ info.getAmountOnStorage()+",   Discounted amount: "+info.getDiscountAmount());
		});
		Iterator<Receipt> currReceipt = receipts.iterator();
		while(currReceipt.hasNext()){
			Receipt r= currReceipt.next();
			System.out.println("Receipt for: "+r.getShoeType()+",   Seller: "+r.getSeller()+",   Costumer: "+r.getCostumer()+",   Discount given: "+r.isDiscount()+",   Issued tick: "+r.getIssuedTick()+",   Request tick: "+r.getRequestTick()+",   Amount sold: "+r.getAmountSold());
		}
	}
	
}
