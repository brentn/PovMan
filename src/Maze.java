import java.awt.*;
import java.util.Collection;
import java.util.HashSet;

/**
 * Created by brent on 08/01/15.
 */
public class Maze {
    private static int WALL_HEIGHT=1;

    private Collection<Wall> walls;
    private Collection<Dot> dots;
    private Man man;

    public Maze() {
        walls = new HashSet<Wall>();
        dots = new HashSet<Dot>();
        man = new Man();
    }

    public void addWall(Wall wall) {
        walls.add(wall);
    }
    public void addWall(Point start, Point end) {
        walls.add(new Wall(start, end));
    }
    public void addDot(Dot dot) {
        dots.add(dot);
    }
    public void addDot(Point pos) {
        dots.add(new Dot(pos));
    }
    public void addDot(int x, int y) {
        dots.add(new Dot(x, y));
    }

    public Collection<Wall> getWalls() {return walls;}
    public Collection<Dot> getDots() {return dots;}

}
