package bgu.spl.app.passive;

import bgu.spl.mics.Broadcast;
/**
 * TickBroadcast implements Broadcast
 *Broadcast of the tick timer
 */
public class TickBroadcast implements Broadcast {
	private int currentTick;
/**
 * @param currentTick the current tick
 */
	public TickBroadcast(int currentTick) {
		this.currentTick = currentTick;
	}
	/**
	 * @return  The current tick
	 */
	public int getCurrentTick() {
		return currentTick;
	}
	
}
