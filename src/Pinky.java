import java.awt.*;

/**
 * Created by brent on 09/01/15.
 */
public class Pinky extends Ghost {

    public Pinky(Point home) {
        super(home);
        scatter_target = new Point(0, 1);
        speed=3;
    }

    @Override
    protected void updateChaseTarget(Maze maze) {
        chase_target = maze.getMan().getTileAhead(4);
    }
}
