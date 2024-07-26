package tankwars;

import tankwars.game.*;
import tankwars.menus.EndGamePanel;
import tankwars.menus.StartMenuPanel;
import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowEvent;

public class Launcher {

    /*
     * Main panel in JFrame, the layout of this panel
     * will be card layout, this will allow us to switch
     * to sub-panels depending on tankwars.game state.
     */
    private JPanel mainPanel;
    /*
     * tankwars.game panel is used to show our tankwars.game to the screen. inside this panel
     * also contains the tankwars.game loop. This is where out objects are updated and
     * redrawn. This panel will execute its tankwars.game loop on a separate thread.
     * This is to ensure responsiveness of the GUI. It is also a bad practice to
     * run long-running loops(or tasks) on Java Swing's main thread. This thread is
     * called the event dispatch thread.
     */
    private GameWorld gamePanel;
    /*
     * JFrame used to store our main panel. We will also attach all event
     * listeners to this JFrame.
     */
    private final JFrame jf;
    /*
     * CardLayout is used to manage our sub-panels. This is a layout manager
     * used for our tankwars.game. It will be attached to the main panel.
     */
    private CardLayout cl;

    public Launcher(){
        this.jf = new JFrame();             // creating a new JFrame object
        this.jf.setTitle("Tank Wars Game"); // setting the title of the JFrame window.
        // when the GUI is closed, this will also shut down the VM
        this.jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    private void initUIComponents(){
        this.mainPanel = new JPanel(); // create a new main panel
        /*
         * start panel will be used to view the start menu. It will contain
         * two buttons start and exit.
         */
        JPanel startPanel = new StartMenuPanel(this); // create a new start panel
        this.gamePanel = new GameWorld(this); // create a new tankwars.game panel
        this.gamePanel.InitializeGame(); // initialize tankwars.game, but DO NOT start tankwars.game
        /*
         * end panel is used to show the end tankwars.game panel.  it will contain
         * two buttons restart and exit.
         */
        JPanel endPanel = new EndGamePanel(this); // create a new end tankwars.game pane;
        cl = new CardLayout(); // creating a new CardLayout Panel
        this.mainPanel.setLayout(cl); // set the layout of the main panel to our card layout
        this.mainPanel.add(startPanel, "start"); //add the start panel to the main panel
        this.mainPanel.add(gamePanel, "game");   //add the tankwars.game panel to the main panel
        this.mainPanel.add(endPanel, "end");    // add the end tankwars.game panel to the main panel
        this.jf.add(mainPanel); // add the main panel to the JFrame
        this.jf.setResizable(false); //make the JFrame not resizable
        this.setFrame("start"); // set the current panel to start panel
    }

    public void setFrame(String type){
        this.jf.setVisible(false); // hide the JFrame
        switch (type) {
            case "start" ->
                // set the size of the jFrame to the expected size for the start panel
                    this.jf.setSize(GameConstants.START_MENU_SCREEN_WIDTH, GameConstants.START_MENU_SCREEN_HEIGHT);
            case "game" -> {
                // set the size of the jFrame to the expected size for the tankwars.game panel
                this.jf.setSize(GameConstants.GAME_SCREEN_WIDTH, GameConstants.GAME_SCREEN_HEIGHT);
                //start a new thread for the tankwars.game to run. This will ensure our JFrame is responsive and
                // not stuck executing the tankwars.game loop.
                (new Thread(this.gamePanel)).start();
            }
            case "end" ->
                // set the size of the jFrame to the expected size for the end panel
                    this.jf.setSize(GameConstants.END_MENU_SCREEN_WIDTH, GameConstants.END_MENU_SCREEN_HEIGHT);
        }
        this.cl.show(mainPanel, type); // change current panel shown on main panel tp the panel denoted by type.
        this.jf.setVisible(true); // show the JFrame
    }

    public JFrame getJf() {
        return jf;
    }

    public void closeGame(){
        this.jf.dispatchEvent(new WindowEvent(this.jf, WindowEvent.WINDOW_CLOSING));
    }

    public static void main(String[] args) {
        ResourceManager.loadAssets();
        ResourcePools.addPool("bullet",new ResourcePool<>("bullet", Bullet.class,500).fillPool(500));
        (new Launcher()).initUIComponents();

    }
}