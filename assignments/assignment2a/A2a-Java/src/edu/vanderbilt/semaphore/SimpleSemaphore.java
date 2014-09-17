package edu.vanderbilt.semaphore;

import java.util.Vector;

/**
 * @class SimpleSemaphore
 *
 * @brief This class implements a simple counting semaphore that
 *        provides both "fair" and "non-fair" semaphore semantics,
 *        just liked Java Semaphores.  Students must implement this
 *        class using Java built-in monitor objects.  Fair semantics
 *        can be implemented via the Specific Notification pattern at
 *        www.dre.vanderbilt.edu/~schmidt/PDF/specific-notification.pdf.
 */
public class SimpleSemaphore implements ISemaphore {
    /**
     * Define a count of the number of available permits.
     */
    // @@ TODO - you fill in here.
	private volatile int mAvailablePermitsCount = 0;
	
    /**
     * Keep track of whether we need a fair or non-fair Semaphore.
     */
    // @@ TODO - you fill in here.
	private volatile boolean mFairSemaphore;
	
    /**
     * Keep track of the waiters in FIFO order if "mFair" is true.
     */
    // @@ TODO - you fill in here.
	private Vector<Object> waitersTrack = new Vector<Object>();

    /** 
     * Initialize the SimpleSemaphore.
     * @param Initialpermits Initial number of permits assigned to the
     *        semaphore, which can be < 0 
     * @parame Fair {@code true} if this lock should use a fair
     *         ordering policy.
     */
    public SimpleSemaphore(int initialPermits,
                           boolean fair) {
        // @@ TODO - you fill in here.
    	mAvailablePermitsCount = initialPermits;
    	mFairSemaphore = fair;
    }
	
    /**
     * Acquire one permit from the semaphore in a manner that can be
     * interrupted.
     */
    @Override
    public void acquire() throws InterruptedException {
        // @@ TODO - you fill in here.
    	if(mFairSemaphore){
    		Object snl = new Object();
    		int waitersTrackSize;
    		synchronized(snl){
    			synchronized(this){
    				
    				waitersTrack.add(snl);
    				waitersTrackSize = waitersTrack.size();
    			}
    			if (waitersTrackSize > mAvailablePermitsCount){
					snl.wait();
					}
    		}
    	}else{// the non-fair way
    		synchronized(this){
    		while(this.mAvailablePermitsCount <= 0){
    			wait();
    			}
    			mAvailablePermitsCount--;
    		}
    		
    	}
    }

    /**
     * Acquire one permit from the semaphore in a manner that cannot be
     * interrupted.
     */
    @Override
    public void acquireUninterruptibly() {
        	while (true) {
        		try{
        			acquire();
        			break;
        		}catch(InterruptedException e){
        		
        	}                	
        }
    }
	
    /**
     * Return one permit to the semaphore.
     */
    @Override
    public void release() {
        // @@ TODO - you fill in here.
    	if(mFairSemaphore){
    		waitersTrack.remove(0);
    		if(!waitersTrack.isEmpty()){
    			synchronized(waitersTrack.firstElement()){
    				waitersTrack.firstElement().notify();
    			}
    		}
    	}else{
    		synchronized(this){	
    			mAvailablePermitsCount++;
    			notify();
    		}
    	}
    }

    /**
     * Return the number of permits available.
     */
    @Override
    public int availablePermits() {
        // @@ TODO - you fill in here.
    	if(mFairSemaphore){
    		return mAvailablePermitsCount-waitersTrack.size();
    	}else{
    		return mAvailablePermitsCount;
    	}
    }
}
