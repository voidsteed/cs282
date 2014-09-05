package edu.vanderbilt.mapapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.content.Context;

import android.widget.EditText;
import android.widget.Toast;

/**
 * @class MapDemoActivity
 *
 * @brief Main Activity of Assignment 1.  Allows the user to input a
 *        latitude and longitude.  When the "Show Location" button
 *        is clicked, an Activity is launched to display the location on
 *        a map.
 */
public class MapAppActivity extends LifecycleLoggingActivity {
    // Helpful constants for range checking.
    private static final double LATITUDE_MIN = -90.0;
    private static final double LATITUDE_MAX = 90.0;
    private static final double LONGITUDE_MIN = -180.0;
    private static final double LONGITUDE_MAX = 180.0;

    // Latitude and longitude for Vanderbilt University.
    private static final String DEFAULT_LATITUDE = "36.1486";
    private static final String DEFAULT_LONGITUDE = "-86.8050";

    // References to Views we will use.
    private EditText mLatitude;
    private EditText mLongitude;
    
    /**
     * Lifecycle hook method called when the Activity starts. 
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set the default layout.
        setContentView(R.layout.map_app_main);
        
        // We will need to get the values out of the EditText boxes,
        // so look them up and cache them.
        // @@ TODO: you fill in here.
        mLatitude = (EditText) findViewById(R.id.latitude_edit);
        mLongitude = (EditText) findViewById(R.id.longitude_edit);
    }

    /**
     * Called when the user clicks the "Show Location" button.
     */
    public void showLocation (View v) {
        // Get the strings that the user has input.
    	String latitudeString = mLatitude.getText ().toString ();
    	String longitudeString = mLongitude.getText ().toString ();

        // Hide the keyboard.
        hideKeyboard();

        // Update the defaults if necessary.
        // @@ TODO: grad students fill in here.
        if (latitudeString.equals("") && longitudeString.equals("")){
        	latitudeString = DEFAULT_LATITUDE;
        	longitudeString = DEFAULT_LONGITUDE;
        }
        // Handle when either latitude or longitude is empty
        if (latitudeString.equals("") || longitudeString.equals("")){
        	showToast(getString(R.string.emptyInputWarning));
        	return;
        }

        // Convert the Strings to floats.
        final float latitude = Float.parseFloat(latitudeString);
        final float longitude = Float.parseFloat(longitudeString);
        		
        // If the input doesn't validate, just return and do nothing
        // (a toast telling them what the error is will have already
        // been displayed).
        if (invalidInput(latitude,
                         longitude))
            return;

        // Launch the activity by sending an intent.  Android will
        // choose the right one or let the user choose if more than
        // one Activity can handle it.
        // @@ TODO: you fill in here.
        // @@ TODO: grad students must support both "Maps" and "Browser" apps.
        // ## Check! Only make geoIntent at this time
        Intent geoIntent = makeGeoIntent(latitude,longitude);
        if (geoIntent.resolveActivity(getPackageManager()) != null) {
            startActivity(geoIntent);
        }
        else{
        	//Handle open with browser app
        	Intent mapsIntent = makeMapsIntent(latitude,longitude);
        	if (mapsIntent.resolveActivity(getPackageManager()) != null){
        		startActivity(mapsIntent);
        	}
        	//Handle no app could open exception
        	else{
            	showToast(getString(R.string.noAppInput));
            }
        }
    }

    /**
     *A method to make and show the toast by passing a string
     */
    private void showToast(String string){
    	Context context = getApplicationContext();
    	CharSequence input = string;
    	int duration = Toast.LENGTH_SHORT;
    	Toast toast = Toast.makeText(context, input, duration);
    	toast.show();
    }
    
    /**
     * Factory method that returns an Intent that designates the "Map"
     * app.
     */
    private Intent makeGeoIntent(final float latitude,
                                 final float longitude) {
        // @@ TODO: you fill in here, replacing null;
    	// create a uri for Intent
    	String uri = "geo:"+ latitude + "," + longitude;
        return new Intent(Intent.ACTION_VIEW,Uri.parse(uri));
    }

    /**
     * Factory method that returns an Intent that designates the
     * "Browser" app.
     */
    private Intent makeMapsIntent(final float latitude,
                                  final float longitude) {
        // @@ TODO: you fill in here, replacing null;
    	// create a url for Intent
    	String url = "http://maps.google.com/maps?q=" + latitude + "," + longitude;
        return new Intent(Intent.ACTION_VIEW,Uri.parse(url));
    }

    /**
     * Returns true if the input are invalid latitude and longitude.
     * If either fails to validate, a toast describing the problem is
     * also displayed.
     */
    private boolean invalidInput(float latitude,
                                 float longitude) {
        // The XML config for the views constrains them to be signed
        // reals.  Check for valid ranges for latitude and longitude:
        // latitude: [-90, 90] 
        // longitude: [-180, 180]
    	// @@ TODO: you fill in here
    	// ## check!
    	if( ((latitude >= LATITUDE_MIN) && (latitude <= LATITUDE_MAX) 
    			&& (longitude >= LONGITUDE_MIN) && (longitude <= LONGITUDE_MAX)))
    		return false;
    	else{
    		showToast(getString(R.string.invalidInputWarning));
    		return true;
    		}
    }

    /**
     * Hide the keyboard after a user has finished typing the acronym
     * they want expanded.
     */
    protected void hideKeyboard() {
        InputMethodManager mgr =
            (InputMethodManager) getSystemService
            (Context.INPUT_METHOD_SERVICE);
        mgr.hideSoftInputFromWindow(mLatitude.getWindowToken(),
                                    0);
    }
}
