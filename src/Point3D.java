import java.awt.*;

/**
 * Created by brent on 08/01/15.
 */
public class Point3D extends Point {
    public int z;

    public Point3D(int x, int y, int z) {
        super(x, y);
        this.z = z;
    }
    public Point3D(Point pt, int height) {
        super(pt);
        this.z = height;
    }

}
