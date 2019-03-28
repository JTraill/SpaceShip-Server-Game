/**
 * Custom class to move the ship left and right and also listens to see if the user wants to quit the game
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

public class CustomKeyListener implements KeyListener {
    private ShipComponent sc;
    public CustomKeyListener(ShipComponent sc) {
        this.sc = sc;
    }
    public void keyTyped(KeyEvent e) {}
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            sc.moveRight();
        } else if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            sc.moveLeft();
        } else if (e.getKeyCode() == KeyEvent.VK_Q) {
            sc.gameQuit();
        }
    }
    public void keyReleased(KeyEvent e) {}
}