import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.File;
import java.util.Random;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;


public class Man implements IModel {

    private static final int INITIAL_LIVES=3;
    private static final int SPEED=85;
    private static final String MAN_IMAGE = "resources/images/man.png";
    private static final File CHOMP_SOUND_FILE = new File("resources/sounds/dot.wav");
    private static final File DIE_SOUND_FILE = new File("resources/sounds/die.wav");

    private boolean alive=false;
    private boolean empowered=false;
    private boolean undecided;
    private long points=0;
    private int doubler=1;
    private int lives;
    private ImageModel model;
    private Point initial_tile;
    private Maze.Direction initial_direction;
    private Sound chomp_sound = new Sound(CHOMP_SOUND_FILE);
    private Sound die_sound = new Sound(DIE_SOUND_FILE);

    private Maze.Direction direction;

    public Man(Point start, Maze.Direction dir) {
        this.lives=INITIAL_LIVES;
        this.initial_tile=start;
        this.initial_direction = dir;
        this.direction=dir;
        createManModel();
    }

    public void addPoints(int points) {
        this.points += points;
    }
    public void addDoubledPoints(int points) {this.points += points*doubler;}
    public void increaseDoubler() {if (doubler<4) doubler = doubler*2;}
    public long getPoints() {return points;}
    public boolean isEmpowered() {return empowered;}

    public void setStartPosition(Point pos, Maze.Direction dir) {
        this.initial_tile = pos;
        this.initial_direction = dir;
    }

    public void control(Maze maze, Set<Integer> pressed_keys) {
        if ( alive && !maze.isPaused()) {
            Set<Maze.Direction> exits = maze.getExitsFrom(model.getTile());
            if (pressed_keys.contains(KeyEvent.VK_UP) && exits.contains(Maze.Direction.UP))
                direction = Maze.Direction.UP;
            else if (pressed_keys.contains(KeyEvent.VK_RIGHT) && exits.contains(Maze.Direction.RIGHT))
                direction = Maze.Direction.RIGHT;
            else if (pressed_keys.contains(KeyEvent.VK_DOWN) && exits.contains(Maze.Direction.DOWN))
                direction = Maze.Direction.DOWN;
            else if (pressed_keys.contains(KeyEvent.VK_LEFT) && exits.contains(Maze.Direction.LEFT))
                direction = Maze.Direction.LEFT;
            else if (!exits.contains(direction))
                direction = Maze.Direction.NONE;
            if (direction != Maze.Direction.NONE) {
                model.reorient(direction);
                model.move(SPEED, direction);
                Dot dot = maze.dotAt(getTile());
                if (dot != null) {
                    eat(dot);
                    maze.removeDot(dot);
                }
            }
        }
    }

    public void update(Maze maze) {
        if (! maze.isPaused()) {
            if (model.pastCenterOfTile(direction)) {
                if (undecided) {
                    Set<Maze.Direction> exits = maze.getExitsFrom(model.getTile());
                    boolean reversable = (new Random().nextInt(10) > 7); //chance of reversing direction
                    if (!reversable) exits.remove(reverse());
                    if (exits.size() > 1) {
                        Maze.Direction[] exitarray = new Maze.Direction[exits.size()];
                        exitarray = exits.toArray(exitarray);
                        int choice = new Random().nextInt(exits.size());
                        if (choice < exits.size())
                            direction = exitarray[choice];
                    } else {
                        if (!exits.contains(direction))
                            direction = exits.iterator().next();
                    }
                    model.reorient(direction);
                    undecided = false;
                }
            } else {
                undecided = true;
            }
            model.move(SPEED, direction);
            Dot dot = maze.dotAt(getTile());
            if (dot != null) {
                eat(dot);
                maze.removeDot(dot);
            }
        }
    }

    public Point getPos() {
        return model.getPos();
    }
    public Point getTile() { return model.getTile(); }

    public Point getTileAhead(int distance) {
        Point tile = model.getTile();
        if (distance==0) return tile;
        switch (direction) {
            case LEFT: return new Point(tile.x-distance, tile.y);
            case RIGHT: return new Point(tile.x+distance, tile.y);
            case UP: return new Point(tile.x, tile.y-distance);
            case DOWN: return new Point(tile.x, tile.y+distance);
            case NONE: return new Point(tile.x, tile.y);
        }
        return null;
    }

    public Maze.Direction getDirection() { return direction; }

    private Maze.Direction reverse() {
        switch(direction) {
            case LEFT: return Maze.Direction.RIGHT;
            case RIGHT: return Maze.Direction.LEFT;
            case UP: return Maze.Direction.DOWN;
            case DOWN: return Maze.Direction.UP;
        }
        return null;
    }

    public void eat(Consumable item) {
        if (item==null) return;
        points += item.consume();
        if (item instanceof PowerPill) {
            empowered=true;
            doubler=1;
            Timer timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    empowered=false;
                }
            }, ((PowerPill)item).getDuration()*1000);
        } else {
            chomp_sound.loop(2);
        }
    }

    public void die() {
        alive=false;
        die_sound.play();
    }

    public boolean hasMoreLives() {return lives>0;}
    public int lives() {return lives;}

    public void recessutate() {
        if (! alive) {
            if (lives > 0) {
                model.setTile(initial_tile);
                model.setOffset(0,50);
                direction = initial_direction;
                undecided=false;
                lives--;
                alive=true;
            }
        }
    }

    public boolean isAlive() {
        return alive;
    }

    private void createManModel() {
        Image image = Toolkit.getDefaultToolkit().getImage(MAN_IMAGE);
        model = new ImageModel(initial_tile, image, new Point3D(150,150,150));
    }

    @Override
    public Model getModel() {
        return model;
    }
}
