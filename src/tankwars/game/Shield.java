package tankwars.game;


import java.awt.image.BufferedImage;

public class Shield extends GameObject{


    public Shield(float x , float y, BufferedImage img){
        super(x,y,img);

    }

    @Override
    public void handleCollision(GameObject obj2) {

    }

}