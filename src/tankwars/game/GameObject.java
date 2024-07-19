package tankwars.game;

import tankwars.ResourceManager;

import java.awt.*;
import java.awt.image.BufferedImage;

public abstract class GameObject {
    protected float x,y;
    protected BufferedImage img;

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
        public abstract void drawImage(Graphics g);
    }

