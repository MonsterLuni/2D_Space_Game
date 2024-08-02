import java.awt.*;
import java.awt.event.MouseEvent;

public class MouseListener implements java.awt.event.MouseListener {
    boolean middleMousePressed;
    boolean leftMousePressed;
    Point pressPoint;
    Point offsetPoint;
    GameManager gm;
    Color currentColor = Color.RED;
    public MouseListener(GameManager gm) {
        this.gm = gm;
    }
    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON2 && !gm.ui.onMenu) {
            middleMousePressed = true;
            pressPoint = e.getPoint();
            offsetPoint = new Point(gm.ui.offsetX,gm.ui.offsetY);
        }
        else if (e.getButton() == MouseEvent.BUTTON1  ) {
            leftMousePressed = true;
            if(gm.mml.hoverUIStart){
                gm.ui.menu = !gm.ui.menu;
            }
            if(!gm.ui.onMenu){
                Point point = screenCoordinatesToWorldCoordinates(e.getPoint());
                if(point.x >= 0 && point.y >= 0 && point.x < gm.worldWidth && point.y < gm.worldHeight){
                    gm.blocks[gm.currentDepth][point.x][point.y] = gm.currentBlock;
                }
            }

        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON2) {
            middleMousePressed = false;
        }
        else if (e.getButton() == MouseEvent.BUTTON1) {
            leftMousePressed = false;
        }
    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }
    public Point screenCoordinatesToWorldCoordinates(Point point) {
        return new Point((point.x - gm.ui.offsetX) / gm.ui.tileSize, (point.y - gm.ui.offsetY) / gm.ui.tileSize);
    }
}
