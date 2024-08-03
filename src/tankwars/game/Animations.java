package tankwars.game;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.List;


public class Animations {
    private float x,y;
    private List<BufferedImage> frames;
    private int currentFrames;
    private long timeSinceLastUpdate =0;
    private long delay = 30;
    private Boolean running = false;

    public Animations(float x, float y , List<BufferedImage> frames){
        this.x = x - frames.get(0).getWidth()/2f;
        this.y = y - frames.get(0).getHeight()/2f;
        this.frames = frames;
        this.running = true;
        this.currentFrames = 0;
    }



    public void update(){
        long currentTime = System.currentTimeMillis();

        if(this.timeSinceLastUpdate+delay < currentTime){
            this.currentFrames++;
            if(this.currentFrames == this.frames.size()){
                this.running = false;
            }
            this.timeSinceLastUpdate = currentTime;
        }
    }


    public void render(Graphics g){
        if(this.running){
            g.drawImage(this.frames.get(currentFrames), (int)x, (int)y, null);
        }
    }

    public boolean runStatus(){
        return running;
    }




}
