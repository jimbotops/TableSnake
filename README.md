Complete Android App for the Snake Table game.

To Use:
1. Pair the phone with the HC06 device in the default android menu (Settings > Bluetooth)
2. Open the TableSnake app
3. Press Connect
4. You should now be able to control the snake by swiping up/down/left/right on the screen


This is a very simple app that uses swipes on the phone screen to send UART commands over bluetooth to a HC06 device. Other HCXX devices may also work but are untested. If trying with this, or connecting to a bluetooth device with a non standard name, you will need to edit the onCreate method for the names to match.

NOTE: You must go into the android settings and pair with the device before trying to use the app and connect to it.
