import javax.swing.*;
import java.awt.*;

public class UI extends JPanel {

    GameManager gm;
    public int tileSize = 10;
    public int maxTileSize = 60;
    public int minTileSize = 3;
    public int offsetX = 0;
    public int offsetY = 0;
    public int sectorWidth = 0;
    public boolean menu;
    public boolean onMenu;
    public String[] kategorie = {"Natural","Food","Even more","Miscellaneous","Exit"};

    public UI(GameManager gm){
        this.gm = gm;
        gm.loadWorld();
    }
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, getWidth(), getHeight());
        switch (gm.currentState){
            case GameManager.MENUSTATE -> drawMenuState(g);
            case GameManager.GAMESTATE -> drawGameState(g);
        }
        if(gm.debug){
            drawDebug(g);
        }
    }
    private void drawMenuState(Graphics g){
        g.setColor(Color.WHITE);
        g.drawString("Menu", getWidth()/2, getHeight()/2);
    }
    private void drawGameState(Graphics g){
        drawMap(g);
        if(!onMenu){
            drawPlacementShadow(g);
        }
        drawBuildingUI(g);
    }
    private void drawPlacementShadow(Graphics g){
        if(gm.currentBlock != null){
            g.setColor(Color.GREEN);
            Point point = gm.ml.screenCoordinatesToWorldCoordinates(gm.mml.currentMousePoint);
            g.drawRect(point.x * tileSize + offsetX ,point.y * tileSize + offsetY, tileSize * gm.currentBlock.width, tileSize * gm.currentBlock.height);
            Color transparentGreen = new Color(0,255,0,100);
            g.setColor(transparentGreen);
            g.fillRect(point.x * tileSize + offsetX + 1,point.y * tileSize + offsetY + 1, tileSize * gm.currentBlock.width - 1, tileSize * gm.currentBlock.height - 1);
        }
    }
    private void drawDebug(Graphics g){
        g.setColor(Color.RED);
        g.drawString(Integer.toString(gm.currentFPS),20,20);
    }
    private void drawMap(Graphics g){
        for(int k = 0; k <= gm.currentDepth; k++){
            for(int i = 0; i < gm.worldWidth; i++){
                for(int j = 0; j < gm.worldHeight; j++){
                    if(gm.blocks[k][i][j] != null) {
                        g.drawImage(gm.blocks[k][i][j].image, i * tileSize + offsetX + 1, j * tileSize + offsetY + 1, tileSize, tileSize, null);
                    }
                }
            }
        }
    }
    private void drawDepthLayer(Graphics g, int height){
        for(int i = 0; i < gm.worldDepth; i++){
            g.setColor(Color.WHITE);
            if(checkHitBoxOfRectangle(getWidth() - (10 + 25),height + 50 + (15 * i),10,5,gm.mml.currentMousePoint)){
                onMenu = true;
                if (gm.ml.leftMousePressed) {
                    gm.currentDepth = Math.abs(i - (gm.worldDepth - 1));
                }
            }
            if(gm.currentDepth == Math.abs(i - (gm.worldDepth - 1))){
                g.setColor(Color.RED);
                g.drawRect(getWidth() - (10 + 25) + 1,height + 50 + (15 * i) + 1,10 - 1,5 - 1);
            }
            g.drawRect(getWidth() - (10 + 25),height + 50 + (15 * i),10,5);
        }
    }
    private void drawBuildingUI(Graphics g){
        drawCircleFilledWithHitBox(g,25, getHeight() - 75, 50,gm.mml.currentMousePoint);
        onMenu = false;
        if(menu){
            int maxWidth = 300;
            int maxHeight = 600;
            int width = (getWidth() / 100) * 40;
            int height = (getHeight() / 100) * 60;
            if(width > maxWidth){
                width = maxWidth;
            }
            if(height > maxHeight){
                height = maxHeight;
            }
            drawSectionSelection(g);
            drawSelect(g,width,height);
            drawInfo(g,width,height);
            drawDepthLayer(g, height);
            g.setColor(Color.WHITE);
        }
    }
    private void drawSelect(Graphics g, int width, int height){
        if(checkHitBoxOfRectangle(25,25,width, height,gm.mml.currentMousePoint)){
            if((gm.mml.currentMousePoint.y - 58) / 25 < gm.section.get(kategorie[gm.currentSection]).length && (gm.mml.currentMousePoint.y - 58) / 25 >= 0){
                gm.currentDescriptionBlock = gm.section.get(kategorie[gm.currentSection])[(gm.mml.currentMousePoint.y - 58) / 25];
                if(gm.ml.leftMousePressed){
                    gm.currentBlock = gm.section.get(kategorie[gm.currentSection])[(gm.mml.currentMousePoint.y - 58) / 25];
                }
            }
        }
        drawRectangleFilledWithHitBox(g,25,25,width, height,gm.mml.currentMousePoint);
        g.setColor(Color.WHITE);
        g.drawString(kategorie[gm.currentSection],50 , 40);
        g.drawLine(25,58,width,58);
        if(gm.section.get(kategorie[gm.currentSection]) != null){
            for(int i = 0; i < gm.section.get(kategorie[gm.currentSection]).length; i++){
                g.drawString(gm.section.get(kategorie[gm.currentSection])[i].name,50,50 + (25 * (i + 1)));
                g.drawLine(25,58 + (25 * (i + 1)),width,58 + (25 * (i + 1)));
            }
        }
    }
    private void drawInfo(Graphics g, int width, int height){
        drawRectangleFilledWithHitBox(g,getWidth() - (width + 25),25,width,height,gm.mml.currentMousePoint);
        g.setColor(Color.WHITE);
        if(gm.currentDescriptionBlock != null){
            g.drawString(gm.currentDescriptionBlock.description,getWidth() - (width + 25) + 25,50);
        }
    }
    private void drawSectionSelection(Graphics g){
        if(checkHitBoxOfRectangle(100, getHeight() - 75, getWidth() - 125, 50,gm.mml.currentMousePoint) && gm.ml.leftMousePressed){
            gm.currentSection = Math.min((gm.mml.currentMousePoint.x - 100) / (sectorWidth), kategorie.length - 1);
        }
        drawRectangleFilledWithHitBox(g,100, getHeight() - 75, getWidth() - 125, 50,gm.mml.currentMousePoint);
        g.setColor(Color.WHITE);
        for(int i = 0; i < kategorie.length; i++){
            sectorWidth = ((getWidth() - 125) / kategorie.length);
            if(sectorWidth < 120){
                sectorWidth = 120;
            }
            if (i != 0) {
                g.drawLine(100 + sectorWidth * i,getHeight() - 75,100 + sectorWidth * i,getHeight() - 25);
            }
            g.drawString(kategorie[i],110 + sectorWidth * i,getHeight() - 50);
        }

    }
    private void drawRectangleFilledWithHitBox(Graphics g,int x,int y,int width,int height, Point cursorPoint){
        g.setColor(Color.WHITE);
        if(checkHitBoxOfRectangle(x,y,width,height,cursorPoint)){
            onMenu = true;
            g.setColor(Color.RED);
        }
        g.drawRect(x,y,width, height);
        g.setColor(Color.BLACK);
        g.fillRect(x + 1,y + 1,width - 1, height - 1);
    }
    private void drawCircleFilledWithHitBox(Graphics g,int x,int y,int diameter, Point cursorPoint){
        g.setColor(Color.WHITE);
        gm.mml.hoverUIStart = false;
        if(Math.sqrt(Math.pow(Math.abs(cursorPoint.x - (x + 25)),2) + Math.pow(Math.abs(cursorPoint.y - (y + 25)),2)) < (double) diameter /2){
            onMenu = true;
            g.setColor(Color.RED);
            gm.mml.hoverUIStart = true;
        }
        g.drawOval(x, y, diameter, diameter);
        g.setColor(Color.BLACK);
        g.fillOval(x + 1, y + 1, diameter - 1, diameter - 1);
    }
    private boolean checkHitBoxOfRectangle(int x,int y,int width,int height, Point cursorPoint){
        return cursorPoint.x >= x && cursorPoint.x <= x + width && cursorPoint.y >= y && cursorPoint.y <= y + height;
    }
    public int getMiddleXForString(String string,int containerWidth){
        return 50;
    }
}
