package misterdneh.ca.myandroidgame.entity;

import android.graphics.Canvas;

import misterdneh.ca.myandroidgame.GameView;
import misterdneh.ca.myandroidgame.R;

public class Player extends entityinfo{

    public Player(GameView gameView){
        this.gameView = gameView;
        initializePlayer();
    }

    private void initializePlayer() {
        entityWidth = 160;
        entityHeight = 160;
        entitySpriteSheet(R.drawable.playwalkingspritesheet,entityWalking);
        defaultEntityImage = entityWalking[0];
        entitySpeed = 8;
    }
    public void update(){
        updateXY();
        updateAnimation();
    }
    public void draw(Canvas canvas){
        if (defaultEntityImage != null) {
            canvas.drawBitmap(defaultEntityImage, posX, posY, null);
        }
    }

    public void updateXY(){
        if (gameView.upPressed){
            entityDirection = "up";
            entityDefaultDirection = "up";
            posY -= entitySpeed;
        }else if (gameView.downPressed){
            entityDirection = "down";
            entityDefaultDirection = "down";
            posY += entitySpeed;
        }else if (gameView.leftPressed){
            entityDirection = "left";
            entityDefaultDirection = "left";
            posX -= entitySpeed;
        }else if (gameView.rightPressed){
            entityDirection = "right";
            entityDefaultDirection = "right";
            posX += entitySpeed;
        }else {
            entityDirection = "none";
        }
    }
    public void updateAnimation(){
        if (gameView.upPressed || gameView.downPressed || gameView.leftPressed || gameView.rightPressed) {
            walkAnimCount++;
            if (walkAnimCount > 12) {
                if (walkAnimNum == 1) {
                    walkAnimNum = 2;
                } else if (walkAnimNum == 2) {
                    walkAnimNum = 3;
                } else if (walkAnimNum == 3) {
                    walkAnimNum = 4;
                } else if (walkAnimNum == 4) {
                    walkAnimNum = 1;
                }
                walkAnimCount = 0;
            }
        }else {
            if (walkAnimCount < 11 + 1) {
                walkAnimCount = 0;
                walkAnimNum = 1;
                if (walkAnimNum == 1) {
                    walkAnimNum = 2;
                } else if (walkAnimNum == 2) {
                    walkAnimNum = 3;
                } else if (walkAnimNum == 3) {
                    walkAnimNum = 4;
                }
            }
        }
        switch (entityDirection){
            case "up":
                if (walkAnimNum == 1) {
                    defaultEntityImage = entityWalking[9];
                }
                if (walkAnimNum == 2) {
                    defaultEntityImage = entityWalking[10];
                }
                if (walkAnimNum == 3) {
                    defaultEntityImage = entityWalking[9];
                }
                if (walkAnimNum == 4) {
                    defaultEntityImage = entityWalking[11];
                }
                break;
            case "down":
                if (walkAnimNum == 1) {
                    defaultEntityImage = entityWalking[0];
                }
                if (walkAnimNum == 2) {
                    defaultEntityImage = entityWalking[1];
                }
                if (walkAnimNum == 3) {
                    defaultEntityImage = entityWalking[0];
                }
                if (walkAnimNum == 4) {
                    defaultEntityImage = entityWalking[2];
                }
                break;
            case "left":
                if (walkAnimNum == 1) {
                    defaultEntityImage = entityWalking[3];
                }
                if (walkAnimNum == 2) {
                    defaultEntityImage = entityWalking[4];
                }
                if (walkAnimNum == 3) {
                    defaultEntityImage = entityWalking[3];
                }
                if (walkAnimNum == 4) {
                    defaultEntityImage = entityWalking[5];
                }
                break;
            case "right":
                if (walkAnimNum == 1) {
                    defaultEntityImage = entityWalking[6];
                }
                if (walkAnimNum == 2) {
                    defaultEntityImage = entityWalking[7];
                }
                if (walkAnimNum == 3) {
                    defaultEntityImage = entityWalking[6];
                }
                if (walkAnimNum == 4) {
                    defaultEntityImage = entityWalking[8];
                }
                break;
            case "none":

                if (entityDefaultDirection.equals("up")) {
                    defaultEntityImage = entityWalking[9];
                }
                if (entityDefaultDirection.equals("down")) {
                    defaultEntityImage = entityWalking[0];
                }
                if (entityDefaultDirection.equals("right")) {
                    defaultEntityImage = entityWalking[6];
                }
                if (entityDefaultDirection.equals("left")) {
                    defaultEntityImage = entityWalking[3];
                }
                break;
        }
    }
}
