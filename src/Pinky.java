import java.awt.*;

/**
 * Created by brent on 09/01/15.
 */
public class Pinky extends Ghost {
    private static final String PINKY_IMAGE = "resources/images/pinky.png";

    public Pinky(Point home) {
        super(home);
        scatter_target = new Point(0, 1);
        speed=11;
    }

    @Override
    protected void updateChaseTarget(Maze maze) {
        chase_target = maze.getMan().getTileAhead(4);
    }

    protected void createGhostModel() {
        super.createGhostModel();
        Image image = Toolkit.getDefaultToolkit().getImage(PINKY_IMAGE);
        model = new ImageModel(home, image, new Point3D(100, 100, 100));
    }
}
