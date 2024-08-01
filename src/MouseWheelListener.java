import java.awt.*;
import java.awt.event.MouseWheelEvent;

public class MouseWheelListener implements java.awt.event.MouseWheelListener {
    GameManager gm;
    public MouseWheelListener(GameManager gm) {
        this.gm = gm;
    }
    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        if(gm.ui.tileSize - e.getWheelRotation() > gm.ui.minTileSize && gm.ui.tileSize - e.getWheelRotation() < gm.ui.maxTileSize && !gm.ui.onMenu){
            Point Before = screenCoordinatesToWorldCoordinates(e.getPoint());
            gm.ui.tileSize -= e.getWheelRotation();
            Point After = screenCoordinatesToWorldCoordinates(e.getPoint());
            gm.ui.offsetX += (After.x - Before.x) * gm.ui.tileSize;
            gm.ui.offsetY += (After.y - Before.y) * gm.ui.tileSize;
        }
    }
    public Point screenCoordinatesToWorldCoordinates(Point point) {
        return new Point((point.x - gm.ui.offsetX) / gm.ui.tileSize, (point.y - gm.ui.offsetY) / gm.ui.tileSize);
    }
}
