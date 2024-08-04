import java.awt.*;
import java.util.Arrays;
import java.util.HashMap;

public class Person{
    GameManager gm;
    Image image;
    Point location;
    int height = 2;
    int width = 1;
    int step;
    Point currentGoal;
    int amountOfNodes;
    Node[] openListed;
    Node[] closedListed;

    public Person(GameManager gm, Image image, Point location) {
        this.gm = gm;
        this.image = image;
        this.location = location;
    }
    public void walkToPoint(Point goal){
        currentGoal = goal;
        CalculateWay thread = new CalculateWay();
        if(!thread.isAlive()){
            thread.start();
        }
    }
    public class CalculateWay extends Thread{
        @Override
        public void run() {
            openListed = new Node[(gm.worldWidth * gm.worldHeight * gm.ui.tileSize * 600)];
            closedListed = new Node[(gm.worldWidth * gm.worldHeight * gm.ui.tileSize * 600)];
            openListed[0] = new Node(location);
            System.out.println("trying...");
            boolean goalFound = false;
            step = 0;
            amountOfNodes = 1;
            int currentAmount;
            while(!goalFound){
                currentAmount = amountOfNodes;
                for(int i = 0; i < currentAmount; i++){
                    Node c = openListed[i];
                    if (c != null) {
                        int currentLocationX = Math.abs(c.location.x - currentGoal.x);
                        int currentLocationY = Math.abs(c.location.y - currentGoal.y);

                        int left = Math.abs(c.location.x - 1 - currentGoal.x) + currentLocationY;
                        int right = Math.abs(c.location.x + 1 - currentGoal.x) + currentLocationY;
                        int up = currentLocationX + Math.abs(c.location.y - 1 - currentGoal.y);
                        int down = currentLocationX + Math.abs(c.location.y + 1 - currentGoal.y);
                        int result = Math.min(Math.min(left, right), Math.min(up, down));

                        if (result == left) {
                            openListed[i + 1] = new Node(c, new Point(c.location.x - 1, c.location.y),amountOfNodes);
                            amountOfNodes++;
                        } else if (result == right) {
                            openListed[i + 1] = new Node(c, new Point(c.location.x + 1, c.location.y),amountOfNodes);
                            amountOfNodes++;
                        } else if (result == up) {
                            openListed[i + 1] = new Node(c, new Point(c.location.x, c.location.y - 1),amountOfNodes);
                            amountOfNodes++;
                        } else if (result == down) {
                            openListed[i + 1] = new Node(c, new Point(c.location.x, c.location.y + 1),amountOfNodes);
                            amountOfNodes++;
                        }
                        if (c.location.x == currentGoal.x && c.location.y == currentGoal.y) {
                            goalFound = true;
                            break;
                        }
                        closedListed[c.id] = c;
                        openListed[i] = null;
                    }
                }
            }
            System.out.println("Complete");
            walkWay(currentGoal);
        }
        public void walkWay(Point goal){
            int i = amountOfNodes - 2;
            Point[] way = new Point[amountOfNodes - 1];
            way[i] = openListed[amountOfNodes - 1].before.location;
            while(true){
                i--;
                way[i] = closedListed[i].before.location;
                if (way[i].x == location.x && way[i].y == location.y) {
                    System.out.println("Found point");
                    break;
                }
            }
            System.out.println("Route calculated");
            for(Point moveTo: way){
                if(moveTo != null){
                    location.x += (moveTo.x - location.x);
                    location.y += (moveTo.y - location.y);
                    try {
                        Thread.sleep(50);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
            location.x = goal.x;
            location.y = goal.y;
            this.interrupt();
        }
    }
    public static class Node{
        public Node(Node before, Point location, int id){
            this.location = location;
            this.before = before;
            this.id = id;
        }
        public Node(Point location){
            this.location = location;
        }
        Node before;
        Point location;
        int id;
    }
}
