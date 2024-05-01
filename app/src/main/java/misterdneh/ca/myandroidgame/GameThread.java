package misterdneh.ca.myandroidgame;

import android.os.Build;
import android.util.Log;
import android.view.SurfaceHolder;
import android.graphics.*;

public class GameThread extends Thread {

    private SurfaceHolder surfaceHolder;
    private GameView gameView;
    private boolean running;
    private long targetFPS = 60; // Target frames per second
    private long startTime;
    private long timeMillis;
    private long waitTime;
    private long totalTime = 0;
    private long frameCount = 0;
    private long targetTime = 1000 / targetFPS;
    private long averageFPS = 0;

    public GameThread(SurfaceHolder surfaceHolder, GameView gameView) {
        this.surfaceHolder = surfaceHolder;
        this.gameView = gameView;
    }

    public void setRunning(boolean running) {
        this.running = running;
    }

    @Override
    public void run() {
        while (running) {
            startTime = System.nanoTime();
            Canvas canvas = null;

            try {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    canvas = surfaceHolder.lockHardwareCanvas();
                }
                synchronized (surfaceHolder) {
                    gameView.update();
                    gameView.draw(canvas);
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (canvas != null) {
                    try {
                        surfaceHolder.unlockCanvasAndPost(canvas);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            // Calculate FPS
            timeMillis = (System.nanoTime() - startTime) / 1000000;
            waitTime = targetTime - timeMillis;
            try {
                if (waitTime > 0) {
                    sleep(waitTime);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            totalTime += System.nanoTime() - startTime;
            frameCount++;
            if (frameCount == targetFPS) {
                averageFPS = 1000 / ((totalTime / frameCount) / 1000000);
                frameCount = 0;
                totalTime = 0;
                System.out.println("AVERAGE FPS " + averageFPS);
            }
        }
    }
    public long getAverageFPS(){
        return averageFPS;
    }

    public void stopLoop() {
        Log.d("GameLoop.java", "stopLoop()");
        running = false;
        try {
            join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
