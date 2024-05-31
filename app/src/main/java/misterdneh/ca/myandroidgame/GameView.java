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
import misterdneh.ca.myandroidgame.world.Tilemanager;

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
    public int tileSize = 0;
    public Player player;
    public Tilemanager tilemanager;

    public GameView(Context context,int screenWidth,int screenHeight) {
        super(context);
        getHolder().addCallback(this);
        gameThread = new GameThread(getHolder(), this);
        textpaint = new Paint();
        backgroundpaint = new Paint();
        textpaint.setColor(Color.WHITE);
        textpaint.setTextSize(60);
        backgroundpaint.setColor(Color.BLUE);
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight + getNavigationBarHeight();
        tileSize = 160;
        player = new Player(this);
        tilemanager = new Tilemanager(this);
    }
    public void draw (Canvas canvas) {
        // Render the game here
        super.draw(canvas);
        canvas.drawRect(0,0,screenWidth, screenHeight,backgroundpaint);
        //canvas.drawText("FPS " + String.valueOf(getNavigationBarHeight()), 200, 200, textpaint);
        canvas.drawRect(playerX,playerY,playerX + playerWidth, playerY + playerHeight,textpaint);
        tilemanager.drawTiles(canvas);
        player.draw(canvas);
    }

    public void update() {
        // Update game state here
        player.update();
    }
    public int getDisplayWidthPixels(Context _context){
        return _context.getResources().getDisplayMetrics().widthPixels + getNavigationBarHeight();
    }
    public int getDisplayHeightPixels(Context _context){
        return _context.getResources().getDisplayMetrics().heightPixels + getNavigationBarHeight() ;
    }
    public int getNavigationBarHeight() {
        DisplayMetrics metrics = new DisplayMetrics();
        ((Activity) getContext()).getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int usableHeight = metrics.widthPixels;
        ((Activity) getContext()).getWindowManager().getDefaultDisplay().getRealMetrics(metrics);
        int realHeight = metrics.widthPixels;
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

