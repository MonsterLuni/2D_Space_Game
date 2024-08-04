import java.awt.*;
import java.awt.event.MouseEvent;

public class MouseMotionListener implements java.awt.event.MouseMotionListener {
    public boolean hoverUIStart;
    GameManager gm;
    Point currentMousePoint = new Point(-1,-1);
    public MouseMotionListener(GameManager gm) {
        this.gm = gm;
    }
    @Override
    public void mouseDragged(MouseEvent e) {
        currentMousePoint = e.getPoint();
        if (gm.ml.middleMousePressed  && !gm.ui.onMenu) {
            gm.ui.offsetX = (e.getPoint().x - gm.ml.pressPoint.x + gm.ml.offsetPoint.x);
            gm.ui.offsetY = (e.getPoint().y - gm.ml.pressPoint.y + gm.ml.offsetPoint.y);
        }
        else if (gm.ml.leftMousePressed  && !gm.ui.onMenu) {
                Point point = screenCoordinatesToWorldCoordinates(e.getPoint());
                placeBlock(point);
        }
    }
    @Override
    public void mouseMoved(MouseEvent e) {
        currentMousePoint = e.getPoint();
    }

    public Point screenCoordinatesToWorldCoordinates(Point point) {
        return new Point((point.x - gm.ui.offsetX) / gm.ui.tileSize, (point.y - gm.ui.offsetY) / gm.ui.tileSize);
    }
    public void placeBlock(Point point){
        if(gm.currentBlock != null && point.x >= 0 && point.y >= 0 && point.x < gm.worldWidth && point.y < gm.worldHeight && gm.placable(point)){
            gm.antimatterPlacer(point);
            gm.blocks[gm.currentDepth][point.x][point.y] = gm.currentBlock;
        }
    }
}
