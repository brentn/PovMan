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
    private Point start_position = new Point(0,0);
    private Man.Direction start_direction = Man.Direction.RIGHT;
    private Man man;

    public Maze() {
        walls = new HashSet<Wall>();
        dots = new HashSet<Dot>();
        man = new Man(start_position, start_direction);
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

    public Dot dotAt(Point pos) {
        for (Dot dot : dots) {
            if (dot.getPos().equals(pos)) return dot;
        }
        return null;
    }

    public boolean wallAt(Point pos) {
        for (Wall wall : walls) {
            if (wall.contains(pos)) return true;
        }
        return false;
    }

    public void setStartPosition(Point pos, Man.Direction dir) {
        start_position = pos;
        start_direction = dir;
    }

    public Collection<Wall> getWalls() {return walls;}
    public Collection<Dot> getDots() {return dots;}

    public void run() {
        if (!man.isAlive()) { man.recessutate();}
        while (man.isAlive()) {
            man.move(this);
        }
    }
}
