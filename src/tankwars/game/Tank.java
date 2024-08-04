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
    private int health = 100;
    int tankID;
    private static ResourcePool<Bullet> bulletPool = new ResourcePool<>("bullet", Bullet.class,500);
    private float screenX;
    private float screenY;

    private float startX;
    private float startY;
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
    private boolean isMoving = false;
    private float tankIdleVolume = .01f;
    private float tankMovingVolume = .02f;


    public boolean shieldOn = false;
    private long coolDown = 800;
    private long timeSinceLastShot = 0;
    Sound moving = ResourceManager.getSound("moving");

    Tank(float x, float y, float vx, float vy, float angle, BufferedImage img, int tankID) {
        super(x,y,img);
        this.startX = x;
        this.startY = y;
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
        boolean currMoving = false;

        if (this.UpPressed) {
            this.moveForwards();
            currMoving = true;
        }

        if (this.DownPressed) {
            this.moveBackwards();
            currMoving = true;
        }

        if (this.LeftPressed) {
            this.rotateLeft();
        }

        if (this.RightPressed) {
            this.rotateRight();
        }


        if(currMoving && !isMoving){
            moving.setVolume(tankMovingVolume);
            moving.loopContinuously();
            isMoving = true;
        }else if(!currMoving && isMoving){
            moving.setVolume(tankIdleVolume);
            isMoving = false;
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
            ResourceManager.getSound("shotFired").setVolume(2f);
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
        //g2d.setColor(Color.RED);
        //g2d.rotate(Math.toRadians(angle), bounds.x + bounds.width/2, bounds.y + bounds.height/2);
        //g2d.drawRect((int)x,(int)y,this.img.getWidth(), this.img.getHeight());


    }

    public void handleCollision(GameObject by){
        if(by instanceof Bullet b){
            if(b.checkOwner()!= this.tankID){
                if(!shieldOn) {
                    this.health -= 34;
                }
            }
        }else if (by instanceof Wall w){
            wallBlock();
        }else if (by instanceof BreakableWall bw){
            wallBlock();
        }else if (by instanceof PowerUp p){
            p.apply(this);
        }else if (by instanceof Tank t){
            wallBlock();
        }
    }

    private void wallBlock(){
        if (this.UpPressed) {
            if (vx != 0) {
                x -= vx;
            }
            if (vy != 0) {
                y -= vy;
            }
        }
        if (this.DownPressed) {
            if (vx != 0) {
                x += vx;
            }
            if (vy != 0) {
                y += vy;
            }
        }
    }

    public void setSpeed(float speed){
        this.R = speed;
    }

    public void gainHealth(int health){
        this.health += health;
        if(this.health>100){
            this.health=100;
        }
    }

    public int getTankID(){
        return tankID;
    }

    public float getTankAngle(){
        return this.angle;
    }

    public void respawn(){
        this.setX(this.startX);
        this.setY(this.startY);
        this.health = 100;
        this.hitbox.setLocation((int)startX,(int)startY);

    }

    public void reset(){
        this.setX(this.startX);
        this.setY(this.screenY);
        this.health = 100;
        this.hitbox.setLocation((int)startX,(int)startY);
        this.lives = 3;

    }

    public int getHealth(){
        return health;
    }

    public void loseLife(){
        lives--;
    }


    public int getLife() {
        return lives;
    }

    public void drawHealth(Graphics g){
        int barWidth = 50;
        int barHeight = 5;
        int barX = (int) this.x + (this.img.getWidth() - barWidth) / 2;
        int barY = (int) this.y - barHeight - 5;

        float healthPercent = (float) this.health / 100;

        g.setColor(Color.RED);
        g.fillRect(barX, barY, barWidth, barHeight);

        g.setColor(Color.GREEN);
        g.fillRect(barX, barY, (int) (barWidth * healthPercent), barHeight);

        g.setColor(Color.BLACK);
        g.drawRect(barX, barY, barWidth, barHeight);

    }

    public void drawHeart(Graphics g){
        BufferedImage heart = ResourceManager.getSprite("heart") ;
        int lifeWidth = heart.getWidth();
        int lifeHeight = heart.getHeight();
        int spacing = 5;

        int x = (int) this.x + (this.img.getWidth() - (lifeWidth * lives + (lives  - 1) * lives)) / 2;
        int y = (int) this.y - lifeHeight - 10;

        for (int i = 0; i < lives; i++) {
            g.drawImage(heart, x, y, null);
            x += lifeWidth + spacing;
        }
    }
    public void drawShield(Graphics g) {
        BufferedImage shield = ResourceManager.getSprite("bubble") ;
        float bubbleX =tankCenterX() - shield.getWidth()/2f;
        float bubbleY =tankCenterY() - shield.getHeight()/2f;
        if(shieldOn){
            g.drawImage(shield,(int)bubbleX,(int)bubbleY,null);

        }

    }
}
