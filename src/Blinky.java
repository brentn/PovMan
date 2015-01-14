import java.awt.*;

/**
 * Created by brent on 09/01/15.
 */
public class Blinky extends Ghost {

    public Blinky(Point home) {
        super(home);
        scatter_target = new Point(27, 1);
        speed=1;
    }

    public Point getPos() {
        return model.getTile();
    }

    @Override
    protected void updateChaseTarget(Maze maze) {
        chase_target = maze.getMan().getTileAhead(0);
    }
}
