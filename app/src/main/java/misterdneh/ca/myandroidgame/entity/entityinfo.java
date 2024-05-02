package misterdneh.ca.myandroidgame.entity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import misterdneh.ca.myandroidgame.GameView;

public class entityinfo {

    public int posX,posY = 0;
    public int entityWidth,entityHeight = 0;
    public int entitySpeed = 0;
    public int walkAnimNum = 1;
    public int walkAnimSpeed = 0;
    public int walkAnimCount = 0;
    public int screenX,screenY = 0;

    public String entityDirection = "none";
    public String entityDefaultDirection = "down";
    public GameView gameView;
    public Bitmap[] entityWalking = new Bitmap[14];
    public Bitmap defaultEntityImage = null;


    public void entitySpriteSheet(int drawable,Bitmap[] bitmaps){
        Bitmap spritesheet1;
        int currentColumn = 0;
        int currentRow = 0;
        int numberOftiles = 0;
        BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
        bitmapOptions.inScaled = false;
        spritesheet1 = BitmapFactory.decodeResource(gameView.getResources(),
                drawable,
                bitmapOptions);
        int maxColumns = spritesheet1.getWidth()/16;
        int maxRows = spritesheet1.getHeight()/16;
        while (currentRow<maxRows){
            bitmaps[numberOftiles] = Bitmap.createScaledBitmap(Bitmap.createBitmap(spritesheet1,
                            currentColumn * 16,
                            currentRow * 16,
                            16,
                            16),entityWidth,
                    entityHeight,false);
            currentColumn ++;
            if (currentColumn == maxColumns){
                currentColumn = 0;
                currentRow ++;
            }
            numberOftiles ++;
        }
    }
}
