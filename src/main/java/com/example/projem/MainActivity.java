package com.example.projem;

import androidx.appcompat.app.AppCompatActivity;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Set;

public class MainActivity extends AppCompatActivity {

    BluetoothAdapter myBluetooth;
    private Set<BluetoothDevice> pairedDevices;
    Button toggle_button,pair_button;
    ListView pairedlist;
    public static String EXTRA_ADRESS= "device_adress";
    ArrayAdapter<String> adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        myBluetooth=BluetoothAdapter.getDefaultAdapter();
        toggle_button= (Button) findViewById(R.id.button_toggle);
        pair_button= (Button) findViewById(R.id.button_pair);
        pairedlist=(ListView) findViewById(R.id.device_list);

        toggle_button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                toggleBluetooth();
            }


        });

        pair_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listdevice();
            }

        });

    }
    private void toggleBluetooth() {
        if(myBluetooth==null){
            Toast.makeText(getApplicationContext(),"Bluetooth Cihazi Yoktur.",Toast.LENGTH_SHORT).show();
        }
        if(!myBluetooth.isEnabled()){
            Intent enableBTIntent= new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivity(enableBTIntent);
        }
        if(myBluetooth.isEnabled()){
            myBluetooth.disable();
        }
    }

    private void listdevice() {
        pairedDevices = myBluetooth.getBondedDevices();
        ArrayList list = new ArrayList();
        if(pairedDevices.size() > 0 ){
            for(BluetoothDevice bt: pairedDevices){
                list.add(bt.getName() + "\n" + bt.getAddress() );
            }
        }
        else{
            Toast.makeText(getApplicationContext(),"Eşleşmiş Cihaz Yoktur.", Toast.LENGTH_SHORT).show();
        }

        final ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1,list);
        pairedlist.setAdapter(adapter);
        pairedlist.setOnItemClickListener(selectDevice);
    }

    public AdapterView.OnItemClickListener selectDevice= new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            String info= ((TextView) view).getText().toString();
            String address= info.substring(info.length()-17);

            Intent comintent= new Intent(MainActivity.this, Communication.class);
            comintent.putExtra(EXTRA_ADRESS,address);
            startActivity(comintent);
        }
    };
}