package edu.vanderbilt.a4_main.bouncy;

import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.concurrent.locks.StampedLock;

import edu.vanderbilt.a4_android.bouncy.BarrierManagerStrategy.BarrierManagerBase;
import edu.vanderbilt.a4_android.bouncy.BouncyBalloon;
import edu.vanderbilt.a4_android.bouncy.BouncyBarrier;

/**
 * @class StampedBarrierManager
 *
 * @brief An implementation of BarrierManager that uses the StampedLocks.
 */
class StampedBarrierManager extends BarrierManagerBase {
    /**
     * A StampedLock to restrict access to the barriers.
     */
    StampedLock mLock;

    /**
     * Constructor initializes the data members.
     */
    public StampedBarrierManager() {
        // TODO - You fill in here.
        mLock = new StampedLock();
    }

    /**
     * 
     */
    @Override
    public void addBarrier(BouncyBarrier b) {
        // TODO - You fill in here.
    	long stamp = mLock.writeLock();
    	try{
    		super.addBarrier(b);
    	}finally{
    		mLock.unlockWrite(stamp);
    	}
    }

    /**
     *
     */
    private boolean tryOptimisticBounce(BouncyBalloon b) {
        // If we have 2 bounces left, don't even try optimistic
        // reading.
        if (b.getBouncesLeft() <= 2)
            return false;

        // TODO - You fill in here to fix this implementation. Some comments are left to guide you. 
		
        // Get an optimstic reading lock
        long stamp = mLock.tryOptimisticRead();

        // Iterate over all the barriers.
        boolean bouncedX = false;
        boolean bouncedY = false;

        try {
            for (Iterator<BouncyBarrier> i = mBarriers.iterator(); i.hasNext();) {
                BouncyBarrier barrier = i.next();

                // Sometimes returns null pointers because we're
                // modifying it concurrently.
                if (barrier == null)
                    return false;

                if (barrier.crossesHorizontally(b)) {
                    bouncedX = true;
                }
                if (barrier.crossesVertically(b)) {
                    bouncedY = true;
                }

                if (bouncedX || bouncedY) {
                    break;
                }
            }
        } catch (ConcurrentModificationException e) {
            return false;
        }

        // Return false if the lock fails to validate
        if(!mLock.validate(stamp)){
        	return false;
        }
        // Otherwise bounce the balloons and return true.
        else{
        	if(bouncedX) b.bounceX();
        	if(bouncedY) b.bounceY();
        	return true;
        }
    }

    /**
     * Uses a StampedLock to use optimistic locking
     */
    @Override
    public void bounceAndRemoveIfNecessary(BouncyBalloon b) {
		
        // Try doing things optimistically first.
        if (tryOptimisticBounce(b))
            return;

        // TODO - You fill in here. Some comments are left to guide you.
		
        // If that doesn't work, grab a read lock.
        long stamp = mLock.readLock();
        
        // Bounce the balloon and figure out if we need to remove a barrier.
        BouncyBarrier toRemove = bounceIfNecessary(b);
        
        // If we have to remove a barrier.
        // Try upgrading to a write lock.
        if (toRemove != null){
        	long ws = mLock.tryConvertToWriteLock(stamp);   	
 
        	// If we didn't acquire the write lock on the first try,
            // release the read lock and explicity acquire the write lock.
        	if(ws == 0L){
        		mLock.unlockRead(stamp);
        		stamp = mLock.writeLock();
        	}
        	else{
        		stamp = ws;	
        	}
        	// Remove the barrier.
            // Release the lock
			super.removeBarrier(toRemove);
			mLock.unlock(stamp);
		// Else just release the read lock	
        }else{
    		mLock.unlockRead(stamp);
    	}
    }

    /**
     * Removes all barriers from this manager. Coordinate access using the
     * StampedLock.
     */
    @Override
    public void clear() {
        // TODO - You fill in here.
    	long stamp = mLock.writeLock();
    	try{
    		super.clear();
    	}finally{
    		mLock.unlockWrite(stamp);
    	}
    }

}
