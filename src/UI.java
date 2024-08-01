import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;

public class UI extends JPanel {
    public UI(GameManager gm){
        this.gm = gm;
        loadHashMap();
        loadWorld();
    }
    GameManager gm;
    public int tileSize = 10;
    public int maxTileSize = 60;
    public int minTileSize = 3;
    public int worldHeight = 100;
    public int worldWidth = 100;
    public int worldDepth = 5;
    public boolean menu;
    public Block[][][] blocks = new Block[worldDepth][worldWidth][worldHeight];
    public String[] kategorie = {"Natural","Food","Even more","Miscellaneous","Exit"};
    public HashMap<String,Block[]> section = new HashMap<>();
    public int offsetX = 0;
    public int offsetY = 0;
    public int sectorWidth = 0;
    public boolean onMenu;
    public void loadHashMap(){
        try{
            section.put("Natural", new Block[]{
                    new Block(ImageIO.read(new FileInputStream("src/assets/natural/dirt.png")),"Dirt","Dirty")
            });
            section.put("Food",new Block[]{
                    new Block(ImageIO.read(new FileInputStream("src/assets/food/furnace.png")),"Furnace","This Furnace can smelt ores"),
                    new Block(ImageIO.read(new FileInputStream("src/assets/food/refrigerator.png")),"Refrigerator","Indeed very cold"),
            });
        }catch (IOException e){
            System.out.println("File not found");
        }
    }
    public void drawDepthLayer(Graphics g, int height){
        for(int i = 0; i < worldDepth; i++){
            if(gm.ml.leftMousePressed && checkHitBoxOfRectangle(getWidth() - (10 + 25),height + 50 + (15 * i),10,5,gm.mml.currentMousePoint)){
                gm.currentDepth = Math.abs(i - worldDepth);
            }
            System.out.println(gm.currentDepth);
            g.drawRect(getWidth() - (10 + 25),height + 50 + (15 * i),10,5);
        }
    }
    public void loadWorld(){
        for(int k = 0; k < worldDepth; k++) {
            for (int i = 0; i < worldWidth; i++) {
                for (int j = 0; j < worldHeight; j++) {
                    blocks[k][i][j] = section.get("Natural")[0];
                }
            }
        }
    }
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, getWidth(), getHeight());
        g.setColor(Color.RED);
        g.drawString(Integer.toString(gm.currentFPS),20,20);
        for(int k = 0; k < worldDepth; k++){
            for(int i = 0; i < worldWidth; i++){
                for(int j = 0; j < worldHeight; j++){
                    if(k != 4){
                        if(blocks[k + 1][i][j] != null){
                            continue;
                        }
                    }
                    if(blocks[k][i][j] != null) {
                        g.drawImage(blocks[k][i][j].image, i * tileSize + offsetX + 1, j * tileSize + offsetY + 1, tileSize, tileSize, null);
                    }
                }
            }
        }
        Items(g);
    }
    private void Items(Graphics g){
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
            sectionSelector(g);
            select(g,width,height);
            info(g,width,height);
            drawDepthLayer(g, height);
            g.setColor(Color.WHITE);
        }
    }
    private void select(Graphics g,int width, int height){
        if(checkHitBoxOfRectangle(25,25,width, height,gm.mml.currentMousePoint)){
            if((gm.mml.currentMousePoint.y - 58) / 25 < section.get(kategorie[gm.currentSection]).length && (gm.mml.currentMousePoint.y - 58) / 25 >= 0){
                gm.currentDescriptionBlock = section.get(kategorie[gm.currentSection])[(gm.mml.currentMousePoint.y - 58) / 25];
                if(gm.ml.leftMousePressed){
                    gm.currentBlock = section.get(kategorie[gm.currentSection])[(gm.mml.currentMousePoint.y - 58) / 25];
                }
            }
        }
        drawRectangleFilledWithHitBox(g,25,25,width, height,gm.mml.currentMousePoint);
        g.setColor(Color.WHITE);
        g.drawString(kategorie[gm.currentSection],50 , 40);
        g.drawLine(25,58,width,58);
        if(section.get(kategorie[gm.currentSection]) != null){
            for(int i = 0; i < section.get(kategorie[gm.currentSection]).length; i++){
                g.drawString(section.get(kategorie[gm.currentSection])[i].name,50,50 + (25 * (i + 1)));
                g.drawLine(25,58 + (25 * (i + 1)),width,58 + (25 * (i + 1)));
            }
        }
    }
    private void info(Graphics g, int width, int height){
        drawRectangleFilledWithHitBox(g,getWidth() - (width + 25),25,width,height,gm.mml.currentMousePoint);
        g.setColor(Color.WHITE);
        if(gm.currentDescriptionBlock != null){
            g.drawString(gm.currentDescriptionBlock.description,getWidth() - (width + 25) + 25,50);
        }
    }
    private void sectionSelector(Graphics g){
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
    public int getMiddleXForString(String string,int containerWidth){
        return 50;
    }
    public boolean checkHitBoxOfRectangle(int x,int y,int width,int height, Point cursorPoint){
        return cursorPoint.x >= x && cursorPoint.x <= x + width && cursorPoint.y >= y && cursorPoint.y <= y + height;
    }
    public void drawRectangleFilledWithHitBox(Graphics g,int x,int y,int width,int height, Point cursorPoint){
        g.setColor(Color.WHITE);
        if(checkHitBoxOfRectangle(x,y,width,height,cursorPoint)){
            onMenu = true;
            g.setColor(Color.RED);
        }
        g.drawRect(x,y,width, height);
        g.setColor(Color.BLACK);
        g.fillRect(x + 1,y + 1,width - 1, height - 1);
    }
    public void drawCircleFilledWithHitBox(Graphics g,int x,int y,int diameter, Point cursorPoint){
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
}
