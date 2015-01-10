import java.awt.*;

/**
 * Created by brent on 09/01/15.
 */
public abstract class Model {
    public abstract boolean isVisibleFrom(Camera camera);
    public abstract Canvas drawAsViewedBy(Camera camera);
}
