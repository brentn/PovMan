import java.awt.*;
import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;

/**
 * Created by brent on 08/01/15.
 * For fun
 */
public class Maze {
    public static enum Direction {RIGHT, DOWN, LEFT, UP}

    private Point size;
    private Collection<Wall> walls;
    private Collection<Dot> dots;
    private Collection<Ghost> ghosts;
    private Queue<Wave> waves;
    private Point start_position = new Point(0,0);
    private Direction start_direction = Direction.RIGHT;
    private int doubler;
    private Man man;
    private Blinky blinky;
    private long modeTimer;
    private Timer waveTimer = new Timer();
    private Iterator<Wave> currentWave;

    public Maze(int x, int y) {
        this.size = new Point(x, y);
        walls = new HashSet<Wall>();
        dots = new HashSet<Dot>();
        ghosts = new HashSet<Ghost>();
        waves = new ArrayBlockingQueue<Wave>(20);
        currentWave = waves.iterator();
        man = new Man(start_position, start_direction);
        doubler=1;
    }

    public void addGhosts(Point home) {
        blinky = new Blinky(home);
        ghosts.add(blinky);
        ghosts.add(new Pinky(home));
        ghosts.add(new Inky(home));
        ghosts.add(new Clyde(home));
    }

    public void addWave(Integer duration, Ghost.Mode mode) {
        waves.add(new Wave(duration, mode));
    }

    public void addWall(Wall wall) {
        walls.add(wall);
    }
    public void addWall(Point start, Point end) {
        walls.add(new Wall(start, end));
    }
    public void addDot(Dot dot) {
        dots.add(dot);
    }
    public void addDot(Point pos) {
        dots.add(new Dot(pos));
    }
    public void addDot(int x, int y) {
        dots.add(new Dot(x, y));
    }

    public void setStartPosition(Point pos, Direction dir) {
        start_position = pos;
        start_direction = dir;
    }

    public Dot dotAt(Point pos) {
        for (Dot dot : dots) {
            if (dot.getPos().equals(pos)) return dot;
        }
        return null;
    }

    public boolean wallAt(Point pos) {
        for (Wall wall : walls) {
            if (wall.contains(pos)) return true;
        }
        return false;
    }

    public Man getMan() { return man; }
    public Blinky getBlinky() {return blinky;}

    public boolean manAt(Point pos) {
        return man.getTileAhead(0).equals(pos);
    }

    public void killMan() {
        man.die();
    }

    public void killGhost(int points) {
        man.addPoints(points * doubler);
        doubler = doubler*2;
    }

    public Set<Direction> getExitsFrom(Point tile) {
        Set<Direction> exits = new HashSet<Direction>();
        if (! wallAt(new Point(tile.x-1, tile.y))) exits.add(Direction.LEFT);
        if (! wallAt(new Point(tile.x+1, tile.y))) exits.add(Direction.RIGHT);
        if (! wallAt(new Point(tile.x, tile.y-1))) exits.add(Direction.UP);
        if (! wallAt(new Point(tile.x, tile.y+1))) exits.add(Direction.DOWN);
        return exits;
    }

    private void resetGhosts() {
        currentWave = waves.iterator();
        waveTimer.cancel();
        waveTimer = new Timer();
        getNextWave();
        for (Ghost ghost : ghosts) {
            ghost.reset();
        }
    }

    private void moveGhosts() {
        for (Ghost ghost : ghosts) {
            ghost.move(this);
        }
    }

    public Collection<Wall> getWalls() {return walls;}

    public Collection<Dot> getDots() {return dots;}

    private void getNextWave() {
        if (! currentWave.hasNext()) return;
        Wave wave = currentWave.next();
        for (Ghost ghost : ghosts) {
            ghost.setMode(wave.mode);
            waveTimer.schedule(new TimerTask() {
                @Override
                public void run() {
                    getNextWave();
                }
            }, wave.duration*1000);
        }
    }

    public void run() {
        resetGhosts();
        if (!man.isAlive()) { man.recessutate();}
        while (man.isAlive()) {
            while (man.isAlive()) {
                man.move(this);
                moveGhosts();
            }
            resetGhosts();
            man.recessutate();
        }
    }

    class Wave {
        int duration;
        Ghost.Mode mode;
        public Wave(int d, Ghost.Mode m) {
            duration = d;
            mode = m;
        }
    }

}
