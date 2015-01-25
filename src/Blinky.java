import java.awt.*;

public class Blinky extends Ghost {
    private static final String BLINKY_IMAGE = "resources/images/blinky.png";
    private Image image;

    public Blinky(Point home) {
        super(home);
        scatter_target = new Point(27, 1);
        speed=70;
    }

    public Point getTile() {
        return model.getTile();
    }

    @Override
    protected void updateChaseTarget(Maze maze) {
        chase_target = maze.getMan().getTile();
    }

    protected void restoreImage() {
        model.swapImage(image);
    }

    @Override
    protected void createGhostModel() {
        super.createGhostModel();
        image = Toolkit.getDefaultToolkit().getImage(BLINKY_IMAGE);
        model = new ImageModel(home, image, new Point3D(100, 100, 100));
    }
}
