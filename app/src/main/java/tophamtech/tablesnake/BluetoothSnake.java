///Icon made by Freepik from www.flaticon.com

package tophamtech.tablesnake;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.os.Bundle;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Set;
import java.util.UUID;


public class BluetoothSnake extends ActionBarActivity {

    private GestureDetectorCompat mDetector;

    //%%.Declare the buttons as a name

    BluetoothAdapter mBluetoothAdapter;
    BluetoothSocket mmSocket;
    BluetoothDevice mmDevice;
    OutputStream mmOutputStream;
    InputStream mmInputStream;
    int threshold = 2000;
    Spinner colourWheel;
    Button submitColour;
    Button reconnect;
    Boolean pairedBool = false;
    ImageView arrows;
    String direction = "dir_right";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth_snake);
        mDetector = new GestureDetectorCompat(this, new MyGestureListener());
        colourWheel = (Spinner) findViewById(R.id.spinner);
        submitColour = (Button) findViewById(R.id.btn_colour);
        reconnect = (Button) findViewById(R.id.btn_connect);
        arrows = (ImageView) findViewById(R.id.img_arrow);
        try {
            bt_Connect("HC-06");
        } catch (IOException e) {
            e.printStackTrace();
        }
        reconnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    bt_Connect("HC-06");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }
    

    @Override
    public boolean onTouchEvent(MotionEvent event){
        this.mDetector.onTouchEvent(event);
        return super.onTouchEvent(event);
    }

    class MyGestureListener extends GestureDetector.SimpleOnGestureListener {
        private static final String DEBUG_TAG = "Gestures";
        //bottom right is +ve, +ve

        @Override
        public boolean onFling(MotionEvent event1, MotionEvent event2,
                               float velocityX, float velocityY) {
            Log.d(DEBUG_TAG, "onFling: " + event1.toString() + event2.toString() + "velocityX: " + velocityX + "velocityY: " + velocityY);
            direction="unset";
            if ((velocityX*velocityX)>(velocityY*velocityY)){
                if (velocityX>0)
                {direction="dir_right";}
                else {
                    direction="dir_left";
                    arrows.setImageResource(R.drawable.left_arrow);
                    arrows.setAlpha((float) 0.5);
                }
            }
            else {
                if (velocityY > 0) {
                    direction = "dir_down";
                } else {
                    direction = "dir_up";
                }
            }
                try {
                    bt_Send(direction);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            return true;
        }
    }

    public void submitColour(View view) throws IOException{
        bt_Send(direction + " - " + "clr_"+colourWheel.getSelectedItem().toString());
        switch (colourWheel.getSelectedItem().toString()){
            case ("red"):
                view.setBackgroundColor(0xFF0000);
                break;
            case ("blue"):
                view.setBackgroundColor(0x0000FF);
                break;
            case ("green"):
                view.setBackgroundColor(0x00FF00);
                break;
            default:
                view.setBackgroundColor(0x000000);
                break;
        }
    }

    public void bt_Connect(String devName) throws IOException {

        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();
        if(pairedDevices.size() > 0)
        {
            for(BluetoothDevice device : pairedDevices)
            {
                if(device.getName().equals(devName)) //Note, you will need to change this to match the name of your device
                {
                    mmDevice = device;
                    UUID uuid = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb"); //Standard SerialPortService ID
                    //open bluetooth socket on device name specified above
                    mmSocket = mmDevice.createRfcommSocketToServiceRecord(uuid);
                    mmSocket.connect();
                    mmOutputStream = mmSocket.getOutputStream();
                    //TODO - Remove or use input stream
                    //mmInputStream = mmSocket.getInputStream();
                    Toast.makeText(getApplicationContext(), "Connected", Toast.LENGTH_SHORT).show();
                    pairedBool = true;
                    submitColour.setEnabled(true);
                    break;
                }
            }
        }
        //%.TODO - Replace with try/catch
        if (!pairedBool){
            Toast.makeText(getApplicationContext(), "Failed to connect", Toast.LENGTH_LONG).show();
            submitColour.setEnabled(false);
        }

    }

    public void bt_Send (String sendmsg) throws IOException
    {
        if (pairedBool == true) {
            String message = sendmsg;
            mmOutputStream.write(message.getBytes());
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_bluetooth_snake, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
