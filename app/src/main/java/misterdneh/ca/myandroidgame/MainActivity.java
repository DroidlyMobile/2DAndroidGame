package misterdneh.ca.myandroidgame;

import static misterdneh.ca.myandroidgame.Dpad.CENTER;
import static misterdneh.ca.myandroidgame.Dpad.DOWN;
import static misterdneh.ca.myandroidgame.Dpad.LEFT;
import static misterdneh.ca.myandroidgame.Dpad.RIGHT;
import static misterdneh.ca.myandroidgame.Dpad.UP;

import android.annotation.SuppressLint;
import android.app.Presentation;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.media.AudioRouting;
import android.media.MediaRouter;
import android.opengl.GLSurfaceView;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.InputDevice;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    public GameView gameView;
    private MediaRouter mMediaRouter;
    private DemoPresentation mPresentation;
    private GLSurfaceView surfaceView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        mMediaRouter = (MediaRouter)getSystemService(Context.MEDIA_ROUTER_SERVICE);
        gameView = new GameView(this,100,100);
        setContentView(gameView);
        getGameControllerIds();
        fullscreen();
    }

    @Override
    protected void onPause() {
        super.onPause();
        gameView.gameThread.stopLoop();
    }

    public void fullscreen() {
        View decorView = this.getWindow().getDecorView();
        View View = null;
        int uiOptions = android.view.View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | android.view.View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | Window.FEATURE_NO_TITLE | android.view.View.SYSTEM_UI_FLAG_FULLSCREEN;
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
            gameView.downPressed = true;
        }else if (code == KeyEvent.KEYCODE_DPAD_UP) {
            gameView.upPressed = true;
        }else if (code == KeyEvent.KEYCODE_DPAD_RIGHT) {
            gameView.rightPressed = true;
        }else if (code == KeyEvent.KEYCODE_DPAD_LEFT) {
            gameView.leftPressed = true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        int code = event.getKeyCode();
        if (code == KeyEvent.KEYCODE_DPAD_DOWN){
            gameView.downPressed = false;
        }else if (code == KeyEvent.KEYCODE_DPAD_UP) {
            gameView.upPressed = false;
        }else if (code == KeyEvent.KEYCODE_DPAD_RIGHT) {
            gameView.rightPressed = false;
        }else if (code == KeyEvent.KEYCODE_DPAD_LEFT) {
            gameView.leftPressed = false;
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

    @Override
    protected void onResume() {
        super.onResume();
        fullscreen();
        mMediaRouter.addCallback(MediaRouter.ROUTE_TYPE_LIVE_VIDEO, mMediaRouterCallback);
    }
    private void updatePresentation() {
        // Get the current route and its presentation display.
        MediaRouter.RouteInfo route = mMediaRouter.getSelectedRoute(
                MediaRouter.ROUTE_TYPE_LIVE_VIDEO);
        Display presentationDisplay = route != null ? route.getPresentationDisplay() : null;

        // Dismiss the current presentation if the display has changed.
        if (mPresentation != null && mPresentation.getDisplay() != presentationDisplay) {
            mPresentation.dismiss();
            mPresentation = null;
        }

        // Show a new presentation if needed.
        if (mPresentation == null && presentationDisplay != null) {
            mPresentation = new DemoPresentation(this, presentationDisplay);
            mPresentation.setOnDismissListener(mOnDismissListener);
            try {
                mPresentation.show();
            } catch (WindowManager.InvalidDisplayException ex) {
                mPresentation = null;
            }
        }
        updateContents();
    }
    private void updateContents() {
        // Show either the content in the main activity or the content in the presentation
        // along with some descriptive text about what is happening.
        if (mPresentation != null) {
            gameView.setVisibility(View.INVISIBLE);
            /*gameView.onPause();
            if (mPaused) {
                mPresentation.getSurfaceView().onPause();
            } else {
                mPresentation.getSurfaceView().onResume();
            }*/
        } else {
            gameView.setVisibility(View.VISIBLE);
            //if (testtext != null) {
            // }
            /*mSurfaceView.setVisibility(View.VISIBLE);

            if (mPaused) {
                mSurfaceView.onPause();
            } else {
                mSurfaceView.onResume();
            }*/
        }
    }

    private final MediaRouter.SimpleCallback mMediaRouterCallback =
            new MediaRouter.SimpleCallback() {
                @Override
                public void onRouteSelected(MediaRouter router, int type, MediaRouter.RouteInfo info) {
                    updatePresentation();
                }

                @Override
                public void onRouteUnselected(MediaRouter router, int type, MediaRouter.RouteInfo info) {
                    updatePresentation();
                }

                @Override
                public void onRoutePresentationDisplayChanged(MediaRouter router, MediaRouter.RouteInfo info) {
                    updatePresentation();
                }
            };

    /**
     * Listens for when presentations are dismissed.
     */
    private final DialogInterface.OnDismissListener mOnDismissListener =
            new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    if (dialog == mPresentation) {
                        mPresentation = null;
                        //updateContents();
                    }
                }
            };
    private final static class DemoPresentation extends Presentation {
        private GLSurfaceView mSurfaceView;
        private TextView testingtext;
        private GameView gamelayout;
        public DemoPresentation(Context context, Display display) {
            super(context, display);
        }

        @SuppressLint("MissingInflatedId")
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            // Be sure to call the super class.
            super.onCreate(savedInstanceState);

            // Get the resources for the context of the presentation.
            // Notice that we are getting the resources from the context of the presentation.

            // Inflate the layout.
            setContentView(gamelayout);
            // Set up the surface view for visual interest.
            /*mSurfaceView = findViewById(R.id.surface_view);
            mSurfaceView.setRenderer(new CubeRenderer(false));*/

        }
    }
}