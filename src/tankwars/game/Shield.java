package tankwars.game;

import tankwars.ResourceManager;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Shield extends GameObject{

    float x,y;
    BufferedImage img;

    public Shield(float x , float y, BufferedImage img){
        this.x = x;
        this.y = y;
        this.img = img;
    }
    public void drawImage(Graphics g){
        g.drawImage(this.img, (int)x, (int)y,null);
    }
}
