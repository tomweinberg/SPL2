package bgu.spl.app.active;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.CountDownLatch;
import java.util.logging.Logger;
import bgu.spl.app.passive.TerminateBroadcast;
import bgu.spl.app.passive.TickBroadcast;
import bgu.spl.mics.MicroService;
/**
*Time Service  is extend of MicroService
*Time Service do import timer of java
*/
public class TimeService extends MicroService {
	private int speed;
	private int duration;
	private int currentTick;
	private Timer timer;
    private static Logger thelogger = Logger.getLogger("TimeService");
    /**
     * Constructor of Time Service  is extend of MicroService
     * @param speed the speed for the timer
     * @param duration when the timer need to stop
     * @param cdl send to the timer that the MicroService register.
     */
	
	public TimeService(int speed, int duration, CountDownLatch cdl){
		super("timer", cdl);
		this.duration= duration;
		this.speed = speed;
		timer= new Timer();
		currentTick=1;
	}
	 /** The Time Service  use the java timer for the time and send it as Broadcast
	  * 
	    */
	@Override
	protected void initialize() {
		try {
			cdl.await();
			thelogger.info("Timer has started all server did register");
			subscribeBroadcast(TerminateBroadcast.class, (t) ->{
				terminate();
			});
			timer.schedule(new TimerTask()
			{
				@Override
				public void run() {
					TickBroadcast tick = new TickBroadcast(currentTick);
					if(currentTick == duration+1){
						sendBroadcast(new TerminateBroadcast());
						timer.cancel();						
					}
					else{
						thelogger.info("sends a new tick: "+currentTick);
						sendBroadcast(tick);
						currentTick++;
					}
				}
			}, 0, speed);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
