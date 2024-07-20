package tankwars;

import javax.imageio.ImageIO;
import javax.sound.sampled.Clip;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.*;

public class ResourceManager {
    private final static Map<String, BufferedImage> sprites = new HashMap<>();
    private final static Map<String, Clip> sounds = new HashMap<>();
    private final static Map<String, List<BufferedImage>> anims = new HashMap<>();

    private static BufferedImage loadSprite(String path) throws IOException {
        return ImageIO.read(
                Objects.requireNonNull(
                        ResourceManager.class.getClassLoader().getResource(path),
                        "Resource %s was not found".formatted(path))
        );
    }
    public static void initSprites() throws IOException {
        ResourceManager.sprites.put("t1", loadSprite("tank1.png"));
        ResourceManager.sprites.put("t2", loadSprite("tank2.png"));
        ResourceManager.sprites.put("rocket", loadSprite("Rocket.gif"));
        ResourceManager.sprites.put("shield", loadSprite("shield.png"));
        ResourceManager.sprites.put("speed", loadSprite("speed.png"));
        ResourceManager.sprites.put("health" ,loadSprite("health.png"));
        ResourceManager.sprites.put("menu" , loadSprite("Title.bmp"));
        ResourceManager.sprites.put("bwall" , loadSprite("wall2.png"));
        ResourceManager.sprites.put("uwall" , loadSprite("wall1.png"));
        ResourceManager.sprites.put("floor", loadSprite("Background.bmp"));
    }

    public static void loadAssets(){
        try {
            initSprites();
        } catch (IOException e) {
            throw new RuntimeException("Loading assets failed, e");
        }
    }

    public static BufferedImage getSprite(String key){
        if(!ResourceManager.sprites.containsKey(key)){
            throw new IllegalArgumentException(
                    "Resource %s is not in map".formatted(key)
            );
        }
        return ResourceManager.sprites.get(key);
    }






}
