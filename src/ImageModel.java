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
    public Image drawFrom(Position_3D pos) {
        //TODO:
        return null;
    }
}
