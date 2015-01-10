import java.awt.*;
import java.util.Random;

/**
 * Created by brent on 08/01/15.
 */

public class Man {

    private static final int INITIAL_LIVES=3;
    private static final int SPEED=5;

    private boolean alive=false;
    private long points=0;
    private int lives;
    private Point initial_tile;
    private Maze.Direction initial_direction;

    private Point tile;
    private Point offset; //by % within the tile
    private Maze.Direction direction;

    public Man(Point start, Maze.Direction dir) {
        this.lives=INITIAL_LIVES;
        this.initial_tile=start;
        this.initial_direction = dir;
    }

    public void addPoints(int points) {
        this.points += points;
    }

    public void move(Maze maze) {
        if (canMoveForward(maze)) {
            move();
            eat(maze.dotAt(tile));
        } else {
            offset.setLocation(50, 50); //reorient to the tile center
            direction = Maze.Direction.values()[new Random().nextInt(4)]; //pick a random direction
        }
    }

    public Point getTileAhead(int distance) {
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

    private void move() {
        int x = offset.x;
        int y = offset.y;
        switch (direction) {
            case RIGHT: x += SPEED;
                if (x >=100) {
                    tile.setLocation(tile.x+1, tile.y);
                    x-=100;
                }
                break;
            case LEFT:  x -= SPEED;
                if (x <=0) {
                    tile.setLocation(tile.x-1, tile.y);
                    x +=100;
                }
                break;
            case UP: y -= SPEED;
                if (y <=0) {
                    tile.setLocation(tile.x, tile.y-1);
                    y +=100;
                }
                break;
            case DOWN: y += SPEED;
                if (y >=100) {
                    tile.setLocation(tile.x, tile.y+1);
                    y -=100;
                }
                break;
        }
        offset.setLocation(x, y);
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
            tile = initial_tile;
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


}
