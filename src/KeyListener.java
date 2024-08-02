import java.awt.event.KeyEvent;

public class KeyListener implements java.awt.event.KeyListener {
    GameManager gm;
    public KeyListener(GameManager gm) {
        this.gm = gm;
    }
    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if(e.getKeyCode() == KeyEvent.VK_F9) {
            gm.saveWorld();
        }
    }
    @Override
    public void keyReleased(KeyEvent e) {

    }
}
