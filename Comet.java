import javax.swing.JComponent;
import javax.swing.JOptionPane;
import java.lang.Object;
import java.text.AttributedCharacterIterator;
import java.util.Random;
import javax.swing.JComponent;
import java.awt.geom.*;
import java.awt.image.ImageObserver;
import java.awt.*;

public class Comet{
    int FRAME_WIDTH=1300;
    int FRAME_HEIGHT=800;
    int plane1 = FRAME_WIDTH/3;
    int plane2 = FRAME_WIDTH/2;
    int plane3 = FRAME_WIDTH-100;
    int x;
    int y;
    Color c;
    public Comet(int x, int y){
        this.x=x;
        this.y=y;
        c=Color.GRAY;
    }
    public void paint(Graphics g){
        Graphics2D g2 = (Graphics2D) g;
        Ellipse2D ellipse = new Ellipse2D.Double(x-20, y, 40, 40);
        g2.setColor(c);
        g2.fill(ellipse);
    }
    /**
     * propells the comet downward
     */
    public void cometFall(){
        y++;
    }
    /**
     * returns x location of comet
     * @return int
     */
    public int getX(){
        return x;
    }
    /**
     * returns y location of comet
     * @return int
     */
    public int getY(){
        return y;
    }
    /**
     * sets color of comet
     * @param c color
     */

    public void setColor(Color c){
        this.c=c;
    }
}