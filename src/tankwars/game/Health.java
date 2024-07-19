package tankwars.game;

import tankwars.ResourceManager;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;

public class Health extends GameObject{

    float x,y;
    BufferedImage img;

    public Health(float x , float y, BufferedImage img){
        this.x = x;
        this.y = y;
        this.img = img;
    }
    public void drawImage(Graphics g){
        g.drawImage(this.img, (int)x, (int)y,null);
    }
}
