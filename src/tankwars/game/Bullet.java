package tankwars.game;

import tankwars.GameConstants;
import tankwars.ResourceManager;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

public class Bullet extends GameObject implements Poolable,Updatable{



    private float vx;
    private float vy;
    private float angle;
    private int tankID =- 1;

    private float R = 10;
    private float ROTATIONSPEED = 3.0f;
    Sound bullethit = ResourceManager.getSound("bulletCollide");



    Bullet(BufferedImage img) {

        super(100,100,img);
        this.vx = 0;
        this.vy = 0;
        this.angle = 0;

    }
    Bullet(float x, float y, float angle, BufferedImage img) {

        super(x,y,img);
        this.vx = 0;
        this.vy = 0;
        this.angle = angle;
    }


    public void update(GameWorld gw) {
        vx = Math.round(R * Math.cos(Math.toRadians(angle)));
        vy = Math.round(R * Math.sin(Math.toRadians(angle)));
        x += vx;
        y += vy;
        checkBorder();
        this.hitbox.setLocation((int)this.x,(int)this.y);

    }

    private void rotateLeft() {
        this.angle -= this.ROTATIONSPEED;
    }

    private void rotateRight() {
        this.angle += this.ROTATIONSPEED;
    }



    private void checkBorder() {
        if (x < 30) {
            x = 30;
            this.hasCollided = true;
        }
        if (x >= GameConstants.GAME_MAP_WIDTH - 50) {
            x = GameConstants.GAME_MAP_WIDTH - 50;
            this.hasCollided = true;
        }
        if (y < 32) {
            y = 32;
            this.hasCollided = true;
        }
        if (y >= GameConstants.GAME_MAP_HEIGHT -70) {
            y = GameConstants.GAME_MAP_HEIGHT - 70;
            this.hasCollided = true;

        }
    }

    public void setOwner(int tankID){
        this.tankID = tankID;
    }

    public int checkOwner(){
        return tankID;
    }



    public void drawImage(Graphics g) {
        AffineTransform rotation = AffineTransform.getTranslateInstance(x, y);
        rotation.rotate(Math.toRadians(angle), this.img.getWidth() / 2.0, this.img.getHeight() / 2.0);
        Graphics2D g2d = (Graphics2D) g;
        g2d.drawImage(this.img, rotation, null);
        //g2d.setColor(Color.RED);
        //g2d.rotate(Math.toRadians(angle), bounds.x + bounds.width/2, bounds.y + bounds.height/2);
        //g2d.drawRect((int)x,(int)y,this.img.getWidth(), this.img.getHeight());

    }


    public void handleCollision(GameObject by) {
        bullethit.setVolume(.3f);
        if( by instanceof Tank){
            //true if the bullet hits other tank
            hasCollided = this.tankID != (((Tank)by).getTankID());
            bullethit.play();

        }else if(by instanceof Wall w){
            hasCollided = true;
            bullethit.play();
        }else if(by instanceof BreakableWall bw){

            bw.breakWall();
            hasCollided = true;
            bullethit.play();
        }

    }




    @Override
    public void initObject(float x, float y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public void initObject(float x, float y, float angle) {
        this.x = x;
        this.y = y;
        this.angle = angle;
    }

    @Override
    public void resetObject() {
        this.x = -5;
        this.y = -5;

    }

}
