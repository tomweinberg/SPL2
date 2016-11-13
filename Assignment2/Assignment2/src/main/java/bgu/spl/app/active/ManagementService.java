package bgu.spl.app.active;
import java.util.logging.*;
import java.util.Comparator;
import java.util.Hashtable;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CountDownLatch;

import bgu.spl.app.passive.NewDiscountBroadcast;
import bgu.spl.app.passive.Receipt;
import bgu.spl.app.passive.DiscountSchedule;
import bgu.spl.app.passive.ManufacturingOrderRequest;
import bgu.spl.app.passive.RestockRequest;
import bgu.spl.app.passive.Store;
import bgu.spl.app.passive.TerminateBroadcast;
import bgu.spl.app.passive.TickBroadcast;
import bgu.spl.mics.MicroService;
/**
*Management Service is extend of MicroService
*the Management Service can send new discount broadcast and ask the factory for more shoes
*/

public class ManagementService extends MicroService {
	private int currentTick;
	private List<DiscountSchedule> discounts;
	private Hashtable<String, ConcurrentLinkedQueue<RestockRequest<Boolean>>> whoDoIOwe;
	private Hashtable<String, Integer> howMuchToStore;
    private static Logger thelogger = Logger.getLogger("Web-site-Client-Service");
    
    /**
    * Constructor of Management Service is extend of MicroService
    * @param discounts list of DiscountSchedule not sorted-sorted it by tick.
    * @param cdl send to the timer that the MicroService register.
    */
	public ManagementService(List<DiscountSchedule> discounts, CountDownLatch cdl) {
		super("manager", cdl);
		List<DiscountSchedule> tmp = discounts;
		tmp.sort(new Comparator<DiscountSchedule>(){
			@Override
			public int compare(DiscountSchedule ds1, DiscountSchedule ds2) {
				return ds1.getTick()- ds2.getTick();
			}
		});
		this.discounts=tmp;
		whoDoIOwe= new Hashtable<String, ConcurrentLinkedQueue<RestockRequest<Boolean>>>();
		howMuchToStore = new Hashtable<String, Integer>();
	}
	 /** The Management Service subscribe Broadcast to TickBroadcast for getting the time
	  * The Management Service subscribe Request to RestockRequest 
	    */
	@Override
	protected void initialize() {
		subscribeBroadcast(TickBroadcast.class, (b) ->{
			currentTick=b.getCurrentTick();
			while(!discounts.isEmpty() && discounts.get(0).getTick()==currentTick){
				DiscountSchedule theDiscount = discounts.remove(0);
				Store.getInstance().addDiscount(theDiscount.getShoeType(), theDiscount.getAmount());
				thelogger.info("the manager send new discount the shoe type is "+theDiscount.getShoeType()+" and the amount is "+theDiscount.getAmount());
				NewDiscountBroadcast broad = new NewDiscountBroadcast(theDiscount.getShoeType(), theDiscount.getAmount());
				sendBroadcast(broad);
			}
		});
		
		subscribeBroadcast(TerminateBroadcast.class, (t) ->{
			terminate();
		});
		
		subscribeRequest(RestockRequest.class, (rr)->{
			if(howMuchToStore.containsKey(rr.getShoeType()) && howMuchToStore.get(rr.getShoeType())>0){
				ConcurrentLinkedQueue<RestockRequest<Boolean>> tmp = whoDoIOwe.get(rr.getShoeType());
				if(tmp!=null){
					tmp.add(rr);
					thelogger.info("Request to reserve "+rr.getShoeType()+" completed !");
					int newAmount = howMuchToStore.get(rr.getShoeType())-1;
					howMuchToStore.replace(rr.getShoeType(), newAmount);
					if(newAmount == 0){
						howMuchToStore.remove(rr.getShoeType());
					}
				}
			}
			else{
				ManufacturingOrderRequest<Receipt> mor = new ManufacturingOrderRequest<Receipt>(rr.getShoeType(), (currentTick % 5) + 1, currentTick);
				boolean takeCareOf = sendRequest(mor, (rec) -> {
						int AmountToAddTheStore=0;
						if(howMuchToStore.containsKey(rr.getShoeType())){
							AmountToAddTheStore= howMuchToStore.get(rr.getShoeType());
						}
						Store.getInstance().add(rec.getShoeType(), AmountToAddTheStore);
						Store.getInstance().file(rec);
						ConcurrentLinkedQueue<RestockRequest<Boolean>> tmp = whoDoIOwe.get(rec.getShoeType());
						int min=Math.min(tmp.size(), rec.getAmountSold());
						while(min>0){
							complete(tmp.poll(), true);
							min--;
						}
				});
				if(takeCareOf){
					howMuchToStore.put(rr.getShoeType(), (currentTick%5));
					if(!whoDoIOwe.containsKey(rr.getShoeType()))
						whoDoIOwe.put(rr.getShoeType(), new ConcurrentLinkedQueue<RestockRequest<Boolean>>());
					whoDoIOwe.get(rr.getShoeType()).add(rr);
					thelogger.info("Request to reserve "+rr.getShoeType()+" completed !");
				}
				else{
					complete(rr, false);
					thelogger.info("Request to reserve "+mor.getShoeType()+" for customer "+rr.getCustomerName()+" couldn't be completed because no factory is available.");
				}
			}
		});
		cdl.countDown();
	}
}
