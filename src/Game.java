import javax.sound.sampled.*;
import java.awt.*;
import java.io.File;

/**
 * Created by brent on 08/01/15.
 */
public class Game {

    private Maze maze;
    private Camera camera;
    private int level;

    public Game() {
        level = 1;
        createMaze(level);
        camera = new Camera(new Point3D(maze.getMan().getPos(), 50), Camera.Style.CLASSIC);
        maze.run(camera);
    }

    private void createMaze(int level) {
        maze = new Maze(27, 35);
        maze.setStartPosition(new Point(14, 26), Maze.Direction.RIGHT);

        //temporary walls
        maze.addWall(new Point(5, 16), new Point(5, 18));
        maze.addWall(new Point(22, 16), new Point(22, 18));

        //outer walls
        maze.addWalls(Wall.U(new Point(0, 12), new Point(27, 3)));
        maze.addWalls(Wall.U(new Point(13, 3), new Point(14, 7)));
        maze.addWalls(Wall.sidewaysU(new Point(0, 12), new Point(5, 16)));
        maze.addWalls(Wall.sidewaysU(new Point(27, 12), new Point(22, 16)));
        maze.addWalls(Wall.sidewaysU(new Point(0, 18), new Point(5, 22)));
        maze.addWalls(Wall.sidewaysU(new Point(27, 18), new Point(22, 22)));
        maze.addWalls(Wall.U(new Point(0, 22), new Point(27, 33)));
        maze.addWalls(Wall.sidewaysU(new Point(0, 27), new Point(2, 28)));
        maze.addWalls(Wall.sidewaysU(new Point(27, 27), new Point(25, 28)));

        //inner blocks
        maze.addWalls(Wall.square(new Point(2, 5), new Point(5, 7)));
        maze.addWalls(Wall.square(new Point(7, 5), new Point(11, 7)));
        maze.addWalls(Wall.square(new Point(16, 5), new Point(20, 7)));
        maze.addWalls(Wall.square(new Point(22, 5), new Point(25, 7)));
        maze.addWalls(Wall.square(new Point(2, 9), new Point(5, 10)));
        maze.addWalls(Wall.sidewaysT(new Point(7, 16), new Point(8, 9), new Point(11, 12)));
        maze.addWalls(Wall.sidewaysT(new Point(20, 9), new Point(19, 16), new Point(16, 12)));
        maze.addWalls(Wall.T(new Point(10, 9), new Point(17, 10), new Point(14, 13)));
        maze.addWalls(Wall.square(new Point(22, 9), new Point(25, 10)));
        //ghost house
        maze.addWalls(Wall.square(new Point(10, 15), new Point(17, 19)));
        //lower half
        maze.addWalls(Wall.square(new Point(7, 18), new Point(8, 22)));
        maze.addWalls(Wall.square(new Point(19, 18), new Point(20, 22)));
        maze.addWalls(Wall.T(new Point(10, 21), new Point(17, 22), new Point(14, 25)));
        maze.addWalls(Wall.square(new Point(2, 24), new Point(5, 25)));
        maze.addWalls(Wall.square(new Point(4, 25), new Point(5, 28)));
        maze.addWalls(Wall.square(new Point(7, 24), new Point(11, 25)));
        maze.addWalls(Wall.square(new Point(16, 24), new Point(20, 25)));
        maze.addWalls(Wall.square(new Point(22, 24), new Point(25, 25)));
        maze.addWalls(Wall.square(new Point(22, 25), new Point(23, 28)));
        maze.addWalls(Wall.T(new Point(10, 27), new Point(17, 28), new Point(14, 31)));
        maze.addWalls(Wall.T(new Point(11, 31), new Point(2, 30), new Point(8, 27)));
        maze.addWalls(Wall.T(new Point(25, 31), new Point(16, 30), new Point(20, 27)));

        //vert cols are 6 and 21
        maze.addDots(Dot.line(new Point(1, 4), new Point(12, 4)));
        maze.addDots(Dot.line(new Point(15, 4), new Point(26, 4)));
        maze.addDots(Dot.line(new Point(6, 5), new Point(6, 29)));
        maze.addDots(Dot.line(new Point(21, 5), new Point(21, 29)));
        maze.addDots(Dot.line(new Point(1, 32), new Point(26, 32)));
        maze.addDots(Dot.line(new Point(7, 26), new Point(13, 26)));
        maze.addDots(Dot.line(new Point(15, 26), new Point(21, 26)));

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

