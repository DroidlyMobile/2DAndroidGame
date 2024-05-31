package misterdneh.ca.myandroidgame;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.MediaRouteActionProvider;
import android.app.Presentation;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.media.MediaRouter;
import android.media.MediaRouter.RouteInfo;
import android.opengl.GLSurfaceView;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.InputDevice;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class DualScreenPresentation extends Activity {

    private MediaRouter mMediaRouter;
    private TopScreen mPresentation;
    //private GLSurfaceView mSurfaceView_top,mSurfaceView_bottom;
    private TextView mInfoTextView;
    private boolean mPaused;
    private RelativeLayout mSurfaceView_top;
    private RelativeLayout mSurfaceView_bottom;
    private GameView gameView;
    public boolean externaldisplay = false;

    @SuppressLint({"MissingInflatedId", "ClickableViewAccessibility"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Be sure to call the super class.
        super.onCreate(savedInstanceState);

        // Get the media router service.
        mMediaRouter = (MediaRouter)getSystemService(Context.MEDIA_ROUTER_SERVICE);
        // See assets/res/any/layout/presentation_with_media_router_activity.xml for this
        // view layout definition, which is being set here as
        // the content of our screen.
        setContentView(R.layout.game_layout);
        gameView = new GameView(this,getDisplayWidthPixels(this)/2,
                getDisplayHeightPixels(this));
        mSurfaceView_top = findViewById(R.id.topview);
        mSurfaceView_bottom = findViewById(R.id.bottomview);
        mSurfaceView_top.addView(gameView);

        fullscreen();
        getGameControllerIds();
        mSurfaceView_bottom.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getActionMasked()){
                    case MotionEvent.ACTION_DOWN:
                        if (externaldisplay) {
                            mPresentation.gameView.rightPressed = true;
                        }else {
                            gameView.rightPressed = true;
                        }
                        break;
                    case MotionEvent.ACTION_UP:
                        if (externaldisplay) {
                            mPresentation.gameView.rightPressed = false;
                        }else {
                            gameView.rightPressed = false;
                        }
                        break;
                }
                return true;
            }
        });
    }
    public int getDisplayWidthPixels(Context _context){
        return _context.getResources().getDisplayMetrics().widthPixels;
    }
    public int getDisplayHeightPixels(Context _context){
        return _context.getResources().getDisplayMetrics().heightPixels ;
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

    @Override
    protected void onResume() {
        super.onResume();
        // Listen for changes to media routes.
        mMediaRouter.addCallback(MediaRouter.ROUTE_TYPE_LIVE_VIDEO, mMediaRouterCallback);
        // Update the presentation based on the currently selected route.
        mPaused = false;
        updatePresentation();
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Stop listening for changes to media routes.
        mMediaRouter.removeCallback(mMediaRouterCallback);
        // Pause rendering.
        mPaused = true;
        updateContents();
    }

    @Override
    protected void onStop() {
        super.onStop();
        // Dismiss the presentation when the activity is not visible.
        if (mPresentation != null) {
            mPresentation.dismiss();
            mPresentation = null;
        }
    }

    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        // Inflate the menu and configure the media router action provider.
        getMenuInflater().inflate(R.menu.presentation_with_media_router_menu, menu);

        MenuItem mediaRouteMenuItem = menu.findItem(R.id.menu_media_route);
        MediaRouteActionProvider mediaRouteActionProvider =
                (MediaRouteActionProvider)mediaRouteMenuItem.getActionProvider();
        mediaRouteActionProvider.setRouteTypes(MediaRouter.ROUTE_TYPE_LIVE_VIDEO);
        // Return true to show the menu.
        return true;
    }*/

    private void updatePresentation() {
        // Get the current route and its presentation display.
        MediaRouter.RouteInfo route = mMediaRouter.getSelectedRoute(
                MediaRouter.ROUTE_TYPE_LIVE_VIDEO);
        Display presentationDisplay = null;

        /*Display presentationDisplay = route != null ? route.getPresentationDisplay() : null;
*/
        if (route != null) {
            presentationDisplay = route.getPresentationDisplay();
        }

        // Dismiss the current presentation if the display has changed.
        if (mPresentation != null && mPresentation.getDisplay() != presentationDisplay) {
            mPresentation.dismiss();
            mPresentation = null;
        }

        // Show a new presentation if needed.
        if (mPresentation == null && presentationDisplay != null) {
            mPresentation = new TopScreen(this, presentationDisplay);
            mPresentation.setOnDismissListener(mOnDismissListener);
            try {
                mPresentation.show();
            } catch (WindowManager.InvalidDisplayException ex) {
                mPresentation = null;
            }
        }
        // Update the contents playing in this activity.
        updateContents();
    }

    private void updateContents() {
        // Show either the content in the main activity or the content in the presentation
        // along with some descriptive text about what is happening.
        if (mPresentation != null) {
            mSurfaceView_top.setVisibility(View.GONE);
            externaldisplay = true;
            //mSurfaceView_top.onPause();
            if (mPaused) {
                gameView.gameThread.stopLoop();
                //mPresentation.getSurfaceView().onPause();
            } else {
                gameView.gameThread.setRunning(true);
                //mPresentation.getSurfaceView().onResume();
            }
        } else {
            mSurfaceView_top.setVisibility(View.VISIBLE);
            externaldisplay = false;
            if (mPaused) {
                gameView.gameThread.stopLoop();
            } else {
                gameView.gameThread.setRunning(true);
            }
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
                        updateContents();
                    }
                }
            };

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
}