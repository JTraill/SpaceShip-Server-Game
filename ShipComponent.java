import javax.swing.JComponent;
import javax.swing.JOptionPane;
import java.lang.Object;
import java.text.AttributedCharacterIterator;
import java.util.Random;
import javax.swing.JComponent;
import java.awt.geom.*;
import java.awt.image.ImageObserver;
import java.awt.*;
import javax.swing.KeyStroke;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import java.awt.event.*;
import java.util.Random;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.File;
import java.io.PrintWriter;
import java.io.*;
import java.net.*;
public class ShipComponent extends FrameViewer implements Shippable{
    private Color[] colors = {
        Color.ORANGE,
        Color.BLUE,
        Color.YELLOW,
        Color.GREEN,
        Color.CYAN,
        Color.PINK
    };
    private DataInputStream fromServer;
    private DataOutputStream toServer;
    private boolean invincible;
    private int invincibleCounter;
    private boolean gameOver;
    private int cometProbability;
    private int pelletProbability;
    private final int FRAME_WIDTH = 1300;
    private final int FRAME_HEIGHT = 800;
    private int score = 0;
    private String difficulty;
    private JLabel scoreLabel = new JLabel("Score: " + score);
    private int x;
    private int y;
    private Color c;
    private int response;
    private int cometDY;
    private int pelletDY;
    private int plane1 = FRAME_WIDTH / 5;
    private int plane2 = FRAME_WIDTH / 2;
    private int plane3 = FRAME_WIDTH - 250;
    private int[] planes = {
        plane1,
        plane2,
        plane3
    };
    private ArrayList < Integer > scores = new ArrayList < Integer > ();
    private ArrayList < Pellet > pellets = new ArrayList < Pellet > ();
    private ArrayList < Comet > comets = new ArrayList < Comet > ();
    private ArrayList < Screen > stars = new ArrayList < Screen > ();
    private ArrayList < Bullet > bullets = new ArrayList < Bullet > ();
    public ShipComponent(String difficulty, int x, int y, Color c, boolean gameOver, int cometProbability, int pelletProbability) {
        score=-2;
        this.difficulty = difficulty;
        this.x = x;
        this.y = y;
        this.c = c;
        this.gameOver = gameOver;
        this.cometProbability = cometProbability;
        this.pelletProbability = pelletProbability;
        invincible = false;
        invincibleCounter = 0;
        scoreLabel.setBackground(Color.WHITE);
        scoreLabel.setForeground(Color.WHITE);
        scoreLabel.setBounds(50, 50, 100, 30);
    }

    @Override
    public void paintComponent(Graphics g) {
        //For some reason my timer doesnt work without something being constantly printed, so that's what this does
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, FRAME_WIDTH, FRAME_HEIGHT);
        Ship ship = new Ship(x, y, Color.WHITE);
        Random rand3 = new Random();
        int n3 = rand3.nextInt(5) + 0;

        if (getGameStatus() == false) {
            if (invincible) {
                ship.setColor(colors[n3]);
                if (invincibleCounter < 1500) {
                    invincibleCounter++;
                } else {
                    invincibleCounter = 0;
                    invincible = false;
                }
            }
            score++;
            Random rand = new Random();
            Random rand2 = new Random();
            int n = rand.nextInt(cometProbability) + 1;
            int n2 = rand2.nextInt(pelletProbability) + 1;
            if (n2 == 1) {
                generatePellet();
            }
            if (n == 1) {
                generateComet();
            }

            ship.paint(g);
            for (Comet c: comets) {
                if (ship.getX() == c.getX() && (ship.getY() >= c.getY() + 50 && ship.getY() <= c.getY() + 75) && invincible != true) {
                    endGame();
                }
                c.cometFall();
                c.paint(g);
            }
            for (Screen s: stars) {
                s.starFall();
                s.paint(g);
            }
            for (Bullet b: bullets) {
                b.bulletShoot();
                b.paint(g);
            }
            for (Pellet p: pellets) {
                if (ship.getX() == p.getX() && (ship.getY() >= p.getY() + 50 && ship.getY() <= p.getY() + 75)) {
                    invincible();
                }
                p.pelletFall();
                p.paint(g);
            }
            filter();
            Font font = g.getFont().deriveFont( 20.0f );
            g.setFont( font );
            g.setColor(Color.WHITE);
            g.drawString("Score: "+score, 50, 125);
        }
    }

    public void getInfoFromServer(){
        try{
            response=fromServer.readInt();
            if(response==2){
                gameQuit();
            }
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    /**
     * Filters out bullets and comets from their respective arraylist when they hit one another
     */
    public void filter() {
        Iterator <Bullet> Biterator = bullets.iterator();
        while (Biterator.hasNext()) {
            Bullet theBullet = Biterator.next();
            Iterator <Comet> Citerator = comets.iterator();
            if (theBullet.getY() < 20) {
                Biterator.remove();
            }
            while (Citerator.hasNext()) {
                Comet theComet = Citerator.next();
                if (theBullet.getX() == theComet.getX() && (theBullet.getY() >= theComet.getY() + 25 && theBullet.getY() <= theComet.getY() + 50)) {
                    score = score + 100;
                    Citerator.remove();
                    Biterator.remove();
                }
            }
        }
    }
    /**
     * sets the difficulty of the game
     */
    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }
    /**
     * Move Ship left if ship is not on the left most plan already
     */
    public void moveLeft() {
        if (x == plane1) {
            x = plane1;
        } else if (x == plane2) {
            x = plane1;
        } else {
            x = plane2;
        }
    }
    /**
     * Move Ship right if ship is not on the right most plan already
     */
    public void moveRight() {
        if (x == plane3) {
            x = plane3;
        } else if (x == plane2) {
            x = plane3;
        } else {
            x = plane2;
        }
    }
    /**
     * Creates a new comet object and adds it to the comets list
     */
    public Comet generateComet() {
        Random rand2 = new Random();
        int n2 = rand2.nextInt(3) + 0;
        Comet cometReturn = new Comet(planes[n2], -50 + cometDY);
        comets.add(cometReturn);
        return cometReturn;
    }
    /**
     * Creates a new pellet object and adds it to the pellets list
     */
    public Pellet generatePellet() {
        Random rand2 = new Random();
        int n = rand2.nextInt(5) + 0;
        int n2 = rand2.nextInt(3) + 0;
        Pellet pelletReturn = new Pellet(planes[n2], -50 + pelletDY, colors[n]);
        pellets.add(pelletReturn);
        return pelletReturn;
    }
    /**
     * returns the x location of the ship object
     */
    public int getShipX() {
        return x;
    }
    /**
     * returns the y location of the ship object
     */
    public int getShipY() {
        return y;
    }
    /**
     * Creates a screen object which holds the start ellipse in the background and animates them randomly
     */
    public void spaceAnimate() {
        Random rand = new Random();
        Random rand2 = new Random();
        int n = rand.nextInt(10) + 1;
        int n2 = rand2.nextInt(FRAME_WIDTH - 100) + 1;
        if (n == 5) {
            Screen screen = new Screen(n2, 0);
            stars.add(screen);
        }
        repaint();
    }
    /**
     * Called from the BulletKeyListener class and fires a bullets from the ship location
     */
    public void shoot() {
        Bullet bullet = new Bullet(x, y - 50);
        bullets.add(bullet);
    }
    /**
     * Called when the user is hit by an asteroid and gives the option to restart the game
     */
    public void endGame() {
        try{
            toServer.writeInt(STOP);
            toServer.writeInt(score);
            toServer.flush();
            pauseTimer();
        }catch(IOException io){
            io.printStackTrace();
        }
        gameOver = true;
    }
    /**
     * retrieves game status of the client, which determines if the game is running or not
     */
    public boolean getGameStatus() {
        return gameOver;
    }
    /**
     * Sets the probability in which the comets will spawn
     * @param cometProbability the int which is determined by the difficulty and determines the amount of comets the player gets
     */
    public void setProbability(int cometProbability) {
        this.cometProbability = cometProbability;
    }
    /**
     * Sets the probability in which the comets will spawn
     * @param pelletProbability the int which is determined by the difficulty and determines the amount of pellets the player gets
     */
    public void setPelletProbability(int pelletProbability) {
        this.pelletProbability = pelletProbability;
    }
    /**
     * returns the difficulty type of the clients game
     * @return difficulty
     */
    public String getDifficulty() {
        return difficulty;
    }
    /**
     * gets the amount of bullets on screen
     * @return int amount of bullets
     */
    public int getBulletsSize() {
        return bullets.size();
    }
    /**
     * sets the info for the server based on the arguments given on load from the FrameViewer class
     * @param fromServer the datastream from server
     * @param toServer the datastream to server
     */
    public void setServerInfo(DataInputStream fromServer, DataOutputStream toServer){
        this.fromServer=fromServer;
        this.toServer=toServer;
    }
    /**
     * Called when the user presses 'NO' on whether or not they want to play again or if the user presses 'q'
     */
    public void gameQuit() {
        try{
            toServer.writeInt(2);
            toServer.flush();
        }catch(IOException e){

        }
    }
    /**
     * System exits the window
     */
    public void closeClient(){
        System.exit(0);
    }
    /**
     * Creates a new frame to tell the client which user won with the highest score once both of them have lost
     * then asks the user if they would like to play again, if no, the game closes and if yes the timer starts back up again
     * @param winScore the score of the winning player
     * @param winner the name of the winning player
     */
    public void announceWinner(int winScore, String winner){
        JFrame f= new JFrame("Label Example");  
        JLabel l1;
        JLabel l2;
        l1=new JLabel(winner+" wins with a score of "+winScore);
        l2=new JLabel("Play Again?");
        JButton b1=new JButton("Yes"); 
        JButton b2=new JButton("No");
        b1.setBounds(50,250,95,30);  
        b2.setBounds(150,250,95,30);  
        l1.setBounds(50,50, 500,30);
        l2.setBounds(50,150, 500,30);
        f.add(l1);  
        f.add(l2);
        f.add(b1);
        f.add(b2);
        f.setSize(500,500);
        f.setLayout(null);  
        f.setVisible(true);
        b1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                gameOver=false;
                score=0;
                f.setFocusable(false);
                startTimer();
                
            }
        });
        b2.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                gameQuit();
            }
        });
    }
    /**
     * Called when a pellet touches the ship and lets the ship become invulnerable for a short period of time
     * @return boolean value of whether the player is invincible or not
     */
    public boolean invincible() {
        invincible = true;
        score += 50;
        return invincible;
    }
}