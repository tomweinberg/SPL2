package bgu.spl.app.active;

import java.util.logging.*;
import java.util.concurrent.CountDownLatch;

import bgu.spl.app.passive.BuyResult;
import bgu.spl.app.passive.PurchaseOrderRequest;
import bgu.spl.app.passive.Receipt;
import bgu.spl.app.passive.RestockRequest;
import bgu.spl.app.passive.Store;
import bgu.spl.app.passive.TerminateBroadcast;
import bgu.spl.app.passive.TickBroadcast;
import bgu.spl.mics.MicroService;
/**
*Selling Service is extend of MicroService
*the Selling Service in responsible to take care on Purchase Order Request and if its need to request Restock Request
*/
public class SellingService extends MicroService {
	private int currTick;
    private static Logger thelogger = Logger.getLogger("Selling-Service");

    /**
     * Constructor of Selling Service is extend of MicroService
     * @param name the name of the Selling Service
     * @param cdl send to the timer that the MicroService register.
     */
	public SellingService(String name, CountDownLatch cdl) {
		super(name, cdl);
		currTick=0;
	}
	 /** The Selling Service  subscribe Broadcast to TickBroadcast for getting the time
	  * The Selling Service subscribe Request to Purchase Order Request
	  * The Selling Service send Restock Request if its needed
	    */
	@Override
	protected void initialize() {
		subscribeBroadcast(TerminateBroadcast.class, (t) ->{
			terminate();
		});
		
		subscribeBroadcast(TickBroadcast.class, (broad) ->{
			currTick = broad.getCurrentTick();
		});
		
		subscribeRequest(PurchaseOrderRequest.class, (por) ->{
				BuyResult answer = Store.getInstance().take(por.getShoeType(), por.getDiscount());
				if(answer.name().equals("REGULAR_PRICE") || answer.name().equals("DISCOUNTED_PRICE")){
					thelogger.info("Request to buy "+por.getShoeType()+" completed by "+getName());
					Receipt rec = new Receipt(getName(), por.getCostumer(), por.getShoeType(), answer.name().equals("DISCOUNTED_PRICE"), currTick, por.getRequestTick() , por.getAmountSold());
					Store.getInstance().file(rec);
					complete(por, rec);
				}
				else if(answer.name().equals("NOT_ON_DISCOUNT")||(por.getDiscount())){
					thelogger.info("Request to buy "+por.getShoeType()+" couldn't be completed by "+getName()+ " because it is not on discount.");
					complete(por, null);
				}
				else{
					RestockRequest<Boolean> restock = new RestockRequest<Boolean>(por.getShoeType(), this, por.getCostumer());
					sendRequest(restock, (bool) ->{
						if(!bool){ 
							complete(por, null);
							thelogger.info("Request to buy "+por.getShoeType()+" couldn't be completed. There are no avaliable factories available to make this shoe.");
						}
						else{
							Receipt rec = new Receipt(getName(), por.getCostumer(), por.getShoeType(), false, currTick, por.getRequestTick() , por.getAmountSold());
							Store.getInstance().file(rec);
							complete(por, rec);
							thelogger.info("Request to buy "+por.getAmountSold()+" "+por.getShoeType()+" completed !");
						}
					});
					thelogger.info("A Restock Request to buy "+por.getAmountSold()+" "+por.getShoeType()+" was sent by "+getName());
				}
		});
		cdl.countDown();
	}
}
