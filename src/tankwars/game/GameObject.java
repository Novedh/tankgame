package tankwars.game;

import org.w3c.dom.css.Rect;
import tankwars.ResourceManager;

import java.awt.*;
import java.awt.image.BufferedImage;

public abstract class GameObject {
    protected float x,y;
    protected BufferedImage img;
    protected Rectangle hitbox;
    protected boolean hasCollided = false;

    public GameObject(float x, float y, BufferedImage img){
        this.x = x;
        this.y = y;
        this.img = img;
        hitbox = new Rectangle((int)x,(int)y,img.getWidth(),img.getHeight());
    }

    public static GameObject newInstance(String type,float x, float y){
        return switch (type) {
            case "3", "9" -> new Wall(x, y, ResourceManager.getSprite("uwall"));
            case "2" -> new BreakableWall(x,y,ResourceManager.getSprite("bwall"));
            case "4" -> new Health(x,y,ResourceManager.getSprite("health"));
            case "5" -> new Speed(x,y,ResourceManager.getSprite("speed"));
            case "6" -> new Shield(x,y,ResourceManager.getSprite("shield"));
            default -> throw new IllegalArgumentException("unsupported type -> %s\n".formatted(type));
        };
        }
        public void drawImage(Graphics g){
            g.drawImage(this.img, (int)x, (int)y,null);
        }
        public Rectangle getHitbox(){
            return hitbox.getBounds();
        }
    }

