import javax.swing.JComponent;
import javax.swing.JOptionPane;
import java.lang.Object;
import java.text.AttributedCharacterIterator;
import java.util.Random;
import javax.swing.JComponent;
import java.awt.geom.*;
import java.awt.image.ImageObserver;
import java.awt.*;

public class Bullet{
    int x;
    int y;
    Color c;
    public Bullet(int x, int y){
        this.x=x;
        this.y=y;
    }

    public void paint(Graphics g){
        Graphics2D g2 = (Graphics2D) g;
        g2.setPaint(c.WHITE);
        g2.fill(new Rectangle2D.Double(x, y, 5, 20));
    }

    public void bulletShoot(){
        y--;
    }

    public int getX(){
        return x;
    }
    public int getY(){
        return y;
    }
}