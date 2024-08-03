package tankwars.game;


import java.awt.image.BufferedImage;
import java.util.Timer;
import java.util.TimerTask;


public class Speed extends GameObject implements PowerUp{

    private float normSpeed = 5;
    private float boostSpeed = 7;
    private long speedDuration = 12000;
    private Timer speedTime;
    private Boolean isBoosted = false;

    public Speed(float x , float y, BufferedImage img){
        super(x,y,img);
        this.hitbox.setLocation((int)this.x,(int)this.y);
    }

    @Override
    public void handleCollision(GameObject obj2) {

    }

    @Override
    public void apply(GameObject obj) {
        if(obj instanceof Tank t){
            if(!isBoosted){
                isBoosted = true;
                t.setSpeed(boostSpeed);
                speedTime = new Timer();
                speedTime.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        t.setSpeed(normSpeed);
                    }
                }, speedDuration);
            }
            this.hasCollided=true;

        }
        }

    }

