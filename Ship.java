import javax.swing.JComponent;
import javax.swing.JOptionPane;
import java.lang.Object;
import java.text.AttributedCharacterIterator;
import java.util.Random;
import javax.swing.JComponent;
import java.awt.geom.*;
import java.awt.image.ImageObserver;
import java.awt.*;

public class Ship{
    int x;
    int y;
    Color c;
    public Ship(int x, int y, Color c){
        this.x=x;
        this.y=y;
        this.c=c;
    }
    public void paint(Graphics g){
        Graphics2D g2 = (Graphics2D) g;
        g2.setColor(c);
        int x2[]={x-20,x,x+20};
        int y2[]={y,y-50,y};
        g2.drawPolygon(x2,y2,3);
        g2.fillPolygon(x2,y2,3);
    }
    /**
     * returns x location of ship
     * @return x location
     */
    public int getX(){
        return x;
    }
    /**
     * returns y location of ship
     * @return y location
     */
    public int getY(){
        return y;
    }
    /**
     * sets color of the ship
     * @param c color of the ship object
     */
    public void setColor(Color c){
        this.c=c;
    }
    
}