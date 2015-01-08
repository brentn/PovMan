import java.util.Collection;

/**
 * Created by brent on 08/01/15.
 */
public class Grid {
    private Collection<Wall> walls;
    private Collection<Dot> dots;

    public Grid() {
        walls = new Collection<Wall>();
        dots = new Collection<Dot>();
    }

    public void addWall(Wall wall) {
        walls.Add(wall);
    }
    public void addWall(Position start, Position end) {
        walls.Add(new Wall(start, end));
    }
    public void addDot(Dot dot) {
        dots.Add(dot);
    }
    public void addDot(Position pos) {
        dots.Add(new Dot(pos));
    }
    public void addDot(int x, int y) {
        dots.Add(new Dot(x, y));
    }

    public void draw(Man man) {

    }
}
