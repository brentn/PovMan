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
        createMaze(level);
        maze.run();
    }

    private void createMaze(int level) {
        maze = new Maze(27, 35);
        Point top_left = new Point(0,1);
        Point top_right = new Point(27, 1);
        Point bottom_left = new Point(0, 34);
        Point bottom_right = new Point(27, 34);
        maze.addWall(top_left, top_right);
        maze.addWall(top_right, bottom_right);
        maze.addWall(bottom_right, bottom_left);
        maze.addWall(bottom_left, top_left);
        maze.addDot(1, 2);
        maze.addDot(1, 33);
        maze.addDot(26, 2);
        maze.addDot(26, 33);
        maze.addGhosts(new Point(13, 13));
        maze.setStartPosition(new Point(13, 29), Maze.Direction.RIGHT);
        maze.addWave(7, Ghost.Mode.SCATTER);
        maze.addWave(20, Ghost.Mode.CHASE);
        maze.addWave(7, Ghost.Mode.SCATTER);
        maze.addWave(20, Ghost.Mode.CHASE);
        maze.addWave(5, Ghost.Mode.SCATTER);
        maze.addWave(20, Ghost.Mode.CHASE);
        maze.addWave(5, Ghost.Mode.SCATTER);
        maze.addWave(20, Ghost.Mode.CHASE);
    }

    public static void main(String [ ] args)
    {
        Game game = new Game();
    }

}

