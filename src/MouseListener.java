import java.awt.*;
import java.awt.event.MouseEvent;

public class MouseListener implements java.awt.event.MouseListener {
    boolean middleMousePressed;
    boolean leftMousePressed;
    Point pressPoint;
    Point offsetPoint;
    GameManager gm;
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
            if (!gm.ui.menu && gm.currentPerson != null){
                System.out.println("Sending Person Number " + gm.currentPerson + " to " + gm.screenCoordinatesToWorldCoordinates(gm.mml.currentMousePoint));
                gm.currentPerson.walkToPoint(gm.screenCoordinatesToWorldCoordinates(gm.mml.currentMousePoint));
            }
            else if(!gm.ui.onMenu){
                Point point = gm.screenCoordinatesToWorldCoordinates(e.getPoint());
                gm.mml.placeBlock(point);
            }
        } else if (e.getButton() == MouseEvent.BUTTON3 && gm.currentPerson != null) {
            gm.currentPerson = null;
        } else if (e.getButton() == MouseEvent.BUTTON3 && gm.currentBlock != null) {
            gm.currentBlock = null;
        } else if (e.getButton() == MouseEvent.BUTTON3 && gm.ui.menu) {
            gm.ui.menu = false;
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
}
