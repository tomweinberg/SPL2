package bgu.spl.app.passive;

import static org.junit.Assert.*;
import org.junit.Test;

public class StoreTest {

	@Test
	public void testLoad()
	{
		
		ShoeStorageInfo temp=new ShoeStorageInfo("Nike", 2, 0);
		ShoeStorageInfo temp2=new ShoeStorageInfo("Reebok", 1, 1);
		ShoeStorageInfo storge[]=new ShoeStorageInfo[2];
		storge[0]=temp;
		storge[1]=temp2;
		
		Store.getInstance().load(storge);
	}
	@Test
	public void testTake()
	{
		boolean ans=true;
		ShoeStorageInfo temp=new ShoeStorageInfo("Nike", 2, 0);
		ShoeStorageInfo temp2=new ShoeStorageInfo("Reebok", 1, 1);
		ShoeStorageInfo storge[]=new ShoeStorageInfo[2];
		storge[0]=temp;
		storge[1]=temp2;
		
		 Store.getInstance().load(storge);
		BuyResult buyResultReturn= Store.getInstance().take("Reebok", false);
		switch (buyResultReturn) {
		case DISCOUNTED_PRICE:
			assertTrue(ans);
			break;

		default:
			assertTrue(ans);
			break;
		}
	}
	@Test
	public void testAdd()
	{
		ShoeStorageInfo temp=new ShoeStorageInfo("Nike", 2, 0);
		ShoeStorageInfo temp2=new ShoeStorageInfo("Reebok", 1, 1);
		ShoeStorageInfo storge[]=new ShoeStorageInfo[2];
		storge[0]=temp;
		storge[1]=temp2;
		
		 Store.getInstance().load(storge);
		 Store.getInstance().add("Nike" , 2);
	}
	@Test
	public void testAddDiscount()
	{
		ShoeStorageInfo temp=new ShoeStorageInfo("Nike", 2, 0);
		ShoeStorageInfo temp2=new ShoeStorageInfo("Reebok", 1, 1);
		ShoeStorageInfo storge[]=new ShoeStorageInfo[2];
		storge[0]=temp;
		storge[1]=temp2;
		
		 Store.getInstance().load(storge);
		 Store.getInstance().addDiscount("Nike", 1);
	}
	



}
