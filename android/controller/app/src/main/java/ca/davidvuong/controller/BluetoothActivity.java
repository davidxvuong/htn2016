package ca.davidvuong.controller;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Set;

public class BluetoothActivity extends AppCompatActivity implements ProcessFinish {
    private final int REQUEST_ENABLE_BT = 1;
    private final String DEVICE_NAME = "HC-06";
    private BluetoothAdapter mBluetoothAdapter;
    private DeviceController deviceController;
    private boolean fan_1_toggle = false;
    private boolean fan_2_toggle = false;
    private boolean humidifier_toggle = false;
    private boolean light_toggle = false;
    private Button btnLight, btnFan1, btnFan2, btnHumidifier;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnFan1 = (Button)findViewById(R.id.btnFan1);
        btnFan2 = (Button)findViewById(R.id.btnFan2);
        btnLight = (Button)findViewById(R.id.btnLight);
        btnHumidifier = (Button)findViewById(R.id.btnHumidifier);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_ENABLE_BT) {
            if (resultCode == RESULT_OK) {

                Toast.makeText(getApplicationContext(), "Working", Toast.LENGTH_SHORT);
                getRecognizedDevices();
            }
            else {
                Toast.makeText(getApplicationContext(), "Not Working", Toast.LENGTH_SHORT);
            }
        }
    }

    private void getRecognizedDevices() {
        Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();
        ArrayList<String> devices = new ArrayList<>();

        // If there are paired devices
        if (pairedDevices.size() > 0) {
            // Loop through paired devices
            for (BluetoothDevice device : pairedDevices) {
                // Add the name and address to an array adapter to show in a ListView
                devices.add(device.getName() + "\n" + device.getAddress());

                if (DEVICE_NAME.equals(device.getName())) {
                    connectToDevice(device);
                }
            }
        }

        else {
            Toast.makeText(getApplicationContext(), "Cannot find device.", Toast.LENGTH_SHORT);
        }

    }

    private void connectToDevice(BluetoothDevice device) {
        ConnectDevice obj = new ConnectDevice(device.getName(), device.getAddress(), getApplicationContext(), this);
        obj.execute();
    }

    public void processFinish(BluetoothSocket socket) {
        deviceController = new DeviceController(socket, getApplicationContext());
    }

    public void btnConnect_Click(View view) {
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBluetoothAdapter == null) {
            //no bluetooth
            finish();
        }
        else {
            if (!mBluetoothAdapter.isEnabled()) {
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
            }
            else {
                getRecognizedDevices();
            }
        }
    }

    public void btnSend_Click(View view) {
//        EditText txtInput = (EditText)findViewById(R.id.txtInput);
//        String input = txtInput.getText().toString();
//        deviceController.send(input);
    }

    private void send(String operation) {
        deviceController.send(operation);
    }

    public void btnDisconnect_Click(View view) {
        deviceController.close();
    }

    public void btnLight_Click(View view) {
        if (light_toggle == false) {
            light_toggle = true;
            send(Operations.LIGHT);
            btnLight.setText("Turn off light");
        }
        else {
            light_toggle = false;
            send(Operations.LIGHT);

            btnLight.setText("Turn on light");
        }
    }

    public void btnHumidifier_Click(View view) {
        if (humidifier_toggle == false) {
            humidifier_toggle = true;
            send(Operations.HUMIDIFIER);

            btnHumidifier.setText("Turn off humidifier");
        }
        else {
            humidifier_toggle = false;
            send(Operations.HUMIDIFIER);

            btnHumidifier.setText("Turn on humidifier");
        }
    }

    public void btnFan2_Click(View view) {
        if (fan_2_toggle == false) {
            fan_2_toggle = true;

            send(Operations.FAN_2_PELTER);

            btnFan2.setText("Turn off Fan 2");
        }
        else {
            fan_2_toggle = false;
            send(Operations.FAN_2_PELTER);

            btnFan2.setText("Turn on Fan 2");
        }
    }

    public void btnFan1_Click(View view) {
        if (fan_1_toggle == false) {
            fan_1_toggle = true;
            send(Operations.FAN_1);

            btnFan1.setText("Turn off Fan 1");
        }
        else {
            fan_1_toggle = false;
            send(Operations.FAN_1);

            btnFan1.setText("Turn on Fan 1");
        }
    }
}
