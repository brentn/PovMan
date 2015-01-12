import java.awt.*;
import java.util.Collection;
import java.util.HashSet;

/**
 * Created by brent on 08/01/15.
 */
public class Wall implements IModel {
    private static int WALL_HEIGHT=1;

    private Point start;
    private Point end;
    private WireframeModel model;

    public Wall(Point start, Point end) {
        this.start = start;
        this.end = end;
        createWallModel();
    }
    public Wall(int startx, int starty, int endx, int endy) {
        this.start = new Point(startx, starty);
        this.end = new Point(endx, endy);
        createWallModel();
    }

    public boolean contains(Point pos) {
        if (isVertical()) {
            if (pos.x != start.x) return false;
            if ((pos.y > start.y) && (pos.y > end.y)) return false;
            if ((pos.y < start.y) && (pos.y < end.y)) return false;
        }  else {
            if (pos.y != start.y) return false;
            if ((pos.x > start.x) && (pos.x > end.x)) return false;
            if ((pos.x < start.x) && (pos.x < end.x)) return false;
        }
        return true;
    }

    private boolean isVertical() { return start.x == end.x; }

    private void createWallModel(){
        model = new WireframeModel();
        model.addVertex(new Point3D(start, 0));
        model.addVertex(new Point3D(end, 0));
        model.addVertex(new Point3D(end, WALL_HEIGHT));
        model.addVertex(new Point3D(start, WALL_HEIGHT));
        model.addEdge(new Edge(0, 1));
        model.addEdge(new Edge(1, 2));
        model.addEdge(new Edge(2, 3));
        model.addEdge(new Edge(3, 0));
    }

    @Override
    public Model getModel() {
        return model;
    }

    //Static helper routines
    public static Collection<Wall> square(Point a, Point b) {
        Collection<Wall> walls = new HashSet<Wall>();
        walls.add(new Wall(a.x, a.y, b.x, a.y));
        walls.add(new Wall(b.x, a.y, b.x, b.y));
        walls.add(new Wall(b.x, b.y, a.x, b.y));
        walls.add(new Wall(a.x, b.y, a.x, a.y));
        return walls;
    }

    public static Collection<Wall> U(Point a, Point b) {
        Collection<Wall> walls = new HashSet<Wall>();
        walls.add(new Wall(a.x, a.y, a.x, b.y));
        walls.add(new Wall(a.x, b.y, b.x, b.y));
        walls.add(new Wall(b.x, b.y, b.x, a.y));
        return walls;
    }

    public static Collection<Wall> sidewaysU(Point a, Point b) {
        Collection<Wall> walls = new HashSet<Wall>();
        walls.add(new Wall(a.x, a.y, b.x, a.y));
        walls.add(new Wall(b.x, a.y, b.x, b.y));
        walls.add(new Wall(b.x, b.y, a.x, b.y));
        return walls;
    }

    public static Collection<Wall> T(Point a, Point b, Point c) {
        Collection<Wall> walls = new HashSet<Wall>();
        walls.add(new Wall(a.x, a.y, b.x, a.y));
        walls.add(new Wall(b.x, a.y, b.x, b.y));
        walls.add(new Wall(b.x, b.y, c.x, b.y));
        walls.add(new Wall(c.x, b.y, c.x, c.y));
        walls.add(new Wall(c.x, c.y, c.x-1, c.y));
        walls.add(new Wall(c.x-1, c.y, c.x-1, b.y));
        walls.add(new Wall(c.x-1, b.y, a.x, b.y));
        walls.add(new Wall(a.x, b.y, a.x, a.y));
        return walls;
    }

    public static Collection<Wall> sidewaysT(Point a, Point b, Point c) {
        Collection<Wall> walls = new HashSet<Wall>();
        walls.add(new Wall(a.x, a.y, a.x, b.y));
        walls.add(new Wall(a.x, b.y, b.x, b.y));
        walls.add(new Wall(b.x, b.y, b.x, c.y));
        walls.add(new Wall(b.x, c.y, c.x, c.y));
        walls.add(new Wall(c.x, c.y, c.x, c.y+1));
        walls.add(new Wall(c.x, c.y+1, b.x, c.y+1));
        walls.add(new Wall(b.x, c.y+1, b.x, a.y));
        walls.add(new Wall(b.x, a.y, a.x, a.y));
        return walls;
    }
}
