import java.awt.*;

public class Block {
    public Image image;
    public String name;
    public String description;
    public int id;
    public String sector;
    public int width = 1;
    public int height = 1;
    public int[] placableDepth = new int[]{1};
    public Block(Image image, String name, String description,String sector, int id){
        this.image = image;
        this.name = name;
        this.description = description;
        this.id = id;
        this.sector = sector;
    }
    public Block(Image image, String name, String description,String sector, int id, int[] placableDepth){
        this.image = image;
        this.name = name;
        this.description = description;
        this.id = id;
        this.sector = sector;
        this.placableDepth = placableDepth;
    }
    public Block(Image image, String name, String description,String sector, int id, int width, int height){
        this.image = image;
        this.name = name;
        this.description = description;
        this.id = id;
        this.sector = sector;
        this.width = width;
        this.height = height;
    }
    public Block(Image image, String name, String description,String sector, int id, int width, int height, int[] placableDepth){
        this.image = image;
        this.name = name;
        this.description = description;
        this.id = id;
        this.sector = sector;
        this.width = width;
        this.height = height;
        this.placableDepth = placableDepth;
    }
}
