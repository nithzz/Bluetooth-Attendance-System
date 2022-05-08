package com.androidtut.qaifi.bluetoothchatapp;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import org.json.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.stream.StreamSupport;

public class DeviceListActivity extends AppCompatActivity {
    private ListView listPairedDevices, listAvailableDevices;
    private ProgressBar progressScanDevices;

    private ArrayAdapter<String> adapterPairedDevices, adapterAvailableDevices;
    private Context context;
    private BluetoothAdapter bluetoothAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_list);
        context = this;
        Button submit = findViewById(R.id.submit);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String attendance= "{ 'attendance': [   {     'roll_number': '20pc24',     'name': 'Nithiya Shri',     'mac': '4C:F2:02:20:5A:4A'   },   {     'roll_number': '20pc35',     'name': 'Varun',     'mac': '98:B8:BC:B2:06:35'   },   {     'roll_number': '20pc22',     'name': 'Navin krishna',     'mac': '04:BD:BF:AC:FD:F2'   } ]}";
                JSONObject obj = null;
                try {
                    obj = new JSONObject(attendance);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                JSONArray arr = null;
                try {
                    arr = obj.getJSONArray("attendance");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                for (int i = 0; i < arr.length(); i++)
                {
                    String post_id = null;
                    try {
                        post_id = arr.getJSONObject(i).getString("mac");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    String post_roll = null;
                    try {
                        post_roll = arr.getJSONObject(i).getString("roll_number");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    HashMap<String,Integer> map1 = new HashMap<>();
                    map1.put("20pc22",R.id.check_20pc22);
                    map1.put("20pc24",R.id.check_20pc24);
                    map1.put("20pc35",R.id.check_20pc35);
                    if(mac.contains(post_id)){
                        String temp = post_roll;
                        CheckBox temp1 = findViewById(map1.get(temp));
                        temp1.setChecked(true);
                    }
                }

                ListView btn = findViewById(R.id.list_available_devices);
                btn.setVisibility(View.GONE);
                Button btn1 = findViewById(R.id.submit);
                btn1.setVisibility(View.GONE);
                CheckBox ch1 = findViewById(R.id.check_20pc22);
                CheckBox ch2 = findViewById(R.id.check_20pc24);
                CheckBox ch3 = findViewById(R.id.check_20pc35);
                ch1.setVisibility(View.VISIBLE);
                ch2.setVisibility(View.VISIBLE);
                ch3.setVisibility(View.VISIBLE);
//                Intent intent = new Intent(DeviceListActivity.this,attendanceListFinal.class);
//                startActivity(intent);



            }
        });
        init();
    }

    private void init() {
        listPairedDevices = findViewById(R.id.list_paired_devices);
        listAvailableDevices = findViewById(R.id.list_available_devices);
        progressScanDevices = findViewById(R.id.progress_scan_devices);

        adapterPairedDevices = new ArrayAdapter<String>(context, R.layout.device_list_item);
        adapterAvailableDevices = new ArrayAdapter<String>(context, R.layout.device_list_item);

        listPairedDevices.setAdapter(adapterPairedDevices);
        listAvailableDevices.setAdapter(adapterAvailableDevices);

        listAvailableDevices.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String info = ((TextView) view).getText().toString();
                String address = info.substring(info.length() - 17);

                Intent intent = new Intent();
                intent.putExtra("deviceAddress", address);
                setResult(RESULT_OK, intent);
                finish();
            }
        });

        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        Set<BluetoothDevice> pairedDevices = bluetoothAdapter.getBondedDevices();

//        if (pairedDevices != null && pairedDevices.size() > 0) {
//            for (BluetoothDevice device : pairedDevices) {
//                adapterPairedDevices.add(device.getName() + "\n" + device.getAddress());
//            }
//        }

        IntentFilter intentFilter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        registerReceiver(bluetoothDeviceListener, intentFilter);
        IntentFilter intentFilter1 = new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        registerReceiver(bluetoothDeviceListener, intentFilter1);

        listPairedDevices.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                bluetoothAdapter.cancelDiscovery();

                String info = ((TextView) view).getText().toString();
                String address = info.substring(info.length() - 17);

                Log.d("Address", address);

                Intent intent = new Intent();
                intent.putExtra("deviceAddress", address);

                setResult(Activity.RESULT_OK, intent);
                finish();
            }
        });
    }
    List<String> list1 = new ArrayList<String>();
    List<String> mac = new ArrayList<String>();
    private BroadcastReceiver bluetoothDeviceListener = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                if (device.getBondState() != BluetoothDevice.BOND_BONDED) {
                    if (device.getName() != null) {
                        if (!list1.contains(device.getName())) {
                            list1.add(device.getName());
                            mac.add(device.getAddress());
                            adapterAvailableDevices.add(device.getName() + "\n" + device.getAddress());
                        }
                    }
                }
            } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
                progressScanDevices.setVisibility(View.GONE);
                if (adapterAvailableDevices.getCount() == 0) {
                    Toast.makeText(context, "No new devices found", Toast.LENGTH_SHORT).show();
                } else {
//                    Toast.makeText(context, "Click on the device to start the chat", Toast.LENGTH_SHORT).show();
                }
            }
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_device_list, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_scan_devices:
                scanDevices();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void scanDevices() {
        progressScanDevices.setVisibility(View.VISIBLE);
        adapterAvailableDevices.clear();
        Toast.makeText(context, "Scan started", Toast.LENGTH_SHORT).show();

        if (bluetoothAdapter.isDiscovering()) {
            bluetoothAdapter.cancelDiscovery();
        }

        bluetoothAdapter.startDiscovery();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (bluetoothDeviceListener != null) {
            unregisterReceiver(bluetoothDeviceListener);
        }
    }
}
