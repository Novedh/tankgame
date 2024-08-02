package tankwars.game;


import java.awt.image.BufferedImage;


public class Wall extends GameObject{

    public Wall(float x , float y, BufferedImage img){
        super(x,y,img);
    }

    @Override
    public void handleCollision(GameObject obj2) {

    }

}
