package com.peoples.android.activities;

//import java.util.List;

import android.app.Activity;
//import android.content.Context;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.peoples.android.Peoples;
import com.peoples.android.R;
//import com.peoples.android.model.Survey;
//import com.peoples.android.processTest.LocationTestActivity;
//import com.peoples.android.server.Pull;
//import com.peoples.android.services.BootService;
import com.peoples.android.model.Survey;
import com.peoples.android.services.CoordinatorService;
//import com.peoples.android.services.GPSLocationService;



public class Menu extends Activity {

	// Debugging
	// TEST
	//TEST
    private static final String TAG = "Menu";
    private static final boolean D = true;
    //private Survey survey;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final Context panda = this;

        if(D) Log.e(TAG, "+++ ON CREATE menu activity +++");

        setContentView(R.layout.main);

        
        //NEW: let the coordinator service take care of sync stuff
        //Intent coordinatorIntent			= new Intent(this, CoordinatorService.class);
        //TODO: write a more proper Action, or use provided ones
    	//coordinatorIntent.setAction(MainActivity.class.getName());
        //startService(coordinatorIntent);
        
//        Next call is done by process, though for now unsure that this class
//        will see changes immediately
        
//        Log.d(TAG, "Fetching surveys");
//        Pull.syncWithWeb(this);

        final TextView q = (TextView) this.findViewById(R.id.title);
        q.setText("Welcome to PEOPLES!");
        
        Button sample = (Button) findViewById(R.id.sample);
        sample.setText("Click here to take a sample survey");
        sample.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                //survey = new Survey(3, panda);
                //Bundle bundle = new Bundle();
                //bundle.putSerializable("survey", survey);
                
                Intent myIntent = new Intent(view.getContext(), Peoples.class);
                //myIntent.putExtras(bundle);
                startActivityForResult(myIntent, 0);
                
            }

        });
        
        Button settings = (Button) findViewById(R.id.settings);
        settings.setText("Settings");
        settings.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                //should be generated dynamically attn:Austin
                //perhaps we need a database call to determine this?
                bundle.putBoolean("one", true);
                bundle.putBoolean("two", true);
                bundle.putBoolean("three", false);
                
                Intent myIntent = new Intent(view.getContext(), PeoplesSettings.class);
                myIntent.putExtras(bundle);
                startActivityForResult(myIntent, 0);
            	
            }
        });
        
        Button phoneID = (Button) findViewById(R.id.uniqueID);
        phoneID.setText("Find this phone's unique ID");
        phoneID.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
            	//panda.getSystemService(Context.TELEPHONY_SERVICE);
            	TelephonyManager tManager = (TelephonyManager)panda.getSystemService(Context.TELEPHONY_SERVICE);
            	String uid = tManager.getDeviceId();
            	
            	Toast.makeText(getApplicationContext(), uid,
                        Toast.LENGTH_SHORT).show();

            }
        });
        
        Button quit = (Button) findViewById(R.id.quit);
        quit.setText("Exit Peoples");
        quit.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
            	//panda.getSystemService(Context.TELEPHONY_SERVICE);
            	/*TelephonyManager tManager = (TelephonyManager)panda.getSystemService(Context.TELEPHONY_SERVICE);
            	String uid = tManager.getDeviceId();
            	
            	Toast.makeText(getApplicationContext(), uid,
                        Toast.LENGTH_SHORT).show();*/
            	Toast.makeText(getApplicationContext(), "Thank you for shopping at Wal-mart",
                        Toast.LENGTH_SHORT).show();
            	finish();
            }
        });
        
        
        //TODO: CANT FIND SYNC VIEW!
        
//        Button sync = (Button) findViewById(R.id.Sync);
//        sync.setText("Sync the surveys manually, to update deploy time or questions");
//        sync.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View view) {
//                Intent myIntent = new Intent(view.getContext(), LocationTestActivity.class);
//                startActivityForResult(myIntent, 0);
//            }
//        });

        //This is just code to test GPS location gathering and persisting to database
        //starts the Activity I"m interested in testing w/o waiting for button handler
        //Context context = this;
        //Intent gpsIntent = new Intent(context, LocationTestActivity.class);
        //gpsIntent = gpsIntent.setClass(context, LocationTestActivity.class);
        //context.startActivity(gpsIntent);

    }
    
    
    /**
     * Called after your activity has been stopped, prior to it being started again.
     */
    @Override
    protected void onRestart() {
    	// TODO Auto-generated method stub
    	super.onRestart();
    }
    
    /**
     * Called when the activity is becoming visible to the user.
     */
    @Override
    protected void onStart() {
    	// TODO Auto-generated method stub
    	super.onStart();
    }
    
    /**
     * Called when the activity will start interacting with the user.
     */
    @Override
    protected void onResume() {
    	// TODO Auto-generated method stub
    	super.onResume();
    }
    
    /**
     * Called when the system is about to start resuming a previous activity. 
     */
    @Override
    protected void onPause() {
    	// TODO Auto-generated method stub
    	super.onPause();
    }
    
    /**
     * Called when the activity is no longer visible to the user, because
     * another activity has been resumed and is covering this one.
     */
    @Override
    protected void onStop() {
    	// TODO Auto-generated method stub
    	super.onStop();
    }
    
    /**
     * The final call you receive before your activity is destroyed.
     */
    @Override
    protected void onDestroy() {
    	// TODO Auto-generated method stub
    	super.onDestroy();
    }

}