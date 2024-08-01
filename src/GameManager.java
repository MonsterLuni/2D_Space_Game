import javax.swing.*;
import java.awt.*;

public class GameManager {
    MouseListener ml;
    MouseMotionListener mml;
    MouseWheelListener mwl;
    KeyListener kl;
    public UI ui;
    public int screenHeight = 600;
    public int screenWidth = 600;
    private final int FPS = 60;
    public int currentFPS;
    public Block currentBlock;
    public Block currentDescriptionBlock;
    public int currentSection = 0;
    public int currentDepth = 4;

    public void start(){
        ui = new UI(this);
        ml = new MouseListener(this);
        mml = new MouseMotionListener(this);
        mwl = new MouseWheelListener(this);
        kl = new KeyListener(this);
        JFrame frame = new JFrame("Simulation");
        ui.paintComponents(ui.getGraphics());
        ui.setBackground(Color.red);
        ui.addMouseListener(ml);
        ui.addMouseMotionListener(mml);
        ui.addMouseWheelListener(mwl);
        ui.addKeyListener(kl);
        ui.setFocusable(true);
        frame.add(ui);
        frame.setSize(screenWidth, screenHeight);
        frame.setVisible(true);
        framelimiter();
    }

    public void framelimiter() {
        long lastTime = System.currentTimeMillis();
        while (true) {
            long currentTime = System.currentTimeMillis();
            long elapsedTime = currentTime - lastTime;

            if (elapsedTime >= 1000 / FPS) {
                ui.repaint();
                currentFPS = (int) (1000 / elapsedTime);
                lastTime = currentTime;
            }

            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

}
