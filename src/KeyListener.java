import java.awt.*;
import java.awt.event.KeyEvent;

public class KeyListener implements java.awt.event.KeyListener {
    GameManager gm;
    public KeyListener(GameManager gm) {
        this.gm = gm;
    }
    @Override
    public void keyTyped(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_1) {
            System.out.println("typed 1");
            gm.ml.currentColor = Color.GREEN;
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_1) {
            System.out.println("typed 1");
            gm.ml.currentColor = Color.GREEN;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}
