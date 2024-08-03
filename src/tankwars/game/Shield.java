package tankwars.game;


import tankwars.ResourceManager;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.Timer;
import java.util.TimerTask;

public class Shield extends GameObject implements PowerUp{

    private Timer shieldTime;
    private int shieldDur = 10000;
    private double scale = 1.8;

    public Shield(float x , float y, BufferedImage img){
        super(x,y,img);
        hitbox.height = (int) (img.getHeight() * scale);
        hitbox.width = (int) (img.getWidth() * scale);


    }

    @Override
    public void handleCollision(GameObject obj2) {

    }

    @Override
    public void apply(GameObject obj) {

        if(obj instanceof Tank t){
            if(!t.shieldOn){
                t.shieldOn = true;

                shieldTime = new Timer();
                shieldTime.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        t.shieldOn = false;
                    }
                }, shieldDur);

            }

            hasCollided = true;
        }
    }
    public void drawImage(Graphics g){
        Graphics2D g2d = (Graphics2D) g;

        AffineTransform transform = new AffineTransform();
        transform.translate((int) (x), (int) (y));
        transform.scale(scale, scale);

        g2d.drawImage(this.img, transform, null);


    }

}
