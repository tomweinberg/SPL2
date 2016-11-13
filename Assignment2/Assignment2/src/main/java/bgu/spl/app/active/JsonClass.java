package bgu.spl.app.active;

import java.lang.reflect.Type;

import bgu.spl.app.Customer;
import bgu.spl.app.ReturnedInfoClass;
import bgu.spl.app.passive.DiscountSchedule;
import bgu.spl.app.passive.PurchaseSchedule;
import bgu.spl.app.passive.ShoeStorageInfo;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

public class JsonClass implements JsonDeserializer<ReturnedInfoClass> {
	@Override
	public ReturnedInfoClass deserialize(final JsonElement json, final Type typeOfT, final JsonDeserializationContext context) throws JsonParseException {
		JsonObject jsonObject = json.getAsJsonObject();
		JsonArray jsonShoeStorageInfoArray = jsonObject.get("initialStorage").getAsJsonArray();
		ShoeStorageInfo[] storage = new ShoeStorageInfo[jsonShoeStorageInfoArray.size()];
		for (int i = 0; i < storage.length; i++) {
		      JsonElement jsonStorage = jsonShoeStorageInfoArray.get(i);
		      JsonObject storageObject = jsonStorage.getAsJsonObject();
		      JsonElement shoeTypeElement = storageObject.get("shoeType");
		      String shoeType = shoeTypeElement.getAsString();
		      JsonElement amountElement = storageObject.get("amount");
		      Integer amountOnStorage = amountElement.getAsInt();
		     // final JsonElement tickElement = storageObject.get("tick");
		      //final Integer tick = tickElement.getAsInt();
		      storage[i] = new ShoeStorageInfo(shoeType, amountOnStorage, 0);
		    }
		// getting the services object out of the json
		JsonObject jsonServicesObject = jsonObject.get("services").getAsJsonObject();
		
		//getting the timer settings out of the time object
		JsonObject jsonTimer = jsonServicesObject.get("time").getAsJsonObject();
		int speed = jsonTimer.get("speed").getAsInt();
		int duration = jsonTimer.get("duration").getAsInt();
		
		//getting the manager settings out of the manager object
		JsonObject jsonManger = jsonServicesObject.get("manager").getAsJsonObject();
		JsonArray jsonDiscountSheduleArray = jsonManger.get("discountSchedule").getAsJsonArray();
		DiscountSchedule[] discountSchedule = new DiscountSchedule[jsonDiscountSheduleArray.size()];
		for (int i = 0; i < discountSchedule.length; i++) {
		      JsonElement jsonDiscountSchedule = jsonDiscountSheduleArray.get(i);
		      JsonObject discountScheduleObject = jsonDiscountSchedule.getAsJsonObject();
		      JsonElement shoeTypeElement = discountScheduleObject.get("shoeType");
		      String shoeType = shoeTypeElement.getAsString();
		      JsonElement amountElement = discountScheduleObject.get("amount");
		      Integer amountOnStorage = amountElement.getAsInt();
		      JsonElement tickElement = discountScheduleObject.get("tick");
		      Integer tick = tickElement.getAsInt();
		      discountSchedule[i] = new DiscountSchedule(shoeType, tick, amountOnStorage);
		 }
		
		
		//getting the number of factories
		int factories = jsonServicesObject.get("factories").getAsInt();
		
		
		//getting the number of factories
		int sellers = jsonServicesObject.get("sellers").getAsInt();
		
		
		//getting the customers settings out of the WebClient object
		JsonArray jsonCustomersArray = jsonServicesObject.get("customers").getAsJsonArray();
		Customer[] theCustomers = new Customer[jsonCustomersArray.size()];
		for (int i = 0; i < theCustomers.length; i++) {
		      JsonElement jsonCustomer = jsonCustomersArray.get(i);
		      JsonObject customerObject = jsonCustomer.getAsJsonObject();
		      JsonElement nameElement = customerObject.get("name");
		      String name = nameElement.getAsString();
		      JsonArray jsonWishListArray = customerObject.get("wishList").getAsJsonArray();
		      String[] wishList = new String[jsonWishListArray.size()];
		      for(int j=0; j<jsonWishListArray.size(); j++){
		    	  JsonElement jsonShoeType = jsonWishListArray.get(j);
			      wishList[j]= jsonShoeType.getAsString();
		      }
		      JsonArray jsonPurchaseScheduleArray = customerObject.get("purchaseSchedule").getAsJsonArray();
		      PurchaseSchedule[] purchaseSchedules = new PurchaseSchedule[jsonPurchaseScheduleArray.size()];
		      for(int j=0; j<jsonPurchaseScheduleArray.size(); j++){
		    	  JsonElement jsonPurchaseSchedule = jsonPurchaseScheduleArray.get(j);
			      JsonObject purchaseScheduleObject = jsonPurchaseSchedule.getAsJsonObject();
			      purchaseSchedules[j]= new PurchaseSchedule(purchaseScheduleObject.get("shoeType").getAsString(), (purchaseScheduleObject.get("tick").getAsInt()));
		      }
		      theCustomers[i] = new Customer(name, wishList, purchaseSchedules);
		 }
		ReturnedInfoClass ret = new ReturnedInfoClass(storage, discountSchedule, factories, sellers, theCustomers, speed, duration);
		return ret;
	}
}
