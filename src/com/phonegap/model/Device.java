package com.phonegap.model;

import android.os.Parcel;
import  android.os.Parcelable;
import android.util.Log;

public class Device extends Object implements Parcelable {
	
	private static final String TAG = "DEVICE_MODEL";
	private String mac;
	private String name;
	
	/**
	 * Constructor.
	 */
	public Device() {
	}
	
	/**
	 * Constructor.
	 * @param mac - device's bt mac address
	 * @param name - device's friendly discoverable name
	 */
	public Device(String mac, String name) {
		this.mac = mac;
		this.name = name;
	}
	
	/**
	 * Parcel constructor.
	 * @param source - marshalled parcel.
	 */
	public Device(Parcel source) {
        Log.v(TAG, "Device(Parcel source): time to put back parcel data");
        name = source.readString();
        mac = source.readString();
	}

	public String getMac() {
		return mac;
	}

	public void setMac(String mac) {
		this.mac = mac;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public int describeContents() {
		return this.hashCode();
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		Log.v(TAG, "writeToParcel..."+ flags);
		dest.writeString(name);
		dest.writeString(mac);
	}
	public static final Parcelable.Creator<Device> CREATOR = new Parcelable.Creator<Device>() {
		public Device createFromParcel(Parcel in) {
			return new Device(in);
		}

		public Device[] newArray(int size) {
		    return new Device[size];
		}
	};
	

}
