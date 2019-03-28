/**
 * Josh Traill, 100287142, March 29 2018
 * Ship game with a functional server. The game is a space game where the player plays as a ship
 * and can move left and right between 3 different x locations. Comets fly towards the ship and if it hits the ship the game ends and the user
 * has the option to either quit or restart the game.
 * The game has 3 different difficulties and these affect the speed of the comets and the probability that they will spawn. It also
 * affects the score and the probability that the invincibility pellets will spawn as well.
 * There is only one bullet allowed on the screen at any given time to make the game harder and not allow the user to spam bullets to win.
 * At any given point of the game the user can press 'q' to quit the game
 * If no difficulty is chosen the game automatically assigns the difficulty to 'medium'
 * 
 */
import javax.swing.JOptionPane;
import java.lang.Object;
import javax.swing.*;
import java.awt.event.*;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.Color;
import java.awt.*;
import java.util.ArrayList;
import java.util.Scanner;
import java.io.*;
import java.net.*;
public class FrameViewer extends JComponent implements Shippable{
    private static Socket connection;
    private static DataInputStream fromServer;
    private static DataOutputStream toServer;
    static ArrayList < Integer > scores = new ArrayList < Integer > ();
    private static final int FRAME_WIDTH = 1300;
    private static final int FRAME_HEIGHT = 800;
    private static JFrame frame = new JFrame("Comets");
    private static JPanel panel = new JPanel();
    private static JLabel label = new JLabel("Select Difficulty: ");
    private static JButton easy = new JButton("Easy");
    private static JButton medium = new JButton("Medium");
    private static JButton hard = new JButton("Hard");
    private static JButton start = new JButton("Start Game");
    private static JButton highScores = new JButton("Get Highscores");
    private static String difficulty = "medium";
    private static final int shipY = 700;
    private static String ip;
    private static String portStr;
    private static int port;
    private static ShipComponent sc = new ShipComponent(difficulty, FRAME_WIDTH / 2, shipY, Color.BLACK, false, 25, 2000);
    private static final CustomKeyListener keyListener = new CustomKeyListener(sc);
    private static final BulletKeyListener bulletKeyListener = new BulletKeyListener(sc);
    private static ActionListener listener;
    private static Timer t;

    public static void main(String[] args) {
        init(args);
    }
    //Initializes all the variables required for the game to run
    public static void init(String[] args) {
        if(args.length==2){
            try{
                ip=args[0];
                portStr=args[1];
            }catch(IndexOutOfBoundsException ob){}
        }else if(args.length==1){
            ip=args[0];
            portStr="2018";
        }else{
            ip="localhost";
            portStr="2018";
        }
        port=Integer.parseInt(portStr);
        label.setBackground(Color.WHITE);
        label.setForeground(Color.WHITE);
        label.setBounds(50, 50, 100, 30);
        easy.setBounds(150, 100, 100, 30);
        medium.setBounds(250, 100, 100, 30);
        hard.setBounds(350, 100, 100, 30);
        start.setBounds(450, 100, 100, 30);
        highScores.setBounds(750, 100, 200, 30);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        panel.setLayout(new BorderLayout());
        panel.add(highScores);
        panel.add(easy);
        panel.add(medium);
        panel.add(hard);
        panel.add(start);
        panel.add(label);
        panel.add(sc);
        frame.add(panel);
        frame.setFocusable(true);
        frame.setSize(FRAME_WIDTH, FRAME_HEIGHT);
        frame.setResizable(false);
        frame.setVisible(true);
        sc.setProbability(125);
        try{
            connection = new Socket(InetAddress.getByName(ip), port);
            fromServer = new DataInputStream(connection.getInputStream());
            toServer = new DataOutputStream(connection.getOutputStream());
            sc.setServerInfo(fromServer, toServer);
            toServer.flush();
        }catch(Exception e){
            e.printStackTrace();
            System.out.println("ERROR");
        }
        
        easy.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                difficulty = "easy";
                sc.setDifficulty(difficulty);
                System.out.println(">>>DIFFICULTY: " + difficulty + " SELECTED<<<");
                sc.setProbability(200);
                sc.setPelletProbability(2000);
            }
        });
        medium.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                difficulty = "medium";
                sc.setDifficulty(difficulty);
                System.out.println(">>>DIFFICULTY: " + difficulty + " SELECTED<<<");
                sc.setProbability(125);
                sc.setPelletProbability(2500);
            }
        });
        hard.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                difficulty = "hard";
                sc.setDifficulty(difficulty);
                System.out.println(">>>DIFFICULTY: " + difficulty + " SELECTED<<<");
                sc.setProbability(90);
                sc.setPelletProbability(3000);
            }
        });
        start.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int response=0;
                try{
                    toServer.writeInt(0);
                    toServer.flush();
                    response = fromServer.readInt();
                    if(response==1){
                        gameStart();
                    }
                    System.out.println(">>>GAME START REQUESTED<<<");
                }catch(Exception exx){}
            }
        });
        highScores.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String output="";
                try{
                    toServer.writeInt(GETSCORES);
                    toServer.flush();
                    int amount = fromServer.readInt();
                    for(int i=0; i<amount; i++){
                        output+="<html>"+(i+1)+" PLACE WITH A SCORE OF: "+fromServer.readInt()+System.lineSeparator()+"<br/>";
                    }
                    output+="</html>";
                    
                    Frame f= new JFrame("Scores");  
                    JLabel l1;
                    l1=new JLabel(output);
                    l1.setBounds(0,50, 500,250);   
                    f.add(l1);  
                    f.setSize(750,750);
                    f.setLayout(null);  
                    f.setVisible(true); 
            }catch(Exception exx){}
            }
        });    
    }

    //Function called to start building the game friend and start the timers
    public static void gameStart() {
        //Start by removing the menu screen elements
        highScores.setFocusable(false);
        panel.remove(easy);
        panel.remove(medium);
        panel.remove(hard);
        panel.remove(start);
        panel.remove(label);
        panel.remove(highScores);
        frame.addKeyListener(keyListener);
        frame.addKeyListener(bulletKeyListener);
        Client c1 = new Client("Thread-1",fromServer, sc);
        c1.start();
        class ShipTimerListener implements ActionListener {
            @Override
            public void actionPerformed(ActionEvent event) {
                System.out.println("");//I need this in my code to help the animation run smoothly for some reason?
                sc.spaceAnimate();
            }
        }
        class BulletTimerListener implements ActionListener {
            @Override
            public void actionPerformed(ActionEvent event) {}
        }
        listener = new ShipTimerListener();
        if (sc.getDifficulty().equals("easy")) {
            t = new Timer(2, listener);
            t.start();
        } else if (sc.getDifficulty().equals("medium")) {
            t = new Timer(2, listener);
            t.start();
        } else {
            t = new Timer(1, listener);
            t.start();
        }
    } 
    //Starts the game timer
    public void startTimer(){
        try{
            t = new Timer(2, listener);
            t.start();
        }catch(Exception e){}
        
    }
    //Stops the game timer
    public void pauseTimer(){
        try{
            t.stop();
        }catch(Exception e){}
        
    }
    
}