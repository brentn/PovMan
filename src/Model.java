import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Comparator;

/**
 * Created by brent on 09/01/15.
 */
public abstract class Model {
    int distance=0;
    public abstract boolean isVisibleFrom(Camera camera);
    public abstract void calculateDistance(Point from);
    public abstract void drawAsViewedBy(Camera camera);
    protected int getDistance() {return distance;}
}
