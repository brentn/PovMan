import java.awt.*;

/**
 * Created by brent on 08/01/15.
 */
public class Dot extends Consumable implements IModel {
    private static int DEFAULT_POINTS=100;
    private static int HEIGHT = 0;
    private Point pos;
    private ImageModel model;

    public Dot(Point pos) {
        super(DEFAULT_POINTS);
        this.pos = pos;
        createDotModel();
    }
    public Dot(int x, int y) {
        super(DEFAULT_POINTS);
        this.pos = new Point(x, y);
        createDotModel();
    }

    public Point getPos() { return pos; }

    private void createDotModel() {
        Point3D pos3d = new Point3D(pos, HEIGHT);
        model = new ImageModel(pos3d, null, new Point3D(25,25,25));
    }

    @Override
    public Model getModel() {
        return model;
    }
}
