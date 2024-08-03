package tankwars;

import tankwars.game.Sound;

import javax.imageio.ImageIO;
import javax.sound.sampled.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.*;

public class ResourceManager {
    private final static Map<String, BufferedImage> sprites = new HashMap<>();
    private final static Map<String, Sound> sounds = new HashMap<>();
    private final static Map<String, List<BufferedImage>> anims = new HashMap<>();
    private final static Map<String,Integer> animInfo = new HashMap<>(){{
        put("bullethit", 24);
        put("bulletshoot", 24);
        put("powerpick", 32);
        put("puffsmoke", 32);
        put("rocketflame", 16);
        put("rockethit", 32);
        put("tracks",11);

    }};


    private static BufferedImage loadSprite(String path) throws IOException {
        return ImageIO.read(
                Objects.requireNonNull(
                        ResourceManager.class.getClassLoader().getResource(path),
                        "Resource %s was not found".formatted(path))
        );
    }

    private static Sound loadSound(String path) throws IOException, LineUnavailableException, UnsupportedAudioFileException {

        AudioInputStream ais = AudioSystem.getAudioInputStream(
                Objects.requireNonNull(
                        ResourceManager.class.getClassLoader().getResource(path),
                        "Sound Resource %s not found".formatted(path)
                ));

        Clip c = AudioSystem.getClip();
        c.open(ais);
        Sound s = new Sound(c);

        return s;
    }

    public static void loadSprites() throws IOException {
        ResourceManager.sprites.put("t1", loadSprite("tank1.png"));
        ResourceManager.sprites.put("t2", loadSprite("tank2.png"));
        ResourceManager.sprites.put("bullet", loadSprite("bullet.png"));
        ResourceManager.sprites.put("shield", loadSprite("resources/powerups/shield.png"));
        ResourceManager.sprites.put("speed", loadSprite("resources/powerups/speed.png"));
        ResourceManager.sprites.put("health" ,loadSprite("resources/powerups/health.png"));
        ResourceManager.sprites.put("menu" , loadSprite("Title.bmp"));
        ResourceManager.sprites.put("tank1win" , loadSprite("tank1menu.png"));
        ResourceManager.sprites.put("tank2win" , loadSprite("tank2menu.png"));
        ResourceManager.sprites.put("bwall" , loadSprite("wall2.png"));
        ResourceManager.sprites.put("uwall" , loadSprite("wall1.png"));
        ResourceManager.sprites.put("floor", loadSprite("Background.bmp"));
        ResourceManager.sprites.put("heart",loadSprite("heart.png"));
        ResourceManager.sprites.put("bubble",loadSprite("bubble.png"));

    }

    public static void loadSounds(){
        try {
            ResourceManager.sounds.put("bg", loadSound("resources/sounds/Music.mid"));
            ResourceManager.sounds.put("bulletCollide",loadSound("resources/sounds/bullet.wav"));
            ResourceManager.sounds.put("pickup", loadSound("resources/sounds/pickup.wav"));
            ResourceManager.sounds.put("shotFired",loadSound("resources/sounds/shotfiring.wav"));
            ResourceManager.sounds.put("moving",loadSound("resources/sounds/tank-moving.wav"));

        } catch (IOException | UnsupportedAudioFileException | LineUnavailableException e) {
            throw new RuntimeException(e);
        }
    }



    public static void loadAnims(){
        String baseFormat = "resources/animations/%s/%s_%04d.png";
        ResourceManager.animInfo.forEach((animationName,frameCount)-> {
            List<BufferedImage> f = new ArrayList<>(frameCount);
            try {
            for(int i = 0; i< frameCount; i++){
                String spritePath = String.format(baseFormat, animationName, animationName, i);
                f.add(loadSprite(spritePath));
                }
            ResourceManager.anims.put(animationName,f);
            }catch (IOException e) {
                throw new RuntimeException(e);
            }

        });
    }


    public static BufferedImage getSprite(String key){
        if(!ResourceManager.sprites.containsKey(key)){
            throw new IllegalArgumentException(
                    "Resource %s is not in map".formatted(key)
            );
        }
        return ResourceManager.sprites.get(key);
    }

    public static Sound getSound(String key){
        if(!ResourceManager.sounds.containsKey(key)){
            throw new IllegalArgumentException(
                    "Resource %s is not in map".formatted(key)
            );
        }
        return ResourceManager.sounds.get(key);
    }
    public static List<BufferedImage> getAnim(String key){
        if(!ResourceManager.anims.containsKey(key)){
            throw new IllegalArgumentException(
                    "Resource %s is not in map".formatted(key)
            );
        }
        return ResourceManager.anims.get(key);
    }

    public static void loadAssets(){
        try {
            loadSprites();
            loadSounds();
            loadAnims();
            System.out.println();

        } catch (IOException e) {
            throw new RuntimeException("Loading assets failed, e");
        }
    }


}
