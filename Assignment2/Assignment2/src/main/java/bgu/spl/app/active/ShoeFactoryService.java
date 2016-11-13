package bgu.spl.app.active;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CountDownLatch;
import java.util.logging.Logger;

import bgu.spl.app.passive.Receipt;
import bgu.spl.app.passive.ManufacturingOrderRequest;
import bgu.spl.app.passive.TerminateBroadcast;
import bgu.spl.app.passive.TickBroadcast;
import bgu.spl.mics.MicroService;

public class ShoeFactoryService extends MicroService {
	private int currentTick;
	private ConcurrentLinkedQueue<ManufacturingOrderRequest<Receipt>> orders;
	private ManufacturingOrderRequest<Receipt> needToSendReceipt;
	private int numberOfOrdersMadeForCurrentRequest;
    private static Logger thelogger = Logger.getLogger("ShoeFactoryService");

    /**
     * Constructor of ShoeFactory Service  is extend of MicroService
     * @param name the name of the ShoeFactory Service 
     * @param cdl send to the timer that the MicroService register.
     */
	public ShoeFactoryService(String name, CountDownLatch cdl) {
		super(name, cdl);
		currentTick=0;
		orders = new ConcurrentLinkedQueue<ManufacturingOrderRequest<Receipt>>();
		needToSendReceipt=null;
		numberOfOrdersMadeForCurrentRequest=0;
	}
	 /** The ShoeFactory Service  subscribe Broadcast to TickBroadcast for getting the time
	  * The ShoeFactory Service subscribe Request to Manufacturing Order Request
	    */
	@Override
	protected void initialize() {
		subscribeBroadcast(TickBroadcast.class, (b) ->{
			currentTick=b.getCurrentTick();
			if(!orders.isEmpty()){
				if(orders.peek()==needToSendReceipt){
					if(orders.peek().getRequestTick() != currentTick){              
						needToSendReceipt = orders.remove();
						numberOfOrdersMadeForCurrentRequest++;
					}
				}
				else {             
					Receipt rec = new Receipt(getName(), "store", needToSendReceipt.getShoeType(), false, currentTick, needToSendReceipt.getRequestTick() ,numberOfOrdersMadeForCurrentRequest);
					complete(needToSendReceipt, rec);
					thelogger.info("Order for shoes of type: "+needToSendReceipt.getShoeType()+ " has been completed.");
					needToSendReceipt = orders.remove();
					numberOfOrdersMadeForCurrentRequest=1;
				}
			}
			else if(needToSendReceipt != null){
				Receipt rec = new Receipt(getName(), "store", needToSendReceipt.getShoeType(), false, currentTick, needToSendReceipt.getRequestTick() ,numberOfOrdersMadeForCurrentRequest);
				complete(needToSendReceipt, rec);
				thelogger.info("Order for shoes of type: "+needToSendReceipt.getShoeType()+ " has been completed.");
				needToSendReceipt=null;
				numberOfOrdersMadeForCurrentRequest=0;
			}
		});
		
		subscribeBroadcast(TerminateBroadcast.class, (t) ->{
			terminate();
		});
		
		subscribeRequest(ManufacturingOrderRequest.class, (mor) ->{
			for(int i=0; i< mor.getAmount(); i++){
				orders.add(mor);
			}
			thelogger.info("Number of orders at the shoe factory "+this.getName()+" is: "+orders.size());
		
			if(needToSendReceipt==null){
				needToSendReceipt=mor;
			}
		});
		cdl.countDown();
	}
}
