import java.awt.*;

/**
 * Created by brent on 09/01/15.
 */
public class Clyde extends Ghost {

    public Clyde(Point home) {
        super(home);
        scatter_target = new Point(0, 35);
        speed=3;
    }

    public Point getPos() {
        return model.getTile();
    }

    @Override
    protected void updateChaseTarget(Maze maze) {
        Point manPos = maze.getMan().getTileAhead(0);
        double a = manPos.x-model.getTile().x;
        double b = manPos.y-model.getTile().y;
        double distance = Math.sqrt(a*a+b*b);
        if (distance > 8) {
            chase_target = manPos;
        } else {
            chase_target = scatter_target;
        }
    }
}
