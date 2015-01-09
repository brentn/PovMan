import java.awt.*;

/**
 * Created by brent on 08/01/15.
 */
public class Game {
    private static Position_3D CLASSIC_CAMERA_POS = new Position_3D(12, 12, 40);
    private static Position_3D CLASSIC_CAMERA_TARGET = new Position_3D(12, 12, 0);

    private Maze maze;
    private Camera camera = new Camera(CLASSIC_CAMERA_POS, CLASSIC_CAMERA_TARGET);
    private int level;
    private Canvas screen;

    public Game() {
        level = 1;
        maze = createMaze(level);
    }

    public void play() {
        camera.capture(maze);
    }

    private Maze createMaze(int level) {
        Point top_left = new Point(1,1);
        Point top_right = new Point(24, 1);
        Point bottom_left = new Point(1, 24);
        Point bottom_right = new Point(24, 24);
        Maze result = new Maze();
        result.addWall(top_left, top_right);
        result.addWall(top_right, bottom_right);
        result.addWall(bottom_right, bottom_left);
        result.addWall(bottom_left, top_left);
        result.addDot(2,2);
        result.addDot(2, 23);
        result.addDot(23, 2);
        result.addDot(23, 23);
        return result;
    }
}
