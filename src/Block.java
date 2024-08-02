import java.awt.*;

public class Block {
    public Image image;
    public String name;
    public String description;
    public int id;
    public String sector;
    public Block(Image image, String name, String description,String sector, int id){
        this.image = image;
        this.name = name;
        this.description = description;
        this.id = id;
        this.sector = sector;
    }

}
