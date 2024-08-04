import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;

public class GameManager {
    public MouseListener ml;
    public MouseMotionListener mml;
    public MouseWheelListener mwl;
    public KeyListener kl;
    public UI ui;
    public Block currentBlock;
    public Block currentDescriptionBlock;
    public int FPS = 60;
    public int currentFPS;
    public int currentSection = 0;
    public int currentDepth = 4;
    public Person currentPerson;
    public int screenHeight = 600;
    public int screenWidth = 600;
    public int worldHeight = 100;
    public int worldWidth = 100;
    public int worldDepth = 5;
    public static final int MENUSTATE = 0;
    public static final int GAMESTATE = 1;
    public int currentState = GAMESTATE;
    public boolean debug = true;
    public Block[][][] blocks = new Block[worldDepth][worldWidth][worldHeight];
    public HashMap<String,Block[]> section = new HashMap<>();
    public Person[] persons;

    public void start(){
        ui = new UI(this);
        loadPersons();
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
    public void generateWorld(){

    }
    public void loadHashMap(){
        try{
            String sector = "Natural";
            section.put("Natural", new Block[]{
                    new Block(ImageIO.read(new FileInputStream("src/assets/natural/dirt.png")),"Dirt","Literally very dirty",sector,0,new int[]{0,1}),
                    new Block(ImageIO.read(new FileInputStream("src/assets/natural/stone.png")),"Stone","Common in the Ground",sector,0,new int[]{0,1})
            });
            sector = "Food";
            section.put("Food",new Block[]{
                    new Block(ImageIO.read(new FileInputStream("src/assets/food/refrigerator.png")),"Refrigerator","Indeed very cold",sector,1,new int[]{3}),
            });
            sector = "Production";
            section.put("Production",new Block[]{
                    new Block(ImageIO.read(new FileInputStream("src/assets/food/furnace.png")),"Furnace","This Furnace can smelt ores",sector,0,2,2,new int[]{3}),
            });
            sector = "Power";
            section.put("Power",new Block[]{
                    new Block(ImageIO.read(new FileInputStream("src/assets/power/cable.png")),"Cable","Transportes Power",sector,0,new int[]{2}),
                    new Block(ImageIO.read(new FileInputStream("src/assets/power/reactor.png")),"Reactor","Produces Power",sector,0,5,4,new int[]{3}),
            });
            sector = "air";
            section.put("air",new Block[]{
                    new Block(null,"air","air",sector,0),
            });
        }catch (IOException e){
            System.out.println("File not found");
        }
    }
    public void loadPersons(){
        try {
            persons = new Person[2];
            persons[0] = new Person(this,ImageIO.read(new FileInputStream("src/assets/person/person.png")),new Point(0,0));
            persons[1] = new Person(this,ImageIO.read(new FileInputStream("src/assets/person/person.png")),new Point(9,9));
            currentPerson = persons[0];
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public void loadWorld(){
        loadHashMap();
        for(int k = 0; k < worldDepth; k++) {
            for (int i = 0; i < worldWidth; i++) {
                for (int j = 0; j < worldHeight; j++) {
                    if (k == 0) {
                        blocks[k][i][j] = section.get("Natural")[1];
                    }
                    else if (k == 1) {
                        blocks[k][i][j] = section.get("Natural")[0];
                    } else{
                        blocks[k][i][j] = section.get("air")[0];
                    }
                }
            }
        }
    }
    public void saveWorld(){
        StringBuilder world = new StringBuilder();
        for(int k = 0; k < worldDepth; k++) {
            for (int i = 0; i < worldWidth; i++) {
                for (int j = 0; j < worldHeight; j++) {
                    if(blocks[k][i][j] == null){
                        world.append(" ").append("x");
                    }
                    else{
                        world.append(" ").append(blocks[k][i][j].sector).append(blocks[k][i][j].id);
                    }

                }
            }
        }
        try{
            BufferedWriter writer = new BufferedWriter(new FileWriter("src/assets/world.txt"));
            writer.write(world.toString());
            writer.close();
            System.out.println("Saved world");
        } catch (IOException e){
            System.out.println("Couldn't save world");
        }
    }
    public Point screenCoordinatesToWorldCoordinates(Point point) {
        return new Point((point.x - ui.offsetX) / ui.tileSize, (point.y - ui.offsetY) / ui.tileSize);
    }
    public boolean placable(Point point){
        try{
            for(int i = 0; i < currentBlock.width; i++){
                for(int j = 0; j < currentBlock.height; j++){
                    if(blocks[currentDepth][point.x + i][point.y + j] != section.get("air")[0]){
                        return false;
                    }
                }
            }
            return true;
        }catch (IndexOutOfBoundsException e){
            return false;
        }
    }
    public void antimatterPlacer(Point point){
        for(int i = 0; i < currentBlock.width; i++){
            for(int j = 0; j < currentBlock.height; j++){
                blocks[currentDepth][point.x + i][point.y + j] = new Block(null,"NULL","NULL","NULL",0);
            }
        }
    }
}
