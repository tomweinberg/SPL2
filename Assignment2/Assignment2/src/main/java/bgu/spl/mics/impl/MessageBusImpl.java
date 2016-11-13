package bgu.spl.mics.impl;

import java.util.Hashtable;
import java.util.Iterator;
import java.util.concurrent.ConcurrentLinkedQueue;

import bgu.spl.mics.Broadcast;
import bgu.spl.mics.Message;
import bgu.spl.mics.MessageBus;
import bgu.spl.mics.MicroService;
import bgu.spl.mics.Request;
import bgu.spl.mics.RequestCompleted;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;


public class MessageBusImpl implements MessageBus {
	
	private Hashtable <MicroService, LinkedBlockingQueue<Message>> microServiceHash;
	private Hashtable <Class, RoundRobin<MicroService>> requestsTypeHash;
	private Hashtable<Class, RoundRobin<MicroService>> broadcastsTypeHash;
	private Hashtable <Request, MicroService> requestCompleteHash;
	private Hashtable <MicroService, ConcurrentLinkedQueue<RoundRobin<MicroService>>> references;
	private ReadWriteLock reqBroadLock;
	private static class SingletonHolder {
        private static MessageBusImpl instance = new MessageBusImpl();
    }
	
		
	
    private MessageBusImpl() {
        microServiceHash = new Hashtable<MicroService, LinkedBlockingQueue<Message>>();
        requestsTypeHash = new Hashtable<Class, RoundRobin<MicroService>>();
        broadcastsTypeHash = new Hashtable<Class, RoundRobin<MicroService>>();
        requestCompleteHash = new Hashtable<Request, MicroService>();
        references = new Hashtable<MicroService, ConcurrentLinkedQueue<RoundRobin<MicroService>>>();
        reqBroadLock = new ReentrantReadWriteLock();
    }
    
	

	@Override
	public void subscribeRequest(Class<? extends Request> type, MicroService m) {
		if(!requestsTypeHash.containsKey(type)){
			synchronized (type) {                   
				if(!requestsTypeHash.containsKey(type))
					requestsTypeHash.put(type, new RoundRobin <MicroService>());
			}
		}
		requestsTypeHash.get(type).add(m);
		references.get(m).add(requestsTypeHash.get(type));
	}

	
	
	@Override
	public void subscribeBroadcast(Class<? extends Broadcast> type, MicroService m) {
		if(!broadcastsTypeHash.containsKey(type)){
			synchronized (type) {                       
				if(!broadcastsTypeHash.containsKey(type))
					broadcastsTypeHash.put(type, new RoundRobin <MicroService>());
			}
		}
		broadcastsTypeHash.get(type).add(m);
		references.get(m).add(broadcastsTypeHash.get(type));
	}

	@Override
	public <T> void complete(Request<T> r, T result) {
		try {
			reqBroadLock.readLock().lock();
			microServiceHash.get(requestCompleteHash.get(r)).put(new RequestCompleted<T>(r, result));
			reqBroadLock.readLock().unlock();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}   
	}

	@Override
	public void  sendBroadcast(Broadcast b) {
		reqBroadLock.writeLock().lock();
		if(broadcastsTypeHash.containsKey(b.getClass())){	
			synchronized (broadcastsTypeHash.get(b.getClass())) {
				Iterator<MicroService> itTheQueue = broadcastsTypeHash.get(b.getClass()).iterator();
				while(itTheQueue.hasNext()){
					try {
						microServiceHash.get(itTheQueue.next()).put(b);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}	
		}
		reqBroadLock.writeLock().unlock();
	}

	@Override
	public boolean sendRequest(Request<?> r, MicroService requester) {
		if(requestsTypeHash.containsKey(r.getClass())){
			reqBroadLock.readLock().lock();
			synchronized (requestsTypeHash.get(r.getClass())) {                         
				MicroService tmp = requestsTypeHash.get(r.getClass()).poll();
				if(tmp != null){
					requestCompleteHash.put(r, requester);
					microServiceHash.get(tmp).add(r);
					reqBroadLock.readLock().unlock();
					return true;
				}
			}
		}
		reqBroadLock.readLock().unlock();
		return false;
	}

	@Override
	public void register(MicroService m) {
		microServiceHash.put(m, new LinkedBlockingQueue<Message>());
		references.put(m, new ConcurrentLinkedQueue<RoundRobin<MicroService>>());
	}

	@Override
	public void unregister(MicroService m) {
		while(!references.get(m).isEmpty()){
			RoundRobin<MicroService> tmp = references.get(m).remove();
			tmp.remove(m);
		}
		references.remove(m);
		microServiceHash.remove(m);
	}

	@Override
	public Message awaitMessage(MicroService m) throws InterruptedException {
		Message mes = microServiceHash.get(m).take();
		return mes;
	}
	
	public static MessageBusImpl getInstance() {
        return SingletonHolder.instance;
    }

}
