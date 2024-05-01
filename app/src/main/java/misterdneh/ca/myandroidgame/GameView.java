package misterdneh.ca.myandroidgame;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class GameView extends SurfaceView implements SurfaceHolder.Callback {

    public GameThread gameThread;
    private Paint textpaint;
    public String buttonpressed = "none";
    public boolean upPressed,downPressed,leftPressed,rightPressed = false;

    public GameView(Context context) {
        super(context);
        getHolder().addCallback(this);
        gameThread = new GameThread(getHolder(), this);
        textpaint = new Paint();
        textpaint.setColor(Color.WHITE);
        textpaint.setTextSize(60);
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

    public void draw (Canvas canvas) {
        // Render the game here
        super.draw(canvas);
        canvas.drawText(buttonpressed, 200, 200, textpaint);
    }

    public void update() {
        // Update game state here
    }
}

