package bgu.spl.app;

import bgu.spl.app.passive.PurchaseSchedule;

public class Customer{
	private String name;
	private String[] wishList;
	private PurchaseSchedule[] purchaseSchedules;
	public Customer(String name, String[] wishList, PurchaseSchedule[] purchaseSchedules) {
		this.name = name;
		this.wishList = wishList;
		this.purchaseSchedules = purchaseSchedules;
	}
	public String getName() {
		return name;
	}
	public String[] getWishList() {
		return wishList;
	}
	public PurchaseSchedule[] getPurchaseSchedules() {
		return purchaseSchedules;
	}
}
