/**
 * Client thread that runs constantly in the background of program 
 * to analyze the response of the Server, and to move forwards with the appropriate response
 */

import java.lang.*;
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
public class Client implements Runnable, Shippable{
    private Thread t;
    private String threadName;
    private DataInputStream fromServer;
    private ShipComponent sc;
    public Client(String name, DataInputStream fromServer, ShipComponent sc) {
        threadName = name;
        this.fromServer=fromServer;
        this.sc=sc;
     }           
     public void run() {
         while(true){
            try{
                int response = fromServer.readInt();
                if(response==1){
                    sc.closeClient();
                }else if(response==GETSCORES){
                    int highScore=fromServer.readInt();
                    String winner=fromServer.readUTF();
                    sc.announceWinner(highScore, winner);
                    
                }
            }catch(Exception exx){}
         }
     }
     
     public void start () {
        System.out.println("Starting " +  threadName );
        if (t == null) {
           t = new Thread (this, threadName);
           t.start ();
        }
     }
}