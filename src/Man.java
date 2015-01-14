import java.awt.*;
import java.util.Random;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by brent on 08/01/15.
 */

public class Man implements IModel {

    private static final int INITIAL_LIVES=3;
    private static final int SPEED=1;
    private static final String MAN_IMAGE = "resources/images/man.png";

    private boolean alive=false;
    private boolean undecided;
    private long points=0;
    private int lives;
    private ImageModel model;
    private Point initial_tile;
    private Maze.Direction initial_direction;

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

    public void setStartPosition(Point pos, Maze.Direction dir) {
        this.initial_tile = pos;
        this.initial_direction = dir;
    }

    public void move(Maze maze) {
        if (model.pastCenterOfTile(direction)) {
            if (undecided) {
                Set<Maze.Direction> exits = maze.getExitsFrom(model.getTile());
                if (exits.size() > 1) {
                    Maze.Direction[] exitarray = new Maze.Direction[exits.size()];
                    exitarray = exits.toArray(exitarray);
                    int choice = new Random().nextInt(exits.size());
                    direction = exitarray[choice];
                } else {
                    if (! exits.contains(direction))
                        direction = exits.iterator().next();
                }
                model.reorient(direction);
                undecided = false;
            }
        } else {
            undecided=true;
        }
        model.move(SPEED, direction);
        eat(maze.dotAt(getTile()));
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
        }
        return null;
    }

    public Maze.Direction getDirection() { return direction; }

    private boolean canMoveForward(Maze maze) {
        Point tile = model.getTile();
        Point offset = model.getOffset();
        switch (direction) {
            case RIGHT:
                if (offset.x+SPEED < 50) return true;
                if (maze.wallAt(new Point(tile.x+1, tile.y))) return false;
                break;
            case LEFT:
                if (offset.x-SPEED > 50) return true;
                if (maze.wallAt(new Point(tile.x-1, tile.y))) return false;
                break;
            case UP:
                if (offset.y-SPEED > 50) return true;
                if (maze.wallAt(new Point(tile.x, tile.y-1))) return false;
                break;
            case DOWN:
                if (offset.y+SPEED < 50) return true;
                if (maze.wallAt(new Point(tile.x, tile.y+1))) return false;
                break;
        }
        return true;
    }

    public void eat(Consumable item) {
        if (item==null) return;
        points += item.consume();
    }

    public void die() {
        alive=false;
    }

    public void recessutate() {
        if (! alive) {
            model.setTile(initial_tile);
            model.setOffset(50,50);
            direction = initial_direction;
            if (lives > 0) {
                lives --;
                alive = true;
            }
            undecided=false;
        }
    }

    public boolean isAlive() {
        return alive;
    }

    private void createManModel() {
        Image image = Toolkit.getDefaultToolkit().getImage(MAN_IMAGE);
        model = new ImageModel(initial_tile, image, new Point3D(120,120,120));
    }

    @Override
    public Model getModel() {
        return model;
    }
}
