import java.awt.*;

public class Clyde extends Ghost {
    private static final String CLYDE_IMAGE = "resources/images/clyde.png";
    private Image image;

    public Clyde(Point home) {
        super(home);
        scatter_target = new Point(0, 34);
        speed=80;
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

    protected void restoreImage() {
        model.swapImage(image);
    }

    @Override
    protected void createGhostModel() {
        super.createGhostModel();
        image = Toolkit.getDefaultToolkit().getImage(CLYDE_IMAGE);
        model = new ImageModel(home, image, new Point3D(100, 100, 100));
    }
}
