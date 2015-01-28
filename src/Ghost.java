import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Random;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

public abstract class Ghost extends Consumable implements IModel {

    public static enum Mode {CHASE, SCATTER, FRIGHTENED}
    private static final String FRIGHTENED_IMAGE = "resources/images/frightened.png";
    private static final String EYES_IMAGE = "resources/images/eyes.png";
    private static final File DIE_SOUND_FILE = new File("resources/sounds/kill.wav");
    public static final int FRIGHTENED_SPEED = 50;
    public static final int TUNNEL_SPEED = 40;
    private static final int POINTS = 200;

    protected Point home;
    protected int speed;
    protected boolean undecided;
    protected Maze.Direction direction;
    protected boolean alive=false;
    protected Point chase_target;
    protected Point scatter_target;
    protected Mode lastmode;
    protected Mode mode = Mode.SCATTER;
    private Image frightened;
    protected ImageModel model;
    private Timer timer;
    private boolean flashing=false;
    private Sound die_sound = new Sound(DIE_SOUND_FILE);

    public Ghost(Point home) {
        super(POINTS);
        this.home = home;
        this.direction = Maze.Direction.LEFT;
        this.frightened = Toolkit.getDefaultToolkit().getImage(FRIGHTENED_IMAGE);
        timer=new Timer();
        createGhostModel();
    }

    public boolean isAlive() { return alive; }

    public void setMode(Mode mode) {
        if (this.mode==mode) return;
        this.mode=mode;
        direction = reverse();
        undecided = true;
    }

    public void reset() {
        model.setTile(home);
        if (direction == Maze.Direction.UP)
            direction = Maze.Direction.RIGHT;
        else if (direction == Maze.Direction.DOWN)
            direction = Maze.Direction.LEFT;
        model.setOffset(50, 50);
        undecided=true;
        alive=true;
    }

    public int kill() {
        if (alive) {
            alive = false;
            return consume();
        }
        return 0;
    }

    public void retreat() {
        die_sound.play();
        //model.swapImage(eyes_image);
        reset();
        alive=false;
    }

    public void update(Maze maze) {
        if (maze.getMan().isEmpowered()) {
            if (mode != Mode.FRIGHTENED) {
                lastmode = mode;
                setMode(Mode.FRIGHTENED);
                model.swapImage(frightened);
            }
        } else {
            if (mode==Mode.FRIGHTENED  && !flashing)
                flash();
        }
        if (alive && ! maze.isPaused()) {
            if (model.pastCenterOfTile(direction)) {
                if (undecided) {
                    Set<Maze.Direction> exits = maze.getExitsFrom(model.getTile());
                    Maze.Direction reverse = reverse();
                    if (exits.contains(reverse)) { exits.remove(reverse); }
                    if (exits.size()==0)
                        return;
                    if (exits.size() > 1) {
                        if (mode==Mode.FRIGHTENED) { //if frightened, movement is random
                            int item = new Random().nextInt(exits.size());
                            int i=0;
                            for (Maze.Direction d : exits) {
                                if (i==item) direction=d;
                                i++;
                            }
                        } else {
                            updateChaseTarget(maze);
                            chooseBestRoute(exits);
                        }
                    } else { //only 1 exit
                        direction = exits.iterator().next();
                        model.reorient(direction);
                    }
                    undecided = false;
                }
            } else {
                undecided=true;
            }
            if (inTunnel()) {
                model.move(TUNNEL_SPEED, direction);
            } else {
                if (mode == Mode.FRIGHTENED) {
                    model.move(FRIGHTENED_SPEED, direction);
                } else {
                    model.move(speed, direction);
                }
            }
            Point tile = model.getTile();
            if (tile.x >= (maze.width()-1))
                model.setTile(new Point(1, tile.y));
            if (tile.x <= 0)
                model.setTile(new Point(maze.width()-2, tile.y));
            // check if ghost and man are touching
            if (maze.manAt(model.getTile())) {
                if (mode==Mode.FRIGHTENED) {
                    maze.killGhost(this);
                } else {
                    maze.killMan();
                }
            }
        }
    }

    private boolean inTunnel() {
        if (model.getTile().y != 16) return false;
        if ((model.getTile().x < 22) && (model.getTile().x > 5)) return false;
        return true;
    }

    public void showPoints(int points) {

    }

    public void flash() {
        flashing=true;
        for (int i=0; i<6; i=i+2) {
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    model.swapImage(frightened);
                }
            }, 250*i);
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    restoreImage();
                }
            }, 250*i+250);
        }
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                flashing=false;
                alive=true;
            }
        }, 1500);
    }

    protected abstract void restoreImage();

    private Point nextTile() {
        if (! model.pastCenterOfTile(direction)) return null;
        Point tile=model.getTile();
        switch (direction) {
            case LEFT: return new Point(tile.x-1, tile.y);
            case RIGHT: return new Point(tile.x+1, tile.y);
            case UP: return new Point(tile.x, tile.y-1);
            case DOWN: return new Point(tile.x, tile.y+1);
        }
        return null;
    }

    protected abstract void updateChaseTarget(Maze maze);

    private void chooseBestRoute(Set<Maze.Direction> exits) {
        double shortestDistance = Double.MAX_VALUE;
        Maze.Direction shortestDirection = null;
        int x = model.getTile().x;
        int y = model.getTile().y;
        for (Maze.Direction exit : exits) {
            Point tile=null;
            switch (exit) {
                case LEFT: tile=new Point(x-1, y); break;
                case RIGHT: tile=new Point(x+1, y); break;
                case UP: tile=new Point(x, y-1); break;
                case DOWN: tile=new Point(x, y+1); break;
            }
            double distance = distanceToTarget(tile);
            if (distance < shortestDistance) {
                shortestDistance = distance;
                shortestDirection = exit;
            }
        }
        if (direction != shortestDirection) {
            direction = shortestDirection;
            model.reorient(direction);
        }
    }

    private double distanceToTarget(Point p1) {
        Point p2=null;
        switch (mode) {
            case SCATTER: p2 = scatter_target; break;
            case CHASE: p2 = chase_target; break;
        }
        if (p2==null) return Double.MAX_VALUE;
        double a = Math.abs(p1.x-p2.x);
        double b = Math.abs(p1.y-p2.y);
        return Math.sqrt(a*a+b*b);
    }

    private Maze.Direction reverse() {
        switch(direction) {
            case LEFT: return Maze.Direction.RIGHT;
            case RIGHT: return Maze.Direction.LEFT;
            case UP: return Maze.Direction.DOWN;
            case DOWN: return Maze.Direction.UP;
        }
        return null;
    }

    protected void createGhostModel() {
        model = new ImageModel(home, null, new Point3D(150, 150, 150));
    }

    @Override
    public Model getModel() {
        return model;
    }
}
