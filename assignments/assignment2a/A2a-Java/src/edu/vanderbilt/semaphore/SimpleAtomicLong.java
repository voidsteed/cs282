package edu.vanderbilt.semaphore;

// These imports are needed for students taking this class for
// graduate credit.
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.concurrent.locks.Lock;

// This import is needed for students taking this class for
// undergraduate credit.
import java.util.concurrent.atomic.AtomicLong;

/**
 * @class SimpleAtomicLong
 *
 * @brief This class implements a subset of the
 *        java.util.concurrent.atomic.SimpleAtomicLong class using a
 *        ReentrantReadWriteLock to illustrate how they work.
 *        Students taking this class for graduate credit must
 *        implement these methods using a Java ReentrantReadWriteLock.
 *        Students taking this class for undergraduate credit can
 *        simply create an instance of AtomicLong and forward all the
 *        SimpleAtomicLong methods to the corresponding AtomicLong
 *        methods.
 */
class SimpleAtomicLong {
    /**
     * The value that's manipulated atomically via the methods.
     */
    // TODO - add the mValue data member here.
	long mValue;

    /**
     * The ReentrantReadWriteLock used to serialize access to mValue.
     */
    // TODO - replace the null with the appropriate initialization:
	// ## changed to final
    private final ReentrantReadWriteLock mRWLock = new ReentrantReadWriteLock();
    // @@ Cache the read and write locks in data members.
    // ## Checked
    private final Lock rLock = mRWLock.readLock();
	private final Lock wLock = mRWLock.writeLock();

    /**
     * Creates a new SimpleAtomicLong with the given initial value.
     */
    public SimpleAtomicLong(long initialValue) {
        // TODO - you fill in here
    	mValue = initialValue;
    }

    /**
     * @brief Gets the current value
     * 
     * @returns The current value
     */
    public long get() {
        // TODO - you fill in here
    	try{
    		mRWLock.readLock().lock();
    		return mValue;
    		}
    	finally
    	{
    		mRWLock.readLock().unlock();
    	}
    }

    /**
     * @brief Atomically decrements by one the current value
     *
     * @returns the updated value
     */
    public long decrementAndGet() {
        // TODO - you fill in here
    	try{
    		mRWLock.writeLock().lock();
    		mValue--;
    		return mValue;
    	}
    	finally{
    		mRWLock.writeLock().unlock();
    	}
    }

    /**
     * @brief Atomically increments by one the current value
     *
     * @returns the previous value
     */
    public long getAndIncrement() {
        // TODO - you fill in here
    	try{
    		long preValue = mValue;
    		mRWLock.writeLock().lock();
    		mValue++;
    		return preValue;
    	}
    	finally{
    		mRWLock.writeLock().unlock();
    	}
    }

    /**
     * @brief Atomically decrements by one the current value
     *
     * @returns the previous value
     */
    public long getAndDecrement() {
        // TODO - you fill in here
    	try{
            // @@ Yikes, this read isn't protected by a lock!
    		// ## put preValue inside writelock
    		mRWLock.writeLock().lock();
    		long preValue = mValue;
    		mValue--;
    		return preValue;
    	}
    	finally{
    		mRWLock.writeLock().unlock();
    	}
    }

    /**
     * @brief Atomically increments by one the current value
     *
     * @returns the updated value
     */
    public long incrementAndGet() {
        // TODO - you fill in here
    	try{
    		mRWLock.writeLock().lock();
    		mValue++;
    		return mValue;
    	}
    	finally{
    		mRWLock.writeLock().unlock();
    	}
    }
}

