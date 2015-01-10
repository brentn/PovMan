import java.awt.*;

/**
 * Created by brent on 09/01/15.
 */
public class ImageModel extends Model {
    private Position_3D pos;
    private Image image;

    public ImageModel(Position_3D pos, Image image) {
        this.pos = pos;
        this.image = image;
    }

    @Override
    public boolean isVisibleFrom(Camera camera) {
        //TODO:
        return true;
    }

    @Override
    public Canvas drawAsViewedBy(Camera camera) {
        //TODO:
        Canvas canvas = new Canvas();
        return canvas;
    }
}
