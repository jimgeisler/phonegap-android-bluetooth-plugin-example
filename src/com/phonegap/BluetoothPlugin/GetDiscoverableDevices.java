package com.phonegap.BluetoothPlugin;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.phonegap.api.Plugin;
import com.phonegap.model.Device;

/**
 * Manages a list of discoverable devices.
 * Bluetooth discovery is done in a separate thread as to not block the UI thread.
 * 
 * Communication is facilitated through the provided handler.
 * 
 * @author jamesgeisler
 */
public class GetDiscoverableDevices {
	
	public static final int DONE_GATHERING_DEVICES = 1;
	public static final int DONE_LOOKING_FOR_DEVICES = 2;
	
	private static final String TAG = "BluetoothChat";
	
	private static Plugin _activity;
	private Exception ex;
	public static List<Device> devices;
	private boolean unregistered;
	
    // Local Bluetooth adapter
    private BluetoothAdapter mBluetoothAdapter = null;
    
    public Handler mHandler;

	public void execute(Plugin activity) {

	  _activity = activity;
	  unregistered = false;
	  devices = new ArrayList<Device>();
	  
	  Looper.prepare();
	  
	  mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

	  Thread t = new Thread() {
	    public void run() {
	    	_doInBackground();
	    	Log.e(TAG, "+++ Background method is complete +++");
	        //uiThreadCallback.post(runInUIThread);
	    	mHandler.sendEmptyMessage(DONE_GATHERING_DEVICES);
	    	Log.e(TAG, "+++ Message should have been sent +++");
	    	startLookingForDevices();
	    	
	    }
	  };
	  t.start();

	}
	
	private void _doInBackground() {
		
		try {
			// do stuff
	        Log.e(TAG, "+++ Do in the background +++");

	        // If the adapter is null, then Bluetooth is not supported
	        if (mBluetoothAdapter == null) {
	        	Log.e(TAG, "+++ BT Adapter is null +++");
	            return;
	        }
			
	        gatherExistingDevices();
	        
		} catch (Exception e) {
			Log.e(TAG, "+++ Exception thrown +++");
			ex = e;
			Log.e(TAG, ex.getMessage());
		}
	}
	
    
    /**
     * Get any bonded devices and add them to the list.
     */
    private void gatherExistingDevices() {
    	
    	Log.e(TAG, "+++ Gathering devices +++");
    	
    	Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();
    	// If there are paired devices
    	if (pairedDevices.size() > 0) {
    	    // Loop through paired devices
    	    for (BluetoothDevice device : pairedDevices) {
    	        // Add the name and address to an array adapter to show in a ListView
    	    	devices.add(new Device(device.getName(), device.getAddress()));
    	    }
    	}
    	
    	Log.e(TAG, "+++ Done gathering +++");
    }

    /**
     * Creates intent filter and registers receiver for discovering devices.
     * Begins device discovery.
     */
    private void startLookingForDevices() {
    	// Register the BroadcastReceiver
    	IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
    	filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
    	_activity.ctx.registerReceiver(mReceiver, filter); // Don't forget to unregister during onDestroy
    	
    	mBluetoothAdapter.startDiscovery();
    }
    
    // Create a BroadcastReceiver for ACTION_FOUND and ACTION_DISCOVERY_FINISHED
    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            // When discovery finds a device
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                // Get the BluetoothDevice object from the Intent
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                // Add the name and address to an array adapter to show in a ListView
                if (device != null && device.getName() != null && device.getAddress() != null) 
                	devices.add(new Device(device.getName(), device.getAddress()));
            } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
            	if (!unregistered) {
	            	mBluetoothAdapter.cancelDiscovery();
	            	_activity.ctx.unregisterReceiver(mReceiver);
	            	mHandler.sendEmptyMessage(DONE_LOOKING_FOR_DEVICES);
	            	unregistered = true;
            	}
            }
        }
    };

}
