package utilities;

import java.util.Timer;
import java.util.TimerTask;

/**
 * This class implements a barrier that allows one thread in it to move on every time it is unlocked by the timer.
 * There is no guarantee of fairness, best used by only one thread.
 * @author Matthew
 */
public class TimerBarrier {
	private final RunBarrier barrier;
	private Timer timer;
	private int rate;
	private boolean started;
	
	/**
	 * Constructor for a TimerBarrier
	 * @param rate The rate at which the barrier unlocks, in miliseconds
	 */
	public TimerBarrier(int rate){
		timer = new Timer();
		barrier = new RunBarrier();
		this.rate = rate;
		started = false;
	}
	
	/**
	 * Starts up the timed barrier. Only call this once, subsequent calls do nothing.
	 */
	public synchronized void start(){
		if(started) return;
		
		timer = new Timer();
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				barrier.runNotify();
			}
		}, 0, rate); // Run every 30 milliseconds
		started = true;
	}
	
	/**
	 * Ends the timed barrier
	 */
	public synchronized void stop(){
		timer.cancel();
		started = false;
	}
	
	/**
	 * Call this to wait for the timed barrier
	 */
	public synchronized void await(){
		barrier.await();
	}
	
	/**
	 * Slightly modified barrier for the run method of the timer
	 */
	private class RunBarrier {
		private boolean wait;

		public RunBarrier() {
			wait = true;
		}

		public synchronized void await() {
			while (wait) {
				try {
					wait();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			wait = true;
		}

		public synchronized void runNotify() {
			wait = false;
			notifyAll();
		}
	}
}
