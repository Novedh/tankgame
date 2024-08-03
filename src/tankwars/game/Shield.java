package tankwars.game;


import tankwars.ResourceManager;

import java.awt.image.BufferedImage;
import java.util.Timer;
import java.util.TimerTask;

public class Shield extends GameObject implements PowerUp{

    private Timer shieldTime;
    private int shieldDur = 5000;

    public Shield(float x , float y, BufferedImage img){
        super(x,y,img);

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
}
