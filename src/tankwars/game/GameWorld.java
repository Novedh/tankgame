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
import java.util.ArrayList;
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
    ArrayList<Animations> anims = new ArrayList<>();
    ArrayList<Animations> tankTracks = new ArrayList<>();


    /**
     *
     */
    public GameWorld(Launcher lf) {
        this.lf = lf;
    }

    @Override
    public void run() {
        this.resetGame();
        Sound bg = ResourceManager.getSound("bg");
        bg.setVolume(0.15f);
        bg.loopContinuously();
        bg.play();

        try {

            while (true) {
                this.tick++;


                this.tankTracks.add(new Animations(t1.tankCenterX(), t1.tankCenterY(), ResourceManager.getAnim("tracks")));
                this.tankTracks.add(new Animations(t2.tankCenterX(), t2.tankCenterY(), ResourceManager.getAnim("tracks")));

                for(int i = this.gObjs.size()-1; i>=0 ;i--){
                    if(this.gObjs.get(i) instanceof Updatable u){
                        u.update(this);
                    }else{
                        break;
                    }
                }

                if(t1.getHealth() <=0 ){
                    t1.loseLife();
                    resetMatch();

                }
                if(t2.getHealth() <=0 ){
                    t2.loseLife();
                    resetMatch();

                }
                if(t2.getLife() == 0 ){
                    resetGame();
                    lf.setFrame("endtank1");
                }
                if(t1.getLife() == 0 ){
                    resetGame();
                    lf.setFrame("endtank2");
                }


                this.checkCollisions();
                for(int i =0; i< this.anims.size(); i++){
                    this.anims.get(i).update();
                }
                for(int i =0; i< this.tankTracks.size(); i++){
                    this.tankTracks.get(i).update();
                }
                this.gObjs.removeIf(g->g.getHasCollided());
                this.tankTracks.removeIf(g->!g.runStatus());
                this.anims.removeIf(g->!g.runStatus());


                this.renderFrame();
                this.repaint();   // redraw tankwars.game
                /*
                 * Sleep for 1000/144 ms (~6.9ms). This is done to have our 
                 * loop run at a fixed rate per/sec. 
                */
                Thread.sleep(12);
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
        this.gObjs.clear();
        this.anims.clear();
        this.tankTracks.clear();
        t1.reset();
        t2.reset();
        InitializeGame();
    }

    public void resetMatch(){
        Tank temp1 = t1;
        Tank temp2 = t2;
        this.gObjs.clear();
        this.anims.clear();
        this.tankTracks.clear();
        reloadMap();
        t1 = temp1;
        t2 = temp2;
        t1.respawn();
        t2.respawn();
        this.gObjs.add(t1);
        this.gObjs.add(t2);


    }

    /**
     * Load all resources for Tank Wars Game. Set all Game Objects to their
     * initial state as well.
     */
    public void InitializeGame() {
        this.world = new BufferedImage(GameConstants.GAME_MAP_WIDTH,
                GameConstants.GAME_MAP_HEIGHT,
                BufferedImage.TYPE_INT_RGB);

        reloadMap();



        t1 = new Tank(GameConstants.TANK1X, GameConstants.TANK1Y, 0, 0, (short) 0, ResourceManager.getSprite("t1"),0);
        TankControl tc1 = new TankControl(t1, KeyEvent.VK_W, KeyEvent.VK_S, KeyEvent.VK_A, KeyEvent.VK_D, KeyEvent.VK_SPACE);
        this.lf.getJf().addKeyListener(tc1);
        t2 = new Tank(GameConstants.TANK2X, GameConstants.TANK2Y, 0, 0, (short) 0, ResourceManager.getSprite("t2"),1);
        TankControl tc2 = new TankControl(t2, KeyEvent.VK_UP, KeyEvent.VK_DOWN, KeyEvent.VK_LEFT, KeyEvent.VK_RIGHT, KeyEvent.VK_ENTER);
        this.lf.getJf().addKeyListener(tc2);

        this.gObjs.add(t1);
        this.gObjs.add(t2);
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


    private void renderFrame() {
        Graphics2D buffer = world.createGraphics();
        this.renderFloor(buffer);
        for(int i =0; i< this.tankTracks.size(); i++){
            this.tankTracks.get(i).render(buffer);
        }
        for(int i =0; i< this.gObjs.size();i++){
            GameObject obj = this.gObjs.get(i);
            obj.drawImage(buffer);
            if(obj instanceof Tank){
                ((Tank) obj).drawHealth(buffer);
                ((Tank) obj).drawHeart(buffer);
                ((Tank) obj).drawShield(buffer);

            }
        }
        for(int i =0; i< this.anims.size(); i++){
            this.anims.get(i).render(buffer);
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;

//        g2.drawImage(world, 0, 0, null);
        this.displaySplitScreen(g2);
        this.displayMiniMap(g2);

    }

    private void checkCollisions(){
        for(int i = 0; i < this.gObjs.size(); i++){

            GameObject obj1 = this.gObjs.get(i);
            if(!(obj1 instanceof Updatable)){
                continue;
            }
            for(int j =0; j < this.gObjs.size(); j++){
                if(i == j)continue;
                GameObject obj2 = this.gObjs.get(j);
                if(obj1.getHitbox().intersects(obj2.getHitbox())){
                    obj1.handleCollision(obj2);
                }
            }
        }
    }

    public void addGameObject(GameObject g){
        this.gObjs.add(g);
    }

    public void removeGameObject(GameObject g){
        this.gObjs.remove(g);
    }

    private void reloadMap(){
        int row = 0;

        InputStreamReader isr = new InputStreamReader(
                Objects.requireNonNull(
                        ResourceManager.class.getClassLoader().getResourceAsStream("map/map2.csv")

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
    }

}
