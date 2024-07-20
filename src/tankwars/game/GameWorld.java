package tankwars.game;


import tankwars.GameConstants;
import tankwars.Launcher;
import tankwars.ResourceManager;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.Buffer;
import java.sql.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

/**
 * @author anthony-pc
 */
public class GameWorld extends JPanel implements Runnable {

    private BufferedImage world;
    private Tank t1;
    private Tank t2;
    private final Launcher lf;
    private long tick = 0;
    ArrayList<GameObject> gObjs = new ArrayList<>(1000);

    /**
     *
     */
    public GameWorld(Launcher lf) {
        this.lf = lf;
    }

    @Override
    public void run() {
        try {
            while (true) {
                this.tick++;
                this.t1.update();
                this.t2.update();// update tank
                this.repaint();   // redraw tankwars.game
                /*
                 * Sleep for 1000/144 ms (~6.9ms). This is done to have our 
                 * loop run at a fixed rate per/sec. 
                */
                Thread.sleep(1000 / 144);
            }
        } catch (InterruptedException ignored) {
            System.out.println(ignored);
        }
    }

    /**
     * Reset tankwars.game to its initial state.
     */
    public void resetGame() {
        this.tick = 0;
        this.t1.setX(300);
        this.t1.setY(300);
    }

    /**
     * Load all resources for Tank Wars Game. Set all Game Objects to their
     * initial state as well.
     */
    public void InitializeGame() {
        this.world = new BufferedImage(GameConstants.GAME_MAP_WIDTH,
                GameConstants.GAME_MAP_HEIGHT,
                BufferedImage.TYPE_INT_RGB);

        int row = 0;

        InputStreamReader isr = new InputStreamReader(
                Objects.requireNonNull(
                        ResourceManager.class.getClassLoader().getResourceAsStream("map/map1.csv")

                )
        );

        try (BufferedReader mapReader = new BufferedReader(isr)){
            while(mapReader.ready()){
                String line = mapReader.readLine();
                String[] objs = line.split(",");
                for(int col = 0; col < objs.length ;col++){
                    String gameItem = objs[col];
                    if(gameItem.equals("0")){
                        continue;
                    }
                    this.gObjs.add(GameObject.newInstance(gameItem,col*32,row*32));
                }
                row++;
            }
        } catch (IOException e){
            throw new RuntimeException(e);
        }

        BufferedImage t1img = null;
        try {
            /*
             * note class loaders read files from the out folder (build folder in Netbeans) and not the
             * current working directory. When running a jar, class loaders will read from within the jar.
             */
            t1img = ImageIO.read(
                    Objects.requireNonNull(GameWorld.class.getClassLoader().getResource("tank1.png"),
                    "Could not find tank1.png")
            );
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
            ex.printStackTrace();
        }

        t1 = new Tank(300, 300, 0, 0, (short) 0, ResourceManager.getSprite("t1"));
        TankControl tc1 = new TankControl(t1, KeyEvent.VK_W, KeyEvent.VK_S, KeyEvent.VK_A, KeyEvent.VK_D, KeyEvent.VK_SPACE);
        this.lf.getJf().addKeyListener(tc1);
        t2 = new Tank(400, 400, 0, 0, (short) 0, ResourceManager.getSprite("t2"));
        TankControl tc2 = new TankControl(t2, KeyEvent.VK_UP, KeyEvent.VK_DOWN, KeyEvent.VK_LEFT, KeyEvent.VK_RIGHT, KeyEvent.VK_ENTER);
        this.lf.getJf().addKeyListener(tc2);
    }

    static double scaleFactor = .12;

    private void displayMiniMap(Graphics2D onScreenPanel) {
        BufferedImage mm = this.world.getSubimage(0,0,GameConstants.GAME_MAP_WIDTH,GameConstants.GAME_MAP_HEIGHT);
        double mmx = (GameConstants.GAME_SCREEN_WIDTH/2) - (GameConstants.GAME_MAP_WIDTH*(scaleFactor))/2;
        double mmy = (GameConstants.GAME_SCREEN_HEIGHT) - (28+GameConstants.GAME_MAP_HEIGHT*(scaleFactor));
        AffineTransform scaler = AffineTransform.getTranslateInstance(mmx,mmy);
        scaler.scale(scaleFactor,scaleFactor);
        onScreenPanel.drawImage(mm,scaler,null);
    }

    private void displaySplitScreen(Graphics2D onScreenPanel){
        BufferedImage lh = this.world.getSubimage((int)this.t1.getScreenX(),(int)this.t1.getScreenY(),
                GameConstants.GAME_SCREEN_WIDTH/2,GameConstants.GAME_SCREEN_HEIGHT);
        BufferedImage rh = this.world.getSubimage((int)this.t2.getScreenX(),(int)this.t2.getScreenY(),
                GameConstants.GAME_SCREEN_WIDTH/2,GameConstants.GAME_SCREEN_HEIGHT);
        onScreenPanel.drawImage(lh,0,0,null);
        onScreenPanel.drawImage(rh,GameConstants.GAME_SCREEN_WIDTH/2+3,0,null);

    }

    private void renderFloor(Graphics buffer){
        BufferedImage floor = ResourceManager.getSprite("floor");
        for(int i = 0; i<GameConstants.GAME_MAP_WIDTH;i+=320){
            for(int j =0; j<GameConstants.GAME_MAP_HEIGHT;j+=240){
                buffer.drawImage(floor,i,j,null);
            }
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        Graphics2D buffer = world.createGraphics();
        this.renderFloor(buffer);
        this.gObjs.forEach(go->go.drawImage(buffer));
        this.t1.drawImage(buffer);
        this.t2.drawImage(buffer);

//        g2.drawImage(world, 0, 0, null);
        this.displaySplitScreen(g2);
        this.displayMiniMap(g2);

    }

}
