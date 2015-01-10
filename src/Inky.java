import java.awt.*;

/**
 * Created by brent on 09/01/15.
 */
public class Inky extends Ghost {

    public Inky(Point home) {
        super(home);
        scatter_target = new Point(23, 35);
        speed=3;
    }

    @Override
    protected void updateChaseTarget(Maze maze) {
        Point p1 = maze.getMan().getTileAhead(2);
        Point p2 = maze.getBlinky().getPos();
        int dx = (p1.x-p2.x)*2;
        int dy = (p1.y-p2.y)*2;
        chase_target = new Point (p2.x+dx, p2.y+dy);
    }
}
