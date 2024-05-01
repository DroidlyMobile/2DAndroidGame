package misterdneh.ca.myandroidgame;

import static misterdneh.ca.myandroidgame.Dpad.CENTER;
import static misterdneh.ca.myandroidgame.Dpad.DOWN;
import static misterdneh.ca.myandroidgame.Dpad.LEFT;
import static misterdneh.ca.myandroidgame.Dpad.RIGHT;
import static misterdneh.ca.myandroidgame.Dpad.UP;

import android.os.Build;
import android.os.Bundle;
import android.view.InputDevice;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private GameView gameView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        gameView = new GameView(this);
        setContentView(gameView);
        getGameControllerIds();
    }

    @Override
    protected void onPause() {
        super.onPause();
        gameView.gameThread.stopLoop();
    }

    public void fullscreen() {
        View decorView = this.getWindow().getDecorView();
        View View = null;
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | Window.FEATURE_NO_TITLE | View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
        decorView.setSystemUiVisibility(uiOptions);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            getWindow().getAttributes().layoutInDisplayCutoutMode =
                    WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES;
        }
    }

    public void viewWidth(View _view, int _width) {
        _view.getLayoutParams().width = _width;
    }

    public void viewHeight(View _view, int _height) {
        _view.getLayoutParams().height = _height;
    }

    public void _setViewWidthHeight(View _view, int _width, int _height) {
        _view.getLayoutParams().width = _width;
        _view.getLayoutParams().height = _height;
    }

    public ArrayList<Integer> getGameControllerIds() {
        ArrayList<Integer> gameControllerDeviceIds = new ArrayList<Integer>();
        int[] deviceIds = InputDevice.getDeviceIds();
        for (int deviceId : deviceIds) {
            InputDevice dev = InputDevice.getDevice(deviceId);
            int sources = 0;
            if (dev != null) {
                sources = dev.getSources();
            }

            // Verify that the device has gamepad buttons, control sticks, or both.
            if (((sources & InputDevice.SOURCE_GAMEPAD) == InputDevice.SOURCE_GAMEPAD)
                    || ((sources & InputDevice.SOURCE_JOYSTICK)
                    == InputDevice.SOURCE_JOYSTICK)) {
                // This device is a game controller. Store its device ID.
                if (!gameControllerDeviceIds.contains(deviceId)) {
                    gameControllerDeviceIds.add(deviceId);
                }
            }
        }
        return gameControllerDeviceIds;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        int code = event.getKeyCode();
        if (code == KeyEvent.KEYCODE_DPAD_DOWN){
            gameView.buttonpressed = "DOWN";
        }else if (code == KeyEvent.KEYCODE_DPAD_UP) {
            gameView.buttonpressed = "UP";
        }else if (code == KeyEvent.KEYCODE_DPAD_RIGHT) {
            gameView.buttonpressed = "LEFT";
        }else if (code == KeyEvent.KEYCODE_DPAD_LEFT) {
            gameView.buttonpressed = "RIGHT";
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        int code = event.getKeyCode();
        if (code == KeyEvent.KEYCODE_DPAD_DOWN){
            gameView.buttonpressed = "none";
        }else if (code == KeyEvent.KEYCODE_DPAD_UP) {
            gameView.buttonpressed = "none";
        }else if (code == KeyEvent.KEYCODE_DPAD_RIGHT) {
            gameView.buttonpressed = "none";
        }else if (code == KeyEvent.KEYCODE_DPAD_LEFT) {
            gameView.buttonpressed = "none";
        }
        return super.onKeyUp(keyCode, event);
    }
    /*@Override
    public boolean onGenericMotionEvent(MotionEvent event) {

        // Check if this event if from a D-pad and process accordingly.
        if (Dpad.isDpadDevice(event)) {

            int press = dpad.getDirectionPressed(event);
            switch (press) {
                case LEFT:
                    // Do something for LEFT direction press
                    gameView.buttonpressed = "LEFT";
                    return true;
                case RIGHT:
                    // Do something for RIGHT direction press
                    gameView.buttonpressed = "RIGHT";
                    return true;
                case UP:
                    // Do something for UP direction press
                    gameView.buttonpressed = "UP";
                    return true;
                case DOWN:
                    // Do something for UP direction press
                    gameView.buttonpressed = "DOWN";
                    return true;

            }
        }else {
            gameView.buttonpressed = "none";
        }
        return super.onGenericMotionEvent(event);
    }*/

    /*private static boolean isFireKey ( int keyCode){
        // Here we treat Button_A and DPAD_CENTER as the primary action
        // keys for the game.
        return keyCode == KeyEvent.KEYCODE_DPAD_CENTER
                || keyCode == KeyEvent.KEYCODE_BUTTON_A;
    }*/

}