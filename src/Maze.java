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
    private static final File SIREN_SOUND_FILE = new File("resources/sounds/siren.wav");

    private Point size;
    private Collection<Wall> walls;
    private boolean[][] isWall;
    private Collection<Dot> dots;
    private Dot[][] dotAt;
    private Collection<Ghost> ghosts;
    private Queue<Wave> waves;
    private int doubler;
    private Man man;
    private Blinky blinky;
    private Timer waveTimer = new Timer();
    private Timer mazeTimer = new Timer();
    private Iterator<Wave> currentWave;
    private boolean play_audio = true;
    private boolean paused =  true;
    private Sound start_sound =new Sound(START_SOUND_FILE);
    private Sound siren_sound = new Sound(SIREN_SOUND_FILE);

    public Maze(int x, int y) {
        this.size = new Point(x, y);
        this.walls = new HashSet<Wall>();
        this.isWall =  new boolean[x][y];
        this.dots = new HashSet<Dot>();
        this.dotAt = new Dot[size.x][size.y];
        this.ghosts = new HashSet<Ghost>();
        this.waves = new ArrayBlockingQueue<Wave>(20);
        this.currentWave = waves.iterator();
        this.man = new Man(new Point((x/2), (y/2)), Direction.RIGHT);
        this.doubler=1;
        Runnable unpause = new Runnable() {
            @Override
            public void run() {
                paused=false;
            }
        };
        start_sound.whenFinished(unpause);
    }

    public boolean isPaused() { return paused; }

    // WALLS
    public Collection<Wall> getWalls() {return walls;}
    public boolean[][] getWallMask() { return isWall; }
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
        for (int x=start.x; x<=end.x; x++) {
            for (int y=start.y; y<=end.y; y++) {
                isWall[x][y]=true;
            }
        }
    }
    public boolean wallAt(Point pos) {
        if (pos==null) return false;
        return (!isWall[pos.x][pos.y]);
    }
    public Set<Direction> getExitsFrom(Point tile) {
        int x = tile.x;
        int y = tile.y;
        if (isWall[x][y])
            return null;
        Set<Direction> result = new HashSet<Direction>();
        if (! isWall[x-1][y]) result.add(Direction.LEFT);
        if (! isWall[x+1][y]) result.add(Direction.RIGHT);
        if (! isWall[x][y-1]) result.add(Direction.UP);
        if (! isWall[x][y+1]) result.add(Direction.DOWN);
        return result;
    }

    // DOTS
    public Collection<Dot> getDots() {
        return dots;
    }
    public void addDot(Dot dot) {
        if (dotAt[dot.getPos().x][dot.getPos().y]==null) {
            dots.add(dot);
            dotAt[dot.getPos().x][dot.getPos().y] = dot;
        }
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
    public void removeDot(Dot dot) {
        Point pos = dot.getPos();
        dots.remove(dot);
        dotAt[pos.x][pos.y] = null;
    }
    public void clearDots(int startx, int starty, int endx, int endy) {
        for (int x=startx; x<=endx; x++) {
            for (int y=starty; y<=endy; y++) {
                dots.remove(dotAt[x][y]);
                dotAt[x][y]=null;
            }
        }
    }

    // GHOSTS
    public Collection<Ghost> getGhosts() { return ghosts; }
    public void addGhosts(Point home) {
        blinky = new Blinky(new Point(home));
        ghosts.add(blinky);
        ghosts.add(new Pinky(new Point(home)));
        ghosts.add(new Inky(home));
        ghosts.add(new Clyde(home));
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
        System.out.println(wave.mode);
        for (Ghost ghost : ghosts) {
            ghost.setMode(wave.mode);
        }
        waveTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                getNextWave();
            }
        }, wave.duration*1000);

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
        paused=true;
        man.die();
    }

    private void play_siren_sound() {
        if (! paused) siren_sound.loop();
        else siren_sound.stop();
    }

    public void run(final Camera camera) {
        resetGhosts();
        man.recessutate();
        camera.capture(this);
        paused=true;
        start_sound.play();
        do {
            while (man.isAlive()) {
                play_siren_sound();
                man.move(Maze.this);
                moveGhosts();
                if (camera.style== Camera.Style.FOLLOW)
                    camera.follow(man);
                camera.capture(Maze.this);
            }
            resetGhosts();
            man.recessutate();
            paused=true;
            mazeTimer.schedule(new TimerTask() {
                @Override
                public void run() {
                    paused=false;
                }
            }, 3000);
        } while (man.isAlive());
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
