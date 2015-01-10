import sun.net.www.content.audio.wav;
import sun.net.www.content.image.gif;

import java.awt.*;
import java.util.Arrays;
import java.util.Collections;
import java.util.Random;

/**
 * Created by brent on 08/01/15.
 */

public class Man {
    public enum Direction {RIGHT, DOWN, LEFT, UP};

    private static final int INITIAL_LIVES=3;
    private static final int SPEED=5;
    private static wav DIE_SOUND;
    private static gif DIE_ANIMATION;

    private boolean alive=false;
    private long points=0;
    private int lives;
    private Point initial_tile;
    private Direction initial_direction;

    private Point tile;
    private Point position; //by % within the tile
    private Direction direction;

    public Man(Point start, Direction dir) {
        this.lives=INITIAL_LIVES;
        this.initial_tile=start;
        this.initial_direction = dir;
    }

    public void move(Maze maze) {
        if (canMoveForward(maze)) {
            move();
            eat(maze.dotAt(tile));
        } else {
            position.setLocation(50, 50); //reorient to the tile center
            direction = Direction.values()[new Random().nextInt(4)]; //pick a random direction
        }
    }

    private boolean canMoveForward(Maze maze) {
        switch (direction) {
            case RIGHT:
                if (position.x+SPEED < 50) return true;
                if (maze.wallAt(new Point(tile.x+1, tile.y))) return false;
                break;
            case LEFT:
                if (position.x-SPEED > 50) return true;
                if (maze.wallAt(new Point(tile.x-1, tile.y))) return false;
                break;
            case UP:
                if (position.y-SPEED > 50) return true;
                if (maze.wallAt(new Point(tile.x, tile.y-1))) return false;
                break;
            case DOWN:
                if (position.y+SPEED < 50) return true;
                if (maze.wallAt(new Point(tile.x, tile.y+1))) return false;
                break;
        }
        return true;
    }

    private void move() {
        int x = position.x;
        int y = position.y;
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
        position.setLocation(x, y);
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
            position = new Point(50,50);
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
