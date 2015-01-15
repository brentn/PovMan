import javax.sound.sampled.*;
import java.awt.*;
import java.io.File;
import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;

/**
 * Created by brent on 08/01/15.
 * For fun
 */
public class Maze {
    public static enum Direction {RIGHT, DOWN, LEFT, UP}

    private static final File START_SOUND_FILE = new File("resources/sounds/start.wav");

    private Point size;
    private Collection<Wall> walls;
    private boolean[][] isPath;
    private Collection<Dot> dots;
    private Dot[][] dotAt;
    private Collection<Ghost> ghosts;
    private Queue<Wave> waves;
    private int doubler;
    private Man man;
    private Blinky blinky;
    private long modeTimer;
    private Timer waveTimer = new Timer();
    private Iterator<Wave> currentWave;
    private boolean play_audio = true;
    private Sound start_sound;

    public Maze(int x, int y) {
        this.size = new Point(x, y);
        this.walls = new HashSet<Wall>();
        this.isPath =  new boolean[x][y];
        this.dots = new HashSet<Dot>();
        this.dotAt = new Dot[x][y];
        this.ghosts = new HashSet<Ghost>();
        this.waves = new ArrayBlockingQueue<Wave>(20);
        this.currentWave = waves.iterator();
        this.man = new Man(new Point((int)(x/2), (int)(y/2)), Direction.RIGHT);
        this.doubler=1;
        this.start_sound = new Sound(START_SOUND_FILE);
    }

    // WALLS
    public Collection<Wall> getWalls() {return walls;}
    public void addWall(Wall wall) {
        addNewWall(wall);
    }
    public void addWall(Point start, Point end) {
        addNewWall(new Wall(start, end));
    }
    public void addWalls(Collection<Wall> new_walls) {
        for (Wall wall : new_walls) {
            addNewWall(wall);
        }
    }
    private void addNewWall(Wall wall) {
        walls.add(wall);
        toggleIsPath(wall);
    }
    private void toggleIsPath(Wall wall) {
        Point start = wall.getStart();
        Point end = wall.getEnd();
        for (int x=start.x; x<end.x; x++) {
            for (int y=start.y; y<end.y; y++) {
                isPath[x][y]=false;
            }
        }
    }
    public boolean wallAt(Point pos) {
        return (!isPath[pos.x][pos.y]);
    }
    public Set<Direction> getExitsFrom(Point tile) {
        int x = tile.x;
        int y = tile.y;
        if (! isPath[x][y])
            return null;
        Set<Direction> result = new HashSet<Direction>();
        if (isPath[x-1][y]) result.add(Direction.LEFT);
        if (isPath[x+1][y]) result.add(Direction.RIGHT);
        if (isPath[x][y-1]) result.add(Direction.UP);
        if (isPath[x][y+1]) result.add(Direction.DOWN);
        return result;
    }

    // DOTS
    public Collection<Dot> getDots() {
        return dots;
    }
    public void addDot(Dot dot) {
        dots.add(dot);
        dotAt[dot.getPos().x][dot.getPos().y]=dot;
    }
    public void addDot(Point pos) {
        addDot(new Dot(pos));
    }
    public void addDot(int x, int y) {
        addDot(new Dot(x, y));
    }
    public void addDots(Collection<Dot> new_dots) {
        for (Dot dot : new_dots) {
            addDot(dot);
        }
    }
    public Dot dotAt(Point pos) {
        return dotAt[pos.x][pos.y];
    }
    public void remove(Dot dot) {
        Point pos = dot.getPos();
        dotAt[pos.x][pos.y] = null;
    }

    // GHOSTS
    public Collection<Ghost> getGhosts() { return ghosts; }
    public void addGhosts(Point home) {
        blinky = new Blinky(home);
        ghosts.add(blinky);
//        ghosts.add(new Pinky(home));
//        ghosts.add(new Inky(home));
//        ghosts.add(new Clyde(home));
    }
    public Blinky getBlinky() {return blinky;}
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
    public void killGhost(int points) {
        man.addPoints(points * doubler);
        doubler = doubler*2;
    }
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
    public void addWave(Integer duration, Ghost.Mode mode) {
        waves.add(new Wave(duration, mode));
    }

    // MAN
    public void setStartPosition(Point pos, Direction dir) {
        man.setStartPosition(pos, dir);
    }
    public Man getMan() { return man; }
    public boolean manAt(Point pos) {
        return man.getTileAhead(0).equals(pos);
    }
    public void killMan() {
        man.die();
    }

    public void initialize() {
        resetGhosts();
        if (! man.isAlive()) man.recessutate();
        start_sound.after(run);
    }

    public void run(Camera camera) {
        final Camera c = camera;
        camera.capture(this);
        start_sound.after(start_gameplay());
        start_sound().play();
        if (play_audio) {
            LineListener listener = new LineListener() {
                public void update(LineEvent event) {
                    if (event.getType() == LineEvent.Type.STOP) {
                        play(c);
                    }
                }
            };
            start_sound.addLineListener(listener);
            start_sound.start();
        } else {
            play(camera);
        }
    }

    private void start_gameplay(Camera camera) {
        while (man.isAlive()) {
            while (man.isAlive()) {
                man.move(this);
                moveGhosts();
                if (camera.style== Camera.Style.FOLLOW)
                    camera.follow(man);
                camera.capture(this);
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
