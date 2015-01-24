import java.awt.*;
import java.util.Collection;
import java.util.HashSet;


public class PowerPill extends Dot implements IModel {
    private static final int DEFAULT_POINTS=50;
    private static final int HEIGHT = 40;
    private int duration;

    public PowerPill(Point pos, int seconds) {
        super(pos);
        super.DEFAULT_POINTS = DEFAULT_POINTS;
        super.HEIGHT = HEIGHT;
        duration=seconds;
        createPowerPillModel();
    }
    public PowerPill(int x, int y, int seconds) {
        super(new Point(x, y));
        super.DEFAULT_POINTS = DEFAULT_POINTS;
        super.HEIGHT = HEIGHT;
        duration=seconds;
        createPowerPillModel();
    }

    public int getDuration() {return duration;}

    private void createPowerPillModel() {
        Point3D pos3d = new Point3D(pos, HEIGHT);
        model = new ImageModel(pos3d, null, new Point3D(70,70,70));
    }

}
