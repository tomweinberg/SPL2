package bgu.spl.app;

import bgu.spl.app.Customer;
import bgu.spl.app.passive.DiscountSchedule;
import bgu.spl.app.passive.ShoeStorageInfo;

public class ReturnedInfoClass{
	private ShoeStorageInfo[] storage;
	private DiscountSchedule[] discountSchedule;
	private int factories;
	private int sellers;
	private Customer[] theCustomers;
	private int timerSpeed;
	private int timerDuration;

	public ReturnedInfoClass(ShoeStorageInfo[] storage, DiscountSchedule[] discountSchedule, int factories, int sellers, Customer[] theCustomers, int speed, int duration) {
		this.storage = storage;
		this.discountSchedule = discountSchedule;
		this.factories = factories;
		this.sellers = sellers;
		this.theCustomers = theCustomers;
		this.timerSpeed = speed;
		this.timerDuration= duration;
	}
	public int getTimerSpeed() {
		return timerSpeed;
	}
	public int getTimerDuration() {
		return timerDuration;
	}
	public ShoeStorageInfo[] getStorage() {
		return storage;
	}
	public DiscountSchedule[] getDiscountSchedule() {
		return discountSchedule;
	}
	public int getFactories() {
		return factories;
	}
	public int getSellers() {
		return sellers;
	}
	public Customer[] getTheCustomers() {
		return theCustomers;
	}
}
