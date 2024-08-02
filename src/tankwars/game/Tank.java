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
public class Tank extends GameObject implements Updatable{

    private int lives = 3;
    private int lifeCounter = 3;
    int tankID;
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

    private long coolDown = 500;
    private long timeSinceLastShot = 0;

    Tank(float x, float y, float vx, float vy, float angle, BufferedImage img, int tankID) {
        super(x,y,img);
        this.tankID = tankID;
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

    public void update(GameWorld gw) {
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
        if(lifeCounter>lives){
            gw.anims.add(new Animations(tankCenterX(),tankCenterY(),ResourceManager.getAnim("bullethit")));
            lifeCounter = lives;
        }

        long currTime = System.currentTimeMillis();
        if(this.ShootPressed && currTime > this.timeSinceLastShot + this.coolDown){
            this.timeSinceLastShot = currTime;
            var p = ResourcePools.getPooldInstance("bullet");
            p.initObject(
                    safeShootX(),
                    safeShootY(),
                       angle);
            Bullet b = ((Bullet)p);
            b.setOwner(this.tankID);
            gw.addGameObject(b);
            ResourceManager.getSound("shotFired").play();
            gw.anims.add(new Animations(safeShootX(),safeShootY(),ResourceManager.getAnim("bulletshoot")));
        }


        centerScreen();
        this.hitbox.setLocation((int)this.x, (int)this.y);

    }
    private float safeShootX(){
        return (int) Math.round((this.getX() + 25) + 35 * Math.cos(Math.toRadians( this.angle )));    }
    private float safeShootY(){
        return (int) Math.round((this.getY() + 25) + 35 * Math.sin(Math.toRadians( this.angle )));
    }
    public float tankCenterX(){
        return this.x + this.img.getWidth()/2f;
    }
    public float tankCenterY(){
        return this.y + this.img.getHeight()/2f;
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


    }

    public void handleCollision(GameObject by){
        if(by instanceof Bullet b){
            if(b.checkOwner()!= this.tankID){
                this.lives--;

            }
        }else if (by instanceof Wall w){
            //stop undo move
        }else if (by instanceof PowerUp p){
            p.apply(this);
        }
    }

    public void setSpeed(float speed){
        this.R = speed;
    }

    public void increaseHealth(int health){
        this.lives ++;
    }

    public int getTankID(){
        return tankID;
    }

    public float getTankAngle(){
        return this.angle;
    }




}
