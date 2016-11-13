package bgu.spl.app.active;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.concurrent.CountDownLatch;
import bgu.spl.app.Customer;
import bgu.spl.app.ReturnedInfoClass;
import bgu.spl.app.passive.DiscountSchedule;
import bgu.spl.app.passive.PurchaseSchedule;
import bgu.spl.app.passive.Store;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;


public class ShoeStoreRunner {
	public static void main(String[] args) {
		String inputJson = args[0];
		
		try {
			BufferedReader jsonReader = new BufferedReader(new FileReader(inputJson));
			GsonBuilder gsonBuilder = new GsonBuilder();
		    gsonBuilder.registerTypeAdapter(ReturnedInfoClass.class, new JsonClass());
		    Gson gson = gsonBuilder.create();
		    ReturnedInfoClass info = gson.fromJson(jsonReader, ReturnedInfoClass.class);
			Store.getInstance().load(info.getStorage());
			int numberOfServicesWithoutTheTimer = 1+ info.getFactories()+ info.getSellers() + info.getTheCustomers().length;
			CountDownLatch cdl = new CountDownLatch(numberOfServicesWithoutTheTimer);
			ArrayList<Thread> threads = new ArrayList<Thread>();
			for(int i=0; i< info.getFactories(); i++){
				Thread name1 = new Thread(new ShoeFactoryService("Factory #"+(i+1), cdl));
				name1.start();
				threads.add(name1);
			}
			for(int i=0 ; i<info.getSellers(); i++){
				Thread name2 = new Thread(new SellingService("Seller #"+(i+1), cdl));
				name2.start();
				threads.add(name2);
			}
			ArrayList <DiscountSchedule> dsl = new ArrayList<DiscountSchedule>();
			for(int i=0; i<info.getDiscountSchedule().length; i++){
				dsl.add(info.getDiscountSchedule()[i]);
			}
			Thread name3 = new Thread(new ManagementService(dsl, cdl));
			name3.start();
			threads.add(name3);
			for(int i=0; i<info.getTheCustomers().length; i++){
				Customer c = info.getTheCustomers()[i];
				ArrayList <PurchaseSchedule> psl = new ArrayList<PurchaseSchedule>();
				for(int j=0; j<c.getPurchaseSchedules().length; j++){
					psl.add(c.getPurchaseSchedules()[j]);
				}
				HashSet<String> wl = new HashSet<String>();
				for(int j=0; j<c.getWishList().length; j++){
					wl.add(c.getWishList()[j]);
				}
				Thread name4 = new Thread(new WebsiteClientService(c.getName(), psl ,wl, cdl));
				name4.start();
				threads.add(name4);
			}
			Thread name5 = new Thread(new TimeService(info.getTimerSpeed(), info.getTimerDuration(), cdl));
			name5.start();
			threads.add(name5);
			while(!threads.isEmpty()){
				try {
					threads.remove(0).join();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			Store.getInstance().print();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
}
