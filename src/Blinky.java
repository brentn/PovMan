import javax.swing.*;
import java.awt.*;

/**
 * Created by brent on 09/01/15.
 */
public class Blinky extends Ghost {

    private static final String BLINKY_IMAGE = "resources/images/blinky.png";

    public Blinky(Point home) {
        super(home);
        scatter_target = new Point(27, 1);
        speed=10;
    }

    public Point getPos() {
        return model.getTile();
    }

    @Override
    protected void updateChaseTarget(Maze maze) {
        chase_target = maze.getMan().getTileAhead(0);
    }

    @Override
    protected void createGhostModel() {
        super.createGhostModel();
        Image image = Toolkit.getDefaultToolkit().getImage(BLINKY_IMAGE);
        model = new ImageModel(home, image, new Point3D(100, 100, 100));
    }
}
