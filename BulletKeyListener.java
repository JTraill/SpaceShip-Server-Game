/**
 * Class used solely to fire a bullet from the ship
 */
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
import java.util.concurrent.TimeUnit;

public class BulletKeyListener implements KeyListener {
    private ShipComponent sc;

    public BulletKeyListener(ShipComponent sc) {
        this.sc = sc;
    }
    public void keyTyped(KeyEvent e) {}

    public void keyPressed(KeyEvent e) {

    }

    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_SPACE) {
            if (sc.getBulletsSize() < 1) {
                sc.shoot();
                System.out.println(">>>SHOOT<<<");
            }
        }

    }
}