package edu.vuum.mocca;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @class PingPongPaddle
 * 
 * @brief This class uses Java Condition and ReentrantLock to
 *        implement a blocking pingpong paddle abstraction.
 */
public class PingPongPaddle {
    /** Condition used to wait for the Pingpong ball. */
    // TODO - You fill in here.
	Condition mWait = null;
	//Condition mNotWait = null;

    /** Lock used along with the Condition. */
    // TODO - You fill in here.
	Lock mLock = null;

    /** Do we have the ball or not. */
    // TODO - You fill in here.
	boolean mHaveBall;
	
    /**
     * Constructor initializes data members.
     */
    public PingPongPaddle(boolean haveBall) {
        // TODO - You fill in here.
    	mLock = new ReentrantLock();
    	mWait = mLock.newCondition();
    	mHaveBall = haveBall;
    }

    /**
     * Returns the ball to the other PingPongPaddle.
     */
    public void returnBall() {
        // TODO - You fill in here.
    	mLock.lock();
        // @@ Please put all this in a try/finally block.

    	mWait.signal();
    	mHaveBall = true;
    	mLock.unlock();
    }

    /**
     * Waits until the other PingPongPaddle hits the ball to us.
     */
    public void awaitBall() {
        // TODO - You fill in here.
    	mLock.lock();
    	try {
    		while(!mHaveBall){
                    // @@ Use awaitUninterruptibly() here and zap the exception stuff.
    			mWait.await();
    			}
    		mHaveBall = false;
    	}catch (InterruptedException e) {
				e.printStackTrace();
		} finally {
			mLock.unlock();
		}
    }
}

