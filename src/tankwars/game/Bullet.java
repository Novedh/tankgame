package tankwars.game;

import tankwars.GameConstants;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

public class Bullet extends GameObject implements Poolable{



    private float vx;
    private float vy;
    private float angle;
    private int owner;
    private boolean hasCollided = false;

    private float R = 5;
    private float ROTATIONSPEED = 3.0f;

    Bullet(BufferedImage img) {

        super(0,0,img);
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


    void update() {
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



    public void drawImage(Graphics g) {
        AffineTransform rotation = AffineTransform.getTranslateInstance(x, y);
        rotation.rotate(Math.toRadians(angle), this.img.getWidth() / 2.0, this.img.getHeight() / 2.0);
        Graphics2D g2d = (Graphics2D) g;
        g2d.drawImage(this.img, rotation, null);
        g2d.setColor(Color.RED);
        //g2d.rotate(Math.toRadians(angle), bounds.x + bounds.width/2, bounds.y + bounds.height/2);
        g2d.drawRect((int)x,(int)y,this.img.getWidth(), this.img.getHeight());

    }

    public void handleCollision(Object with){
        if(with instanceof Bullet b){
            //lose hp
        }else if (with instanceof Wall w){
            //stop undo move
        }else if (with instanceof Speed sp){
            //increase speed
        }else if(with instanceof Health hl){
            //add a heart
        }else if(with instanceof Shield){
            //add shield
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
