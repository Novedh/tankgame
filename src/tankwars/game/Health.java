package tankwars.game;


import java.awt.image.BufferedImage;


public class Health extends GameObject{

    public Health(float x , float y, BufferedImage img){
        super(x,y,img);
    }

    @Override
    public void handleCollision(GameObject obj2) {

    }

}
