/*---------------------------------------------------------------------------*
 * BootIntentReceiver.java                                                   *
 *                                                                           *
 * Starts/schedules the various services when the phone is started.          *
 *---------------------------------------------------------------------------*
 * Copyright 2011 Sema Berkiten, Vladimir Costescu, Henry Liu, Diego Vargas, *
 * Austin Walker, and Tony Xiao                                              *
 *                                                                           *
 * This file is part of Survey Droid.                                        *
 *                                                                           *
 * Survey Droid is free software: you can redistribute it and/or modify      *
 * it under the terms of the GNU General Public License as published by      *
 * the Free Software Foundation, either version 3 of the License, or         *
 * (at your option) any later version.                                       *
 *                                                                           *
 * Survey Droid is distributed in the hope that it will be useful,           *
 * but WITHOUT ANY WARRANTY; without even the implied warranty of            *
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the             *
 * GNU General Public License for more details.                              *
 *                                                                           *
 * You should have received a copy of the GNU General Public License         *
 * along with Survey Droid.  If not, see <http://www.gnu.org/licenses/>.     *
 *****************************************************************************/
package org.surveydroid.android;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;

import org.surveydroid.android.coms.ComsService;
import org.surveydroid.android.survey.SurveyScheduler;

/**
 * Starts the background Survey Droid services after at boot time.
 * 
 * @author Austin Walker
 */
public class BootIntentReceiver extends BroadcastReceiver
{
	protected static final String TAG = "BootIntentReceiver";
	
    @Override
    public void onReceive(Context context, Intent intent)
    {
    	if (!intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED))
    	{
    		if (Config.D)
    		{
    			throw new RuntimeException("Wrong action: "
    				+ intent.getAction());
    		}
    		else
    		{
    			Util.w(null, TAG, "Wrong action: " + intent.getAction());
    			return;
    		}
    	}
    	/*
    	 * Here, we delay the startup of the application for a while.  The
    	 * main reason for this is to allow the device that the app is running
    	 * on to update it's clock.  It also avoids problems where required
    	 * system services have not been started up in time.
    	 */
    	final Context c = context;
    	Runnable r = new Runnable()
    	{
			@Override
			public void run()
			{
				startup(c);
			}
    	};
    	Handler h = new Handler();
    	h.postDelayed(r, 10 * 1000); //delay 10 seconds
    }
    
    //TODO should this be private?
    /**
     * Starts up the basic Survey Droid services.
     * 
     * @param context - the {@link Context} given to the receiver
     */
    public static void startup(final Context context)
    {
    	Util.i(null, TAG, "+++Starting Survey Droid+++");
        
        //start the coms service pulling
        Util.d(null, TAG, "Starting pull service");
        final Intent comsPullIntent = new Intent(context, ComsService.class);
        comsPullIntent.setAction(ComsService.ACTION_DOWNLOAD_DATA);
        comsPullIntent.putExtra(ComsService.EXTRA_RUNNING_TIME,
        		System.currentTimeMillis());
        comsPullIntent.putExtra(ComsService.EXTRA_REPEATING, true);
        context.startService(comsPullIntent);
    	
        /*
         * Delay other things for a bit so that pull can complete first.  This
         * way, if some feature is disabled on the server, it won't even be
         * started.
         */
        Runnable r = new Runnable()
    	{
			@Override
	        public void run()
	        {
		        //start call monitoring
		        Util.d(null, TAG, "Starting call monitoring");
		        TelephonyManager tm = (TelephonyManager)
		        	context.getSystemService(Context.TELEPHONY_SERVICE);
		        tm.listen(new CallTracker(context),
		        		PhoneStateListener.LISTEN_CALL_STATE);
		        
		        //start location tracking
		        Util.d(null, TAG, "Starting location tracking");
		        Intent trackingIntent =
		        	new Intent(context, LocationTrackerService.class);
		        trackingIntent.setAction(
		        		LocationTrackerService.ACTION_START_TRACKING);
		        context.startService(trackingIntent);
		        
		        //start the coms service pushing
		        Util.d(null, TAG, "Starting push service");
		        Intent comsPushIntent = new Intent(context, ComsService.class);
		        comsPushIntent.setAction(ComsService.ACTION_UPLOAD_DATA);
		        comsPullIntent.putExtra(ComsService.EXTRA_RUNNING_TIME,
		        		System.currentTimeMillis());
		        comsPushIntent.putExtra(ComsService.EXTRA_REPEATING, true);
		        context.startService(comsPushIntent);
		        
		    	//start the survey scheduler
		    	Util.d(null, TAG, "Starting survey scheduler");
		    	Intent schedulerIntent = new Intent(context,
		    			SurveyScheduler.class);
		    	schedulerIntent.setAction(
		    			SurveyScheduler.ACTION_SCHEDULE_SURVEYS);
		    	schedulerIntent.putExtra(SurveyScheduler.EXTRA_RUNNING_TIME,
		    			System.currentTimeMillis());
		        context.startService(schedulerIntent);
	        }
    	};
    	Handler h = new Handler();
    	h.postDelayed(r, 10 * 1000);
    }
}