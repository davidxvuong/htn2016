package ca.davidvuong.controller;

import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.widget.Toast;

/**
 * Created by david on 17/09/16.
 */
public class DeviceController  {
    private BluetoothSocket socket;
    private Context context;

    public DeviceController(BluetoothSocket socket, Context context) {
        this.socket = socket;
        this.context =context;
    }


    public void send(String input) {
        try {
            socket.getOutputStream().write(input.getBytes());
        }
        catch (Exception ex) {
            Toast.makeText(context, "Unable to send operation to Arduino: " + ex.getMessage(), Toast.LENGTH_SHORT);
        }
    }

    public void close() {
        try {
            socket.close();
        }
        catch (Exception ex) {
            Toast.makeText(context, "Unable to close connection: " + ex.getMessage(), Toast.LENGTH_SHORT);
        }
    }
}
