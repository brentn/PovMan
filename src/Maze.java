import java.awt.*;
import java.io.File;
import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;

/**
 * Created by brent on 08/01/15.
 * For fun
 */
public class Maze {
    public static enum Direction {RIGHT, DOWN, LEFT, UP, NONE}

    private static final File START_SOUND_FILE = new File("resources/sounds/start.wav");
    private static final File SIREN_SOUND_FILE = new File("resources/sounds/siren.wav");
    private static final File DIE_SOUND_FILE = new File("resources/sounds/die.wav");

    private Point size;
    private Collection<Wall> walls;
    private boolean[][] isWall;
    private Collection<Dot> dots;
    private Dot[][] dotAt;
    private Collection<Ghost> ghosts;
    private Queue<Wave> waves;
    private Man man;
    private Blinky blinky;
    private Timer waveTimer = new Timer();
    private Timer mazeTimer = new Timer();
    private Iterator<Wave> currentWave;
    private boolean paused =  true;
    private Runnable unpause = new Runnable() {
        @Override
        public void run() {
            paused=false;
        }
    };
    private Sound start_sound =new Sound(START_SOUND_FILE);
    private Sound siren_sound = new Sound(SIREN_SOUND_FILE);
    private Sound die_sound = new Sound(DIE_SOUND_FILE);

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
        start_sound.whenFinished(unpause);
    }

    public boolean isPaused() { return paused; }
    public int width() {return size.x;}


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
        Set<Direction> result = new HashSet<Direction>();
        if (isWall[x][y])
            return result;
        if ((x==0) || (! isWall[x-1][y])) result.add(Direction.LEFT);
        if ((x+1)==width() || (! isWall[x+1][y])) result.add(Direction.RIGHT);
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
        ghosts.add(new Pinky(new Point(home.x-1,home.y)));
        ghosts.add(new Inky(new Point(home.x+2, home.y)));
        ghosts.add(new Clyde(new Point(home.x+1, home.y)));
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
    public void killGhost(Ghost ghost) {
        int points = ghost.kill();
        ghost.showPoints(points);
        man.addPoints(points);
        man.increaseDoubler();
        ghost.alive=false;
        ghost.retreat();
    }
    public void resetDeadGhosts() {
        for (Ghost ghost : ghosts) {
            if (!ghost.isAlive())
                ghost.reset();
        }
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
    public String getScore() { return "Score: "+man.getPoints();}
    public void resetMan() {
        man.recessutate();
    }
    public boolean manAt(Point pos) {
        return man.getTileAhead(0).equals(pos);
    }
    public void killMan() {
        paused=true;
        siren_sound.stop();
        man.die();
        mazeTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                man.recessutate();
                if (man.isAlive()) resetGhosts();
            }
        }, 3000);
        mazeTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (man.isAlive()) paused=false;
                else lose();
            }
        }, 5000);
    }

    public void start(final Camera camera) {
        paused=true;
        resetMan();
        resetGhosts();
        camera.capture(this);
        mazeTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
            if (!paused)
                siren_sound.loop();
            man.control(Maze.this, camera.getPressed_keys());
            //man.update(Maze.this);
            for (Ghost ghost : ghosts) {
                ghost.update(Maze.this);
            }
            if (camera.style == Camera.Style.FOLLOW)
                camera.follow(man);
            camera.capture(Maze.this);
            if (dots.size()==0) win();
            }
        },0,40);
        start_sound.play();
    }

    private void lose() {
        waveTimer.cancel();
        mazeTimer.cancel();
        siren_sound.stop();
    }

    private void win() {
        waveTimer.cancel();
        mazeTimer.cancel();
        siren_sound.stop();
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
