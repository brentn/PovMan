import java.awt.*;
import java.util.Random;

/**
 * Created by brent on 08/01/15.
 */

public class Man implements IModel {

    private static final int INITIAL_LIVES=3;
    private static final int SPEED=5;

    private boolean alive=false;
    private long points=0;
    private int lives;
    private ImageModel model;
    private Point initial_tile;
    private Maze.Direction initial_direction;

    private Point offset; //by % within the tile
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
        if (canMoveForward(maze)) {
            model.move(SPEED, direction);
            eat(maze.dotAt(model.getPos()));
        } else {
            offset.setLocation(50, 50); //reorient to the tile center
            direction = Maze.Direction.values()[new Random().nextInt(4)]; //pick a random direction
        }
    }

    public Point getTileAhead(int distance) {
        Point tile = model.getPos();
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
        Point tile = model.getPos();
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
            model.setPos(initial_tile);
            offset = new Point(50,50);
            direction = initial_direction;
            if (lives > 0) {
                lives --;
                alive = true;
            }
            //TODO: insert pause
        }
    }

    public boolean isAlive() {
        return alive;
    }

    private void createManModel() {
        model = new ImageModel(initial_tile, null, new Point3D(120,120,120));
    }

    @Override
    public Model getModel() {
        return model;
    }
}
