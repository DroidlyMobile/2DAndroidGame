package misterdneh.ca.myandroidgame.world;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.BufferedReader;
import java.io.InputStream;
import java.util.ArrayList;

import misterdneh.ca.myandroidgame.GameView;

public class tileinfo {

    public GameView gameView;
    public int[][] worldTileNum;
    public int tileNum = 0;
    public int tileWidth,tileHeight = 160;

    public Bitmap defaultTileImg = null;
    public Bitmap[] tileImgs = new Bitmap[1000];

    public InputStream inputStream;
    public BufferedReader bufferedReader;

    public ArrayList<Integer> tilesList = new ArrayList<>();

    public void setupTilesheet(int drawable){
        Bitmap tilesheet1;
        int currentColumn = 0;
        int currentRow = 0;
        int numberOftiles = 0;
        BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
        bitmapOptions.inScaled = false;
        tilesheet1 = BitmapFactory.decodeResource(gameView.getResources(),
                drawable,
                bitmapOptions);
        int maxColumns = tilesheet1.getWidth()/16;
        int maxRows = tilesheet1.getHeight()/16;
        while (currentRow<maxRows){
            tileImgs[numberOftiles] = Bitmap.createScaledBitmap(
                    Bitmap.createBitmap(tilesheet1,
                            currentColumn * 16,
                            currentRow * 16,
                            16,
                            16),gameView.tileSize,
                    gameView.tileSize,false);
            currentColumn ++;
            if (currentColumn == maxColumns){
                currentColumn = 0;
                currentRow ++;
            }
            numberOftiles ++;
            tilesList.add(numberOftiles);
        }
    }
}
