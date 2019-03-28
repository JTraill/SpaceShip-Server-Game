import java.io.*;
import java.net.*;
import java.time.LocalDateTime;
import java.time.format.*;
import java.awt.*;
import javax.swing.*;
import javax.swing.text.DefaultCaret;
import java.util.ArrayList;

/**
 * Server for the Encryption and Decryption of messages
 * inspired losely by programs in Y. D. Liang's book on Java and from a previous lab
 * http://www.cs.armstrong.edu/liang/intro9e/book/MultiThreadServer.java
 *
 * @author J. Traill
 * @version April 12, 2018
 */
public class GameServer extends JFrame implements Shippable{
   public static ShipComponent sc;
   public static final int FRAME_WIDTH = 1000;
   public static final int FRAME_HEIGHT = 450;
   public static final Font LOG_FONT = new Font("SansSerif", Font.BOLD, 32);
   private static DateTimeFormatter FORMATTER = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM, FormatStyle.SHORT);
   private JTextArea textAreaLog;
   
   /**
   * Starts the server which creates a log window, opens one socket per client
   * and starts an encryption / decryption (in its own thread). The Server
   * begins the converstation by giving a client a name (a client number).
   * @param args line arguments -- not used
   */
   public static void main(String[] args) {
      new GameServer(sc);
   }
   
   /**
   * Writes onto the textAreaLog a string and adds a carriage return.
   * Scroll down so that the bottom of the textArea is always shown,
   * @param msg the message to display.
   */
   private void report(String msg){
      textAreaLog.append(msg + '\n');
      textAreaLog.setCaretPosition(textAreaLog.getDocument().getLength());
   }

   /**
   * Builds an extended frame that has a text area to capture the messages 
   * e.g. the log of connections and when a game begins. 
   */
   public void buildLogFrame(){ 
      textAreaLog = new JTextArea();
      textAreaLog.setFont(LOG_FONT);
      DefaultCaret caret = (DefaultCaret)textAreaLog.getCaret();
      caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
      setSize(FRAME_WIDTH, FRAME_HEIGHT);
      setTitle("Log of Activities");
      add(new JScrollPane(textAreaLog), BorderLayout.CENTER);
      setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      setVisible(true);
   }

   /**
   * Reports the server's IP address.
   * Gives current time.
   * @return gives the date and time nicely formatter
   * @throws UnknownHostException due to not a proper server
   */
   private String reportStatsOnServer() throws UnknownHostException
   {
      report("This server's computer name is " + 
         InetAddress.getLocalHost().getHostName());
      report("This server's IP address is " + 
         InetAddress.getLocalHost().getHostAddress() + "\n");
      return LocalDateTime.now().format(FORMATTER);
   } // reportStatsOnServer

   /**
   * Reports the clients's domain name and IP address. <br /> 
   * @param socket an open socket
   * @param n the client's number (starting with 1)
   */
   private void reportStatsOnClient(Socket socket, int n){
      InetAddress  addr = socket.getInetAddress();
      report("client " + n + "'s host name is " + addr.getHostName());
      report("client " + n + "'s IP Address is " + addr.getHostAddress());
      report("starting thread for client " + n + " at " + 
         LocalDateTime.now().format(FORMATTER));
   }
   /**
   * Builds a reporting window.
   * It opens a server socket.
   * It connects through a socket to a client.
   * It provides encrypting and decrypting for that client (handles the client)
   */
   public GameServer(ShipComponent sc) 
   {
       this.sc=sc;
      buildLogFrame();
      
      try (ServerSocket serverSocket = new ServerSocket(PORT)){
         String nowStr = reportStatsOnServer();        
         report("The server, port " + serverSocket.getLocalPort() 
            + ", started on " + nowStr);    
         int clientNumber = 0;  
         while (true) {
            clientNumber++;
            System.out.println(">>SERVER WAITING<<");
            try{
                Socket socket = serverSocket.accept();
                reportStatsOnClient(socket, clientNumber);
                Runnable service = new HandleClient(socket, textAreaLog, clientNumber, sc);
                new Thread(service).start();
            }catch(Exception e){

            }
         }
      }catch(IOException e) {
         report("problems in server " + e.toString());
         e.printStackTrace(System.err);
      }
   }
}
