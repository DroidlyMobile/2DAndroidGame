package misterdneh.ca.myandroidgame.world;

import android.graphics.Canvas;

import misterdneh.ca.myandroidgame.GameView;
import misterdneh.ca.myandroidgame.R;

public class Tilemanager extends tileinfo{

    public tileinfo[] tiles = new tileinfo[1000];

    public Tilemanager(GameView gameView){
        this.gameView = gameView;
        setupTilesheet(R.drawable.worldtiles);
        worldTileNum = new int[25][25];//max world size x and y
        setupTileDetails();
    }
    public void drawTiles(Canvas canvas) {
        int tileCol = 0;
        int tileRow = 0;
        while (tileCol < 25 && tileRow < 25) {
            tileNum = worldTileNum[tileCol][tileRow];//Gets the tileNum at the XY position from the txt data
            int tileWorldX = tileCol * gameView.tileSize;//Sets the tile at the position X in the world times the scaled tilesize 160 in example
            int tileWorldY = tileRow * gameView.tileSize;//Sets position Y times scaled tilesize
            int tileScreenX = tileWorldX - gameView.player.posX + gameView.player.screenX;
            int tileScreenY = tileWorldY - gameView.player.posY + gameView.player.screenY;

            if (tileWorldX + gameView.tileSize > gameView.player.posX - gameView.player.screenX
                    && tileWorldX - gameView.tileSize < gameView.player.posX + gameView.player.screenX
                    && tileWorldY + gameView.tileSize > gameView.player.posY - gameView.player.screenY
                    && tileWorldY - (gameView.tileSize * 2) < gameView.player.posY + gameView.player.screenY) {//Camera 64 is added to the bottom because of the dumb navbar
                if (tiles[tileNum].defaultTileImg != null) {
                    canvas.drawBitmap(tiles[tileNum].defaultTileImg,
                            tileScreenX, tileScreenY, null);
                }
            }
            tileCol++;

            if (tileCol == 25) {//Check if tileCol reaches the end in this case 100 tiles then resets back to 0 then increases rows
                tileCol = 0;
                tileRow++;
            }
        }
    }

    private void setupTileDetails() {
        //collisionTiles.add(String.valueOf(1));
        for (int tileID = 0; tileID<tilesList.size(); tileID++){
            tiles[tileID] = new tileinfo();
            tiles[tileID].defaultTileImg = tileImgs[tileID];
            tiles[tileID].tileWidth = gameView.tileSize;
            tiles[tileID].tileHeight = gameView.tileSize;
            // if (collisionTiles.contains(String.valueOf((int)tileID))){
            //    tileInfo[tileID].tileCollision = true;
            // }
        }

    }


}
