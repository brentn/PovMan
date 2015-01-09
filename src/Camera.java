import java.awt.*;

/**
 * Created by brent on 08/01/15.
 */
public class Camera {
    private static int FIELD_OF_VIEW=120; //degrees
    private Position_3D pos;
    private Position_3D target;

    public Camera(Position_3D pos, Position_3D target) {
        this.pos = pos;
        this.target = target;
    }

    public Image capture(Maze maze) {
        Image result = new Image();
        for (Wall wall : maze.getWalls()) {
            if (isVisible(wall)) {

            }
        }
        return result;
    }
}
