import javax.swing.JComponent;
import javax.swing.JOptionPane;
import java.lang.Object;
import java.text.AttributedCharacterIterator;
import java.util.Random;
import javax.swing.JComponent;
import java.awt.geom.*;
import java.awt.image.ImageObserver;
import java.awt.*;

public class Pellet{
    int x;
    int y;
    Color c;
    public Pellet(int x, int y, Color c){
        this.x=x;
        this.y=y;
        this.c=c;
    }

    public void paint(Graphics g){
        Graphics2D g2 = (Graphics2D) g;
        g2.setPaint(c);
        Ellipse2D ellipse = new Ellipse2D.Double(x-20, y, 30, 30);
        g2.fill(ellipse);
    }
    /**
     * makes the pellet fall towards the player
     */
    public void pelletFall(){
        y++;
    }
    /**
     * returns x value of pellet
     */
    public int getX(){
        return x;
    }
    /**
     * returns y value of pellet
     */
    public int getY(){
        return y;
    }
}