import java.awt.*;

public class Inky extends Ghost {
    private static final String INKY_IMAGE = "resources/images/inky.png";
    private Image image;

    public Inky(Point home) {
        super(home);
        scatter_target = new Point(27, 34);
        speed=75;
    }

    @Override
    protected void updateChaseTarget(Maze maze) {
        Point p1 = maze.getMan().getTileAhead(2);
        Point p2 = maze.getBlinky().getTile();
        int dx = (p1.x-p2.x)*2;
        int dy = (p1.y-p2.y)*2;
        chase_target = new Point (p2.x+dx, p2.y+dy);
    }

    protected void restoreImage() {
        model.swapImage(image);
    }

    @Override
    protected void createGhostModel() {
        super.createGhostModel();
        image = Toolkit.getDefaultToolkit().getImage(INKY_IMAGE);
        model = new ImageModel(home, image, new Point3D(100, 100, 100));
    }
}
