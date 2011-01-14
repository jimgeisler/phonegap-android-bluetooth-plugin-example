package com.phonegap.BluetoothPlugin;

import android.os.Bundle;
import com.phonegap.*;

/**
 * BluetoothPlugin PhoneGap application.
 * @author jamesgeisler
 */
public class BluetoothPlugin extends DroidGap
{
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        super.loadUrl("file:///android_asset/www/index.html");
    }
}