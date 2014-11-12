package edu.vuum.mocca;

import java.lang.ref.WeakReference;
import java.util.concurrent.CountDownLatch;

import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

/**
 * @class AndroidPlatformStrategy
 * 
 * @brief Provides methods that define a platform-independent API for output
 *        data to Android UI thread and synchronizing on thread completion in
 *        the ping/pong game. It plays the role of the "Concrete Strategy" in
 *        the Strategy pattern.
 */
public class AndroidPlatformStrategy extends PlatformStrategy {
	/**
	 * Latch to decrement each time a thread exits to control when the play()
	 * method returns.
	 */
	private CountDownLatch mLatch = null;

	/** Activity variable finds GUI widgets by view. */
	private final WeakReference<PingPongOutputInterface> mOuterClass;

	/** Defined a symbolic constants for latch count. */
	private final int LATCH_COUNT = 2;

	public AndroidPlatformStrategy(final Object output) {
		/** The current activity window (succinct or verbose). */
		mOuterClass = new WeakReference<PingPongOutputInterface>(
				(PingPongOutputInterface) output);
	}

	/** Do any initialization needed to start a new game. */
	public void begin() {
		/** (Re)initialize the CountDownLatch. */
		// TODO - You fill in here.
		// @@ Please don't use magic numbers, use symbolic constants instead:
		// ## checked!
		mLatch = new CountDownLatch(LATCH_COUNT);
	}

	/**
	 * Print the outputString to the display. Blocks for 0.5 seconds to let the
	 * user see what's going on.
	 */
	public void print(final String outputString) {
		// TODO - You fill in here.
		mOuterClass.get().print(outputString);
	}

	/** Indicate that a game thread has finished running. */
	public void done() {
        // TODO - You fill in here.
// @@ Please use runOnUiThread() so the countDown() method occurs in the UI Thread.
    	// ## checked! No need to change to runOnUiThread()
    	mLatch.countDown();
    }

	/** Barrier that waits for all the game threads to finish. */
	public void awaitDone() {
		// TODO - You fill in here.
		try {
			mLatch.await();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Error log formats the message and displays it for the debugging purposes.
	 */
	public void errorLog(String javaFile, String errorMessage) {
		Log.e(javaFile, errorMessage);
	}
}
