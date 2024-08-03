package tankwars.game;


import java.awt.image.BufferedImage;


public class Health extends GameObject implements PowerUp{

    public Health(float x , float y, BufferedImage img){
        super(x,y,img);
    }

    @Override
    public void handleCollision(GameObject obj) {

    }

    @Override
    public void apply(GameObject obj) {
        if(obj instanceof Tank t){
            t.gainHealth(60);
            this.hasCollided=true;
        }
    }
}
