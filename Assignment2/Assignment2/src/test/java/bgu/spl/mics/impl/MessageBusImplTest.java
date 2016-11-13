package bgu.spl.mics.impl;

import static org.junit.Assert.*;

import java.util.concurrent.CountDownLatch;

import javax.naming.spi.DirStateFactory.Result;

import org.junit.Test;

import bgu.spl.app.passive.NewDiscountBroadcast;
import bgu.spl.app.passive.PurchaseOrderRequest;
import bgu.spl.mics.Broadcast;
import bgu.spl.mics.MicroService;
import bgu.spl.mics.Request;

public class MessageBusImplTest {

	private static final Class<? extends Request> Request = null;
	private CountDownLatch temp= new CountDownLatch(0);
	MicroService m=new MicroService("tom 12",temp) {
	
		
		@Override
		protected void initialize() {
			// TODO Auto-generated method stub
			
		}
	};
	@Test
	public void testRegister(){
		MessageBusImpl.getInstance().register(m);
	}
	@Test
	public void testUnRegister()
	{
		MessageBusImpl.getInstance().register(m);
		MessageBusImpl.getInstance().unregister(m);
	}

	@Test
	public <R>void testSubscribeRequest() {
		MessageBusImpl.getInstance().register(m);
		Request<R> t=new PurchaseOrderRequest<>("Nike", false, "tom 1", 1, 1);
		MessageBusImpl.getInstance().subscribeRequest(t.getClass(), m);
	}
	
	@Test
	public void testSubscribeBroadcast() {
		Broadcast broadcast= new NewDiscountBroadcast("tom 2",3);
		MessageBusImpl.getInstance().subscribeBroadcast(broadcast.getClass(), m);

	}
	
	@Test
	public <R> void testcomplete() {
		MessageBusImpl.getInstance().register(m);
		Request<R> r=new PurchaseOrderRequest<>("Nike", false, "tom weinberg", 1, 1);
		CountDownLatch temp1= new CountDownLatch(0);

		MicroService temp=new MicroService("temp",temp1) {
			
			@Override
			protected void initialize() {
				// TODO Auto-generated method stub
				
			}
		};
		MessageBusImpl.getInstance().subscribeRequest(r.getClass(), temp);
		boolean ans =MessageBusImpl.getInstance().sendRequest( r,  m);
	MessageBusImpl.getInstance().complete(r, null);

	}
	@Test

	public void testSendBroadcast(){
		NewDiscountBroadcast n=new NewDiscountBroadcast("NIKE", 1);
		MessageBusImpl.getInstance().sendBroadcast(n);
		
	}
	@Test
	public <R> void testSendRequest() {
		MessageBusImpl.getInstance().register(m);
		Request<R> r=new PurchaseOrderRequest<>("Nike", false, "tom weinberg", 1, 1);
		CountDownLatch temp1= new CountDownLatch(0);

		MicroService temp=new MicroService("temp",temp1) {
			
			@Override
			protected void initialize() {
				// TODO Auto-generated method stub
				
			}
		};
		MessageBusImpl.getInstance().subscribeRequest(r.getClass(), temp);
		
	
		boolean ans =MessageBusImpl.getInstance().sendRequest( r,  m);
		assertTrue(ans);
	}
	@Test
	public void testawaitMessage()
	{
		MessageBusImpl.getInstance().register(m);
		MessageBusImpl.getInstance().subscribeBroadcast(NewDiscountBroadcast.class, m);
		NewDiscountBroadcast n=new NewDiscountBroadcast("NIKE", 1);
		MessageBusImpl.getInstance().sendBroadcast(n);
		try{
			MessageBusImpl.getInstance().awaitMessage(m);
		}catch (InterruptedException e){
		}
		
	}

}
