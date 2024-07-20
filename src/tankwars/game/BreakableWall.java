package tankwars.game;


import java.awt.image.BufferedImage;


public class BreakableWall extends GameObject{

    public BreakableWall(float x , float y, BufferedImage img){
        this.x = x;
        this.y = y;
        this.img = img;
    }

}
