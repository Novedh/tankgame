package tankwars.game;


import java.awt.image.BufferedImage;


public class Wall extends GameObject{

    public Wall(float x , float y, BufferedImage img){
        this.x = x;
        this.y = y;
        this.img = img;
    }

}
