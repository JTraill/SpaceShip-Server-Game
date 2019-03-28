import java.io.*;
import java.net.*;
import java.awt.*;
import javax.swing.*;
import javax.swing.text.DefaultCaret;
import java.util.*;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentLinkedQueue;

/** 
 * Defines the thread class and handles input and command requests from the user
 * Starts by sending the name of the client to the server
 * @author Josh Traill, loosely based on the HandleClient class from a previous lab
 */

public class HandleClient extends FrameViewer implements Runnable, Shippable{
    private static Collection<Socket> activeSockets = new ConcurrentLinkedQueue<>(); //used to store open sockest across the server
    private static ArrayList<Integer> highScores=new ArrayList<Integer>(); //creating new generic arraylist
    private static ShipComponent sc;
    private static int playersPlaying;
    private Socket socket; 
    private JTextArea textAreaLog;
    private int clientNumber;
    private DataInputStream fromClient;
    private DataOutputStream toClient;
   
   /**
   * Receives the open socket so that input/output streams can be attached
   * and it receives the JTextArea where the messages will be logged
   * @param s a socket that is opened already
   * @param tA a JTextArea that receives messages
   * @param cN the client number (used for reporting purposes)
   * @param shipC the ship component
   */
   public HandleClient(Socket s, JTextArea tA, int cN, ShipComponent shipC){
      socket = s;
      textAreaLog = tA;
      clientNumber = cN;
      shipC = sc;
      playersPlaying = 0;
   }
   
   /**
   * Helper function to report messages to the textAreaLog
   * which is an instance variable. It keeps the last appended line
   * showing at the bottom.
   * @param direction defines to or from the server
   * @param msg the message to be delivered
   */
   private void report(String direction, String msg){
      textAreaLog.append(direction + " client " + clientNumber + ": ");
      textAreaLog.append(msg + '\n');
      textAreaLog.setCaretPosition(textAreaLog.getDocument().getLength());
   }
   
  /**
   * Runs a thread:
   *  - set up the DataInputStream and the DataOutputStream
   *  - call for the commands from the client to be executed
   *  - clean up
   */
   public void run(){
      try{
         try{
            fromClient = new DataInputStream(socket.getInputStream());
            toClient = new DataOutputStream(socket.getOutputStream());
            activeSockets.add(socket);
            executeCmds();
         }
         finally{
            activeSockets.remove(socket);
            socket.close();
         }
      }

      catch(Exception e){
         report("ERROR ", e.getMessage());
      }
   } // run
   
   /**
   * Execute all commands until the QUIT command is received from the client, 
   * i.e. continuously serve the client.
   * If there is an unknown command, then stop, do not continue.
   */
   private void executeCmds() throws IOException{
      // send the client its name (number)
      report("to ", cmdToString(NAME) + " " + clientNumber);
      // start listening to client's requests and respond to them  
      boolean done = false;
      while (!done){ 
        int cmd=fromClient.readInt();
         System.out.println("COMMAND: "+cmd);
         if(cmd==START){
             if(activeSockets.size()<2){
                 report("to", "WAITING FOR OPPONENT");
                 toClient.writeInt(0);
                 toClient.flush();
             }else{
                 playersPlaying+=1;
                toClient.writeInt(1);
                toClient.flush();
                report("to", "GAME START");
             }
         }else if(cmd==QUIT){
            playersPlaying-=1;
            done=true;
            toClient.writeInt(1);
            toClient.flush();
            doQuit();
         }else if(cmd==STOP){
            playersPlaying-=1;
            int score = fromClient.readInt();
            System.out.println(">>PLAYER STOP SUCCESSFULLY<<<");
            storeHighScores(score);
            int maxScore=getHighestScore();
            report("to", "GAME STOP");
            System.out.println(activeSockets.size());
            report("to", "SCORE: "+score);
            System.out.println("PLAYERS PLAYING: "+playersPlaying);
            if(playersPlaying==0){//used to make sure the last player is playing when the winner is displayed
                toClient.writeInt(GETSCORES);
                toClient.writeInt(maxScore);
                toClient.writeUTF("Client "+clientNumber);
                toClient.flush();
            }
         }else if(cmd==GETSCORES){
             toClient.writeInt(getScoreSize());
             toClient.flush();
             for(int i : highScores){
                 toClient.writeInt(i);
                 toClient.flush();
             }
         }
        }
    }
   /**
   * Processes a server quit by notifying the client with a DONE.
   */
   private void doQuit() throws IOException{
      report("from", cmdToString(QUIT) + "  received");
      toClient.writeInt(DONE);
      toClient.flush();
   }
   /**
    * Stores the last highscore from client for future reference
    @param newScore score to be stored into the servers data
    */
   private void storeHighScores(int newScore){
        if(highScores.size()>3){
            for(int i : highScores){
                if(newScore>i){
                    i=newScore;
                }
                System.out.println("SCORE: "+i);
            }
        }else{
            highScores.add(newScore);
        }
   }
   /**
    * @return the amount of scores stored
    */
   private int getScoreSize(){
        return highScores.size();
   }
   /**
    * @return max the top score in the data pool
    */
   private int getHighestScore(){
       int max=0;
       for(int i : highScores){
        if(i>max){
            max=i;
        }
       }
    return max;
   }
}