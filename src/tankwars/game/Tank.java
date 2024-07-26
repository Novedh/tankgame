package tankwars.game;

import tankwars.GameConstants;
import tankwars.ResourceManager;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.nio.Buffer;
import java.util.ArrayList;

/**
 *
 * @author anthony-pc
 */
public class Tank extends GameObject{

    private int lives = 3;
    int ownerID;
    private static ResourcePool<Bullet> bulletPool = new ResourcePool<>("bullet", Bullet.class,500);
    private float screenX;
    private float screenY;

    private float vx;
    private float vy;
    private float angle;

    private float R = 5;
    private float ROTATIONSPEED = 3.0f;

    private boolean UpPressed;
    private boolean DownPressed;
    private boolean RightPressed;
    private boolean LeftPressed;
    private boolean ShootPressed;

    private ArrayList<Bullet> ammo = new ArrayList<Bullet>();
    private long coolDown = 500;
    private long timeSinceLastShot = 0;

    Tank(float x, float y, float vx, float vy, float angle, BufferedImage img) {
        super(x,y,img);
        this.screenX = x;
        this.screenY = y;
        this.vx = vx;
        this.vy = vy;
        this.angle = angle;
    }

    void setX(float x){ this.x = x; }

    void setY(float y) { this. y = y;}

    float getX(){return this.x;}

    float getY(){return this.y;}

    public float getScreenX() {
        return screenX;
    }

    public float getScreenY() {
        return screenY;
    }

    void toggleUpPressed() {
        this.UpPressed = true;
    }

    void toggleDownPressed() {
        this.DownPressed = true;
    }

    void toggleRightPressed() {
        this.RightPressed = true;
    }

    void toggleShootPressed() {
        this.ShootPressed = true;
    }

    void toggleLeftPressed() {
        this.LeftPressed = true;
    }

    void unToggleUpPressed() {
        this.UpPressed = false;
    }

    void unToggleDownPressed() {
        this.DownPressed = false;
    }

    void unToggleRightPressed() {
        this.RightPressed = false;
    }

    void unToggleLeftPressed() {
        this.LeftPressed = false;
    }

    void unToggleShootPressed() {
        this.ShootPressed = false;
    }

    void update() {
        if (this.UpPressed) {
            this.moveForwards();
        }

        if (this.DownPressed) {
            this.moveBackwards();
        }

        if (this.LeftPressed) {
            this.rotateLeft();
        }

        if (this.RightPressed) {
            this.rotateRight();
        }

        long currTime = System.currentTimeMillis();
        if(this.ShootPressed && currTime > this.timeSinceLastShot + this.coolDown){
            this.timeSinceLastShot = currTime;
            var p = ResourcePools.getPooldInstance("bullet");
            p.initObject(
                    x+ this.img.getWidth()/2f,
                    y+this.img.getHeight()/2f,
                       angle);
            this.ammo.add((Bullet) p);
        }

        for(int i =0; i<ammo.size();i++){
            this.ammo.get(i).update();
        }
        centerScreen();
        this.hitbox.setLocation((int)this.x, (int)this.y);


    }

    private void rotateLeft() {
        this.angle -= this.ROTATIONSPEED;
    }

    private void rotateRight() {
        this.angle += this.ROTATIONSPEED;
    }

    private void moveBackwards() {
        vx =  Math.round(R * Math.cos(Math.toRadians(angle)));
        vy =  Math.round(R * Math.sin(Math.toRadians(angle)));
        x -= vx;
        y -= vy;
       checkBorder();
    }

    private void moveForwards() {
        vx = Math.round(R * Math.cos(Math.toRadians(angle)));
        vy = Math.round(R * Math.sin(Math.toRadians(angle)));
        x += vx;
        y += vy;
        checkBorder();
    }

    private void centerScreen(){
        this.screenX = this.x - GameConstants.GAME_SCREEN_WIDTH/4f;
        this.screenY = this.y - GameConstants.GAME_SCREEN_HEIGHT/2f;

        if(this.screenX<0) screenX = 0;
        if(this.screenY<0) screenY = 0;

        if(screenX > GameConstants.GAME_MAP_WIDTH - GameConstants.GAME_SCREEN_WIDTH/2f){
            this.screenX = GameConstants.GAME_MAP_WIDTH - GameConstants.GAME_SCREEN_WIDTH/2f;
        }

        if(screenY > GameConstants.GAME_MAP_HEIGHT - GameConstants.GAME_SCREEN_HEIGHT){
            this.screenY = GameConstants.GAME_MAP_HEIGHT - GameConstants.GAME_SCREEN_HEIGHT;
        }

    }
    private void checkBorder() {
        if (x < 30) {
            x = 30;
        }
        if (x >= GameConstants.GAME_MAP_WIDTH - 80) {
            x = GameConstants.GAME_MAP_WIDTH - 80;
        }
        if (y < 32) {
            y = 32;
        }
        if (y >= GameConstants.GAME_MAP_HEIGHT - 110) {
            y = GameConstants.GAME_MAP_HEIGHT - 110;
        }
    }


    @Override
    public String toString() {
        return "x=" + x + ", y=" + y + ", angle=" + angle;
    }


    public void drawImage(Graphics g) {
        AffineTransform rotation = AffineTransform.getTranslateInstance(x, y);
        rotation.rotate(Math.toRadians(angle), this.img.getWidth() / 2.0, this.img.getHeight() / 2.0);
        Graphics2D g2d = (Graphics2D) g;
        g2d.drawImage(this.img, rotation, null);
        g2d.setColor(Color.RED);
        //g2d.rotate(Math.toRadians(angle), bounds.x + bounds.width/2, bounds.y + bounds.height/2);
        g2d.drawRect((int)x,(int)y,this.img.getWidth(), this.img.getHeight());
        for(int i =0; i<ammo.size();i++){
            this.ammo.get(i).drawImage(g);
        }

    }

    public void handleCollision(Object by){
        if(by instanceof Bullet b){
            //lose hp
        }else if (by instanceof Wall w){
            //stop undo move
        }else if (by instanceof Speed sp){
            //increase speed
        }else if(by instanceof Health hl){
            //add a heart
        }else if(by instanceof Shield){
            //add shield
        }
    }

    public Rectangle getHitbox(){
        return hitbox.getBounds();
    }



}
