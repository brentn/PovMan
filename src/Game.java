import java.awt.*;

/**
 * Created by brent on 08/01/15.
 */
public class Game {
    private static Point3D CLASSIC_CAMERA_POS = new Point3D(0, 0, 8);
    private static Point3D CLASSIC_CAMERA_TARGET = new Point3D(14, 18, 1);

    private Maze maze;
    private Camera camera;
    private int level;

    public Game() {
        level = 1;
        createMaze(level);
        camera = new Camera(CLASSIC_CAMERA_POS, CLASSIC_CAMERA_TARGET);
        //while (true) {camera.capture(maze); camera.update();}
        maze.run(camera);
    }

    private void createMaze(int level) {
        maze = new Maze(27, 35);
        maze.setStartPosition(new Point(13, 29), Maze.Direction.RIGHT);
        maze.addWall(new Point(0, 3), new Point(14, 3));
        maze.addWall(new Point(14, 3), new Point(14, 7));
        maze.addWall(new Point(14, 7), new Point(15, 7));
        maze.addWall(new Point(15, 7), new Point(15, 3));
        maze.addWall(new Point(15, 3), new Point(28, 3));
        maze.addWall(new Point(0, 3), new Point(0, 12));
        maze.addWall(new Point(28, 3), new Point(28, 12));
        maze.addWall(new Point(0, 12), new Point(5, 12));
        maze.addWall(new Point(5, 12), new Point(5, 16));
        maze.addWall(new Point(5, 16), new Point(0, 16));
        maze.addWall(new Point(28, 12), new Point(23, 12));
        maze.addWall(new Point(23, 12), new Point(23, 16));
        maze.addWall(new Point(23, 16), new Point(28, 16));

        maze.addWall(new Point(0, 16), new Point(0, 18));
        maze.addWall(new Point(28, 16), new Point(28, 18));

        maze.addWall(new Point(0, 18), new Point(5, 18));
        maze.addWall(new Point(28, 18), new Point(23, 18));
        maze.addWall(new Point(5, 18), new Point(5, 22));
        maze.addWall(new Point(23, 18), new Point(23, 22));
        maze.addWall(new Point(5, 22), new Point(0, 22));
        maze.addWall(new Point(23, 22), new Point(28, 22));
        maze.addWall(new Point(0, 22), new Point(0, 27));
        maze.addWall(new Point(28, 22), new Point(28, 27));
        maze.addWall(new Point(0, 27), new Point(3, 27));
        maze.addWall(new Point(28, 27), new Point(26, 27));
        maze.addWall(new Point(3, 27), new Point(3, 28));
        maze.addWall(new Point(26, 27), new Point(26, 28));
        maze.addWall(new Point(3, 28), new Point(0, 28));
        maze.addWall(new Point(27, 28), new Point(28, 28));
        maze.addWall(new Point(0, 28), new Point(0, 33));
        maze.addWall(new Point(28, 28), new Point(28, 33));
        maze.addWall(new Point(0, 33), new Point(28, 33));

        maze.addWall(new Point(7, 9), new Point(8, 9));
        maze.addWall(new Point(8, 9), new Point(8, 12));
        maze.addWall(new Point(8, 12), new Point(12, 12));
        maze.addWall(new Point(12, 12), new Point(12, 13));
        maze.addWall(new Point(12, 13), new Point(8, 13));
        maze.addWall(new Point(8, 13), new Point(8, 16));
        maze.addWall(new Point(8, 16), new Point(7, 16));
        maze.addWall(new Point(7, 16), new Point(7, 9));

        maze.addWall(new Point(21, 9), new Point(20, 9));
        maze.addWall(new Point(20, 9), new Point(20, 12));
        maze.addWall(new Point(20, 12), new Point(17, 12));
        maze.addWall(new Point(17, 12), new Point(17, 13));
        maze.addWall(new Point(17, 13), new Point(20, 13));
        maze.addWall(new Point(20, 13), new Point(20, 16));
        maze.addWall(new Point(20, 16), new Point(21, 16));
        maze.addWall(new Point(21, 16), new Point(21, 9));


        maze.addWall(new Point(11, 9), new Point(18, 9));
        maze.addWall(new Point(11, 9), new Point(11, 10));
        maze.addWall(new Point(18, 9), new Point(18, 10));
        maze.addWall(new Point(11, 10), new Point(14, 10));
        maze.addWall(new Point(18, 10), new Point(15, 10));
        maze.addWall(new Point(14, 10), new Point(14, 14));
        maze.addWall(new Point(15, 10), new Point(15, 14));
        maze.addWall(new Point(14, 14), new Point(15, 14));


        maze.addDot(1, 2);
        maze.addDot(1, 33);
        maze.addDot(26, 2);
        maze.addDot(26, 33);
        maze.addGhosts(new Point(13, 13));
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

