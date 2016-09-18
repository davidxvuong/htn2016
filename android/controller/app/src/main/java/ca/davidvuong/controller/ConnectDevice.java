package ca.davidvuong.controller;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import java.io.IOException;
import java.util.UUID;

/**
 * Created by david on 17/09/16.
 */
public class ConnectDevice extends AsyncTask<Void, Void, Void>  // UI thread
{
    private boolean ConnectSuccess = true; //if it's here, it's almost connected
    private String address;
    private String name;
    private boolean deviceConnected = false;
    private BluetoothSocket btSocket;
    private BluetoothAdapter mBluetoothAdapter;
    private Context context;
    private BluetoothActivity btActivity;


    static final UUID myUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    public ConnectDevice(String name, String address, Context context, BluetoothActivity btActivity) {
        this.address = address;
        this.name = name;
        this.context = context;
        this.btActivity = btActivity;
    }

    @Override
    protected void onPreExecute()
    {
        Toast.makeText(context, "Connecting...", Toast.LENGTH_SHORT);
    }

    @Override
    protected Void doInBackground(Void... devices) //while the progress dialog is shown, the connection is done in background
    {
        try
        {
            if (btSocket == null || !deviceConnected)
            {
                mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();//get the mobile bluetooth device
                BluetoothDevice remoteDevice = mBluetoothAdapter.getRemoteDevice(address);//connects to the device's address and checks if it's available
                btSocket = remoteDevice.createInsecureRfcommSocketToServiceRecord(myUUID);//create a RFCOMM (SPP) connection
                BluetoothAdapter.getDefaultAdapter().cancelDiscovery();
                btSocket.connect();//start connection
            }
        }
        catch (IOException e)
        {
            ConnectSuccess = false;//if the try failed, you can check the exception here
        }
        return null;
    }
    @Override
    protected void onPostExecute(Void result) //after the doInBackground, it checks if everything went fine
    {
        super.onPostExecute(result);

        if (!ConnectSuccess)
        {
            Toast.makeText(context, "Could not connect to " + name, Toast.LENGTH_SHORT);
        }
        else
        {
            Toast.makeText(context, "Connected to " + name, Toast.LENGTH_SHORT);
            System.out.println("done");
            deviceConnected = true;

            btActivity.processFinish(btSocket);
        }
    }
}