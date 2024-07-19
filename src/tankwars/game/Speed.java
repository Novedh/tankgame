package tankwars.game;

import tankwars.ResourceManager;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;

public class Speed extends GameObject{

    float x,y;
    BufferedImage img;

    public Speed(float x , float y, BufferedImage img){
        this.x = x;
        this.y = y;
        this.img = img;
    }
    public void drawImage(Graphics g){
        g.drawImage(this.img, (int)x, (int)y,null);
    }
}
