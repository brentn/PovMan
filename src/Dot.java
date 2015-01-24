import javax.sound.sampled.*;
import java.awt.*;
import java.io.File;
import java.util.Collection;
import java.util.HashSet;


public class Dot extends Consumable implements IModel {
    private static final int DEFAULT_POINTS=10;
    private static final int HEIGHT = 50;

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

    public static Collection<Dot> line(int startx, int starty, int endx, int endy) {
        Collection<Dot> dots = new HashSet<Dot>();
        if (startx==endx) {
            for (int y=starty; y <= endy; y++) {
                dots.add(new Dot(startx, y));
            }
        } else {
            for (int x=startx; x <= endx; x++) {
                dots.add(new Dot(x, starty));
            }
        }
        return dots;
    }

    public static Collection<Dot> fill(int startx, int starty, int endx, int endy, boolean[][] wallAt) {
        Collection<Dot> dots = new HashSet<Dot>();
        for (int x=startx; x<=endx; x++) {
            for (int y=starty; y<=endy; y++) {
                if (! wallAt[x][y]) {
                    dots.add(new Dot(x, y));
                }
            }
        }
        return dots;
    }

}
