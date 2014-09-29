package edu.vanderbilt.semaphore;

import java.util.ArrayList;

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

        // @@ Don't allocate the vector unless mFair is true.

	private ArrayList<Object> waitersQ = new ArrayList<Object>();

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
    		Object waitLock = new Object();
    		//boolean mustWait;
    		synchronized(waitLock){
    			synchronized(this){
        // @@ You're not handling the mAvailPermitCount properly here
        // - you're *always* adding it to the Vector, even if there's
        // no need to wait.
    				if(mAvailablePermitsCount <= 0){
    					waitersQ.add(waitLock);
    				}
    				else{
    					--mAvailablePermitsCount;
    					return;
    				}
    			}
    				try{
    					waitLock.wait();
    				} catch(InterruptedException e) { 
    					synchronized(this){
    						waitersQ.remove(this);
    						throw new InterruptedException("thread is interrupted.");
    					}
    				}
    			}

        // @@ This check needs to be done in a synchronized block to avoid race conditions.
  
    	}else{// the non-fair way
    		synchronized(this){
    		while(this.mAvailablePermitsCount <= 0){
    			wait();
    			}
    			--mAvailablePermitsCount;
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
        // @@ This needs to be done in a synchronized block to avoid
        // race conditions.  However, you're also removing the very
        // element that you need to be notifying..
    		synchronized(this){
    			mAvailablePermitsCount++;
    			if(!waitersQ.isEmpty()){
    				Object next = waitersQ.remove(0);
    				--mAvailablePermitsCount;
    				synchronized(next){
        				next.notify();
        			}
    			}	
    		}
        // @@ You're missing some important things(s) here needed to avoid race conditions.
    	// ## checked!
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
        // @@ This needs to be done in a synchronized block to avoid race conditions.
    	// ## added to synchronized (this)	
    		synchronized(this){
    			return mAvailablePermitsCount - waitersQ.size();
    		}
    	}else{
    		return mAvailablePermitsCount;
    	}
    }
}
