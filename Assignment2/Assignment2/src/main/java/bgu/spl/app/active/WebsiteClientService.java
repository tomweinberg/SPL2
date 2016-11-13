	package bgu.spl.app.active;
	
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import bgu.spl.app.passive.NewDiscountBroadcast;
import bgu.spl.app.passive.Receipt;
import bgu.spl.app.passive.PurchaseOrderRequest;
import bgu.spl.app.passive.PurchaseSchedule;
import bgu.spl.app.passive.TerminateBroadcast;
import bgu.spl.app.passive.TickBroadcast;
import bgu.spl.mics.MicroService;
import java.util.logging.*;
	
	
	
	/**
	*Web-site Client Service is extend of MicroService
	*The Web-site Client Service IN  responsible to send to shoes the Client
	*/
	public class WebsiteClientService extends MicroService{
	private List<PurchaseSchedule> listP;
	private Set<String> wishList;
	private int currentTick;
	private int requestsRecieved;
	private int listsSizes;
    private static Logger thelogger = Logger.getLogger("Web-site-Client-Service");
    /**
     * Constructor of Web-site Client is extend of MicroService
     *  <p>
     * @param name the name of the Selling Service
     * @param listp List of shoes that the Web-site Client want to buy
     * @param wishList unsorted set of shoe that the Web-site Client want to buy only in discount
     * @param cdl send to the timer that the MicroService register.
     */
 
	public WebsiteClientService(String name,List<PurchaseSchedule> listP,Set<String> wishList, CountDownLatch cdl) {
		super(name, cdl);
		this.listP=listP;
		currentTick=0;
		Collections.sort(listP,  new Comparator<PurchaseSchedule>(){
			public int compare (PurchaseSchedule p1,PurchaseSchedule p2){
				return p1.getTick()-p2.getTick();
			}
		});
		this.wishList=wishList;
		requestsRecieved=0;
		listsSizes = wishList.size()+ listP.size();
	}
	
	 /** The Web-site Client  subscribe Broadcast to TickBroadcast for getting the time
	  * The Web-site Client subscribe Broadcast to New Discount Broadcast for the wish list
	    */
	@Override
	protected void initialize() {
		
		subscribeBroadcast(NewDiscountBroadcast.class, message->{
			Iterator<String> it = wishList.iterator();
			while(it.hasNext()){
				String shoe = it.next();
				if(shoe.equals(message.getShoeType())){
					PurchaseOrderRequest<Receipt> req=new PurchaseOrderRequest<Receipt>(shoe,true, getName(), currentTick, 1); 
					sendRequest(req, (c)->{
						if(c!=null){
							thelogger.info(getName()+" bought "+shoe+" on discount!");
							requestsRecieved++;
							wishList.remove(shoe);
							Iterator<PurchaseSchedule> itit = listP.iterator();
							boolean found=false;
							while(itit.hasNext() && !found){
								PurchaseSchedule tmp = itit.next();
								if(tmp.getShoeType()==shoe){
									listP.remove(tmp);
									found=true;
								}
							}	
						}
					});
				}
			}
	});
	
		
		subscribeBroadcast(TickBroadcast.class, c->{
			currentTick= c.getCurrentTick();
				while(!listP.isEmpty() && listP.get(0).getTick() == c.getCurrentTick()){
					PurchaseOrderRequest<Receipt> req=new PurchaseOrderRequest<Receipt>(listP.get(0).getShoeType(), false, getName(), currentTick, 1);
					sendRequest(req, V->{
						requestsRecieved++;
					});
					listP.remove(0);
				}
			if(requestsRecieved==listsSizes)  
				terminate();
		});
		subscribeBroadcast(TerminateBroadcast.class, (mssg)->{
			terminate();
		});
		cdl.countDown();
	}
}
