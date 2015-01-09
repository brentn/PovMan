import java.awt.*;

/**
 * Created by brent on 08/01/15.
 */
public class Position_3D extends Point {
    private int height;

    public Position_3D(int x, int y, int height) {
        super(x, y);
        this.height = height;
    }
    public Position_3D(Point pt, int height) {
        super(pt);
        this.height = height;
    }
}
