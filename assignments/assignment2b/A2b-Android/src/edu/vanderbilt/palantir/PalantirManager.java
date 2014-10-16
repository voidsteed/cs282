package edu.vanderbilt.palantir;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;

import edu.vanderbilt.semaphore.SimpleSemaphore;

/**
 * @class PalantirManager
 *
 * @brief Uses a "fair" Semaphore to control access to the
 *        available Palantiri.  Implements the "Pooling" pattern
 *        in POSA3.
 */
public class PalantirManager {
    /**
     * Max number of Palantiri available.
     */
    private int mMaxPalantiri = 0;

    /**
     * Simple implementation of a Semaphore that can be configured
     * to use the "fair" policy.
     */
    private SimpleSemaphore mAvailable = null;

    /**
     * The Palantiri this class manages. Each palantir maps to a
     * boolean, which indicates whether or not the palantir is
     * currently in use.
     */
    protected HashMap<Palantir, Boolean> mPalantiri = null;
    
    /**
     * Create a local variable called free. 
     * It's used as a flag to indicate if the palantir is free.*/
    private final boolean notInUse = false;
    private final boolean inUse = true;

    /**
     * Create a resource manager for the palantiri passed as a
     * parameter.
     * 
     * Note: when instantiating the Semaphore, use "fair" semantics.
     */
    public PalantirManager(final List<Palantir> palantiri) {
    	// @@ TODO - You fill in here.
    	
    	//Set mMaxPalantiri to length of palantiri list
    	mMaxPalantiri = palantiri.size();
    	
    	// create mAvailable using fair semantic semaphore
    	mAvailable = new SimpleSemaphore(mMaxPalantiri,true);
    	
    	//Create a hashmap with Panlantir and boolean
    	//Loop through Hashmap and map panlantiri with notInUse(false) in default.
    	mPalantiri = new HashMap<Palantir, Boolean>();
    	for(Palantir p:palantiri){
    		mPalantiri.put(p, notInUse);
    	}
    }

    /**
     * Get the next available Palantir from the resource pool,
     * blocking until one is available.
     * 
     * @throws InterruptedException	 
     */
    public Palantir acquirePalantir() throws InterruptedException {
    	// @@ TODO - You fill in here (replacing return null);
    	//Acquire lock using semaphore
    	mAvailable.acquire();
    	//use a local method to get next available palantir

        // @@ Just say
        // return getNextAvailablePalantir();
    	// ##Checked
    	return getNextAvailablePalantir();
    }

    /**
     * Returns the designated @code palantir so that it's
     * available for others to use.
     */
    public void releasePalantir(final Palantir palantir) {
        // @@ TODO - You fill in here.
    	//Lock the palantiri list and 
    	//change the palantir's value to notInUse	
        // @@ This implementation is incorrect - call the markAsUnused() helper method:
    	// ## checked. using markAsUnused() to return the palantir to the pool
    	
    	boolean success = markAsUnused(palantir);
    	//release a semaphore to mAvailable
    	if(success){
    		mAvailable.release();
    	}else{
    		throw new RuntimeException("Unable to mark palantir to unused");
    	}
    }

    /**
     * Get the next available Palantir from the resource pool.  If
     * there are no available Palantir (which shouldn't happen),
     * return null.  This method can be called from multiple threads,
     * so make sure that any shared state is protected from race
     * conditions.
     */
    protected Palantir getNextAvailablePalantir() {
    	// @@ TODO - You fill in here (replacing return null);
    	// create a local variable for return
    	Palantir nextAvailablePalantir = null;
    	//lock the palantiri map
    	synchronized(mPalantiri){
    		//check if there is a free palantir

            // @@ You don't need to call containsValue() first:
    		// ## checked
    		// loop through the palantiri map
    		for(Entry<Palantir,Boolean> p : mPalantiri.entrySet()){
    			//Check one entry see if the palantir is in use
				if(p.getValue()==notInUse){
					//get this available palantir
					nextAvailablePalantir = p.getKey();
					//change this palantir status from noInUse to inUse
					p.setValue(inUse);
					//Find the palantir,so don't need to loop again, break it
					break;
				}
    		}
    		
    	}
    	return nextAvailablePalantir;
    }

    /**
     * Return the @code palantir back to the resource pool.  This
     * method can be called from multiple threads, so make sure that
     * any shared state is protected from race conditions.
     */
    
    protected boolean markAsUnused(final Palantir palantir) {
    	// @@ TODO - You fill in here (replacing return false);
    	//set a boolean flag for return
    	boolean markedUnused = false;
    	//Because multi-thread, so lock palantir HashMap
    	synchronized(mPalantiri){
    		// if the palantir is inUse(true)
    		if(mPalantiri.get(palantir)){
    			//change from inUse to notInUse
    			mPalantiri.put(palantir, notInUse);
    			//set the flag to be true
    			markedUnused = true;}
    		//else is just return false
    	}
    	
    	return markedUnused;
    }
    
    /**
     * Generate a list of palantiri with random gaze times between 1
     * and 5 milliseconds.
     * 
     * Each palantir's id will be its position in the list.
     */
    public static List<Palantir> generatePalantiri(int size) {
    	// Create a list to hold the generated Palantiri.
        List<Palantir> palantiri = new ArrayList<Palantir>();		

        // Create a new Random number generators.
        final Random rand = new Random();

        // Create and add each new Palantir into the list.
        for (int i = 0; i < size; ++i) {
            final int id = i;

            palantiri.add(new Palantir() {
                    @Override
                    public void gaze() throws InterruptedException {
                    	Thread.sleep(rand.nextInt(4000) + 1000);
                    }

                    @Override
                    public int getId() {
                        return id;
                    }
                });
        }
        
        return palantiri;
    }
}

