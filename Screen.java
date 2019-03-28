import javax.swing.JComponent;
import javax.swing.JOptionPane;
import java.lang.Object;
import java.text.AttributedCharacterIterator;
import java.util.Random;
import javax.swing.JComponent;
import java.awt.geom.*;
import java.awt.image.ImageObserver;
import java.awt.*;

public class Screen{
    int x;
    int y;
    Color c;
    public Screen(int x, int y){
        this.x=x;
        this.y=y;
        c=Color.WHITE;
    }

    public void paint(Graphics g){
        Graphics2D g2 = (Graphics2D) g;
        g2.setPaint(c);
        g2.fill(new Ellipse2D.Double(x, y, 2, 2));
    }
    /**
     * this gives the effect that the stars are falling
     */
    public void starFall(){
        y++;
    }
    
}