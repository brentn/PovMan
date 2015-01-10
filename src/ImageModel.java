import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Created by brent on 09/01/15.
 */
public class ImageModel extends Model {
    private Point tile;
    private Point offset;
    private Image image;

    public ImageModel(Point tile, Image image) {
        this.tile = tile;
        this.offset = new Point(0, 0);
        this.image = image;
    }

    public Point getTile() {
        return tile;
    }

    public void setPos(Point tile) {
        this.tile = tile;
        this.offset = new Point(0, 0);
    }

    public void move(int speed, Maze.Direction direction) {
        int x = tile.x;
        int y = tile.y;
        switch (direction) {
            case RIGHT: x += speed;
                if (x >=100) {
                    tile.setLocation(tile.x+1, tile.y);
                    x-=100;
                }
                break;
            case LEFT: x -= speed;
                if (x <=0) {
                    tile.setLocation(tile.x-1, tile.y);
                    x+=100;
                }
                break;
            case UP: y -= speed;
                if (y <= 0) {
                    tile.setLocation(tile.x, tile.y-1);
                    y+=100;
                }
                break;
            case DOWN: y += speed;
                if (y >=100) {
                    tile.setLocation(tile.x, tile.y+1);
                    y-=100;
                }
                break;
        }
        offset.setLocation(x, y);
    }

    public boolean pastCenterOfTile(Maze.Direction direction) {
        switch (direction) {
            case LEFT: return offset.x >= 50;
            case RIGHT: return offset.x <= 50;
            case UP: return offset.y <=50;
            case DOWN: return offset.y >=50;
        }
        return false;
    }

    public void reorient(Maze.Direction direction) {
        if (direction == Maze.Direction.LEFT || direction == Maze.Direction.RIGHT) {
            offset.setLocation(offset.x, 50);
        } else {
            offset.setLocation(50, offset.y);
        }
    }

    @Override
    public boolean isVisibleFrom(Camera camera) {
        //TODO:
        return true;
    }

    @Override
    public void drawAsViewedBy(Camera camera) {
        //TODO:
    }
}
