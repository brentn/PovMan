import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Created by brent on 09/01/15.
 */
public abstract class Model {
    public abstract boolean isVisibleFrom(Camera camera);
    public abstract void drawAsViewedBy(Camera camera);
}
