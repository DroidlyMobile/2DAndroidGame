package misterdneh.ca.myandroidgame;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import misterdneh.ca.myandroidgame.entity.Player;

public class GameView extends SurfaceView implements SurfaceHolder.Callback {

    public GameThread gameThread;
    private Paint textpaint,backgroundpaint;
    public String buttonpressed = "none";
    public boolean upPressed,downPressed,leftPressed,rightPressed = false;
    public int playerX = 0;
    public int playerY = 0;
    public int playerSpeed = 16;
    public int playerWidth = 160;
    public int playerHeight = 160;
    public int screenWidth,screenHeight = 0;
    public Player player;

    public GameView(Context context) {
        super(context);
        getHolder().addCallback(this);
        gameThread = new GameThread(getHolder(), this);
        textpaint = new Paint();
        backgroundpaint = new Paint();
        textpaint.setColor(Color.WHITE);
        textpaint.setTextSize(60);
        backgroundpaint.setColor(Color.BLUE);
        screenWidth = getDisplayWidthPixels(context);
        screenHeight = getDisplayHeightPixels(context);
        player = new Player(this);
    }



    public void draw (Canvas canvas) {
        // Render the game here
        super.draw(canvas);
        canvas.drawRect(0,0,screenWidth, screenHeight,backgroundpaint);
        canvas.drawText("FPS " + String.valueOf(getDisplayHeightPixels(getContext())), 200, 200, textpaint);
        canvas.drawRect(playerX,playerY,playerX + playerWidth, playerY + playerHeight,textpaint);
        player.draw(canvas);
    }

    public void update() {
        // Update game state here
        player.update();
    }
    public int getDisplayWidthPixels(Context _context){
        return _context.getResources().getDisplayMetrics().widthPixels;
    }
    public int getDisplayHeightPixels(Context _context){
        return _context.getResources().getDisplayMetrics().heightPixels + getNavigationBarHeight();
    }
    private int getNavigationBarHeight() {
        DisplayMetrics metrics = new DisplayMetrics();
        ((Activity) getContext()).getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int usableHeight = metrics.heightPixels;
        ((Activity) getContext()).getWindowManager().getDefaultDisplay().getRealMetrics(metrics);
        int realHeight = metrics.heightPixels;
        if (realHeight > usableHeight) {
            return realHeight - usableHeight;
        }
        return 0;
    }
    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        gameThread = new GameThread(getHolder(),this);
        gameThread.setRunning(true);
        gameThread.start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        // Handle surface changes if needed
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        boolean retry = true;
        gameThread.setRunning(false);
        while (retry) {
            try {
                gameThread.join();
                retry = false;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

