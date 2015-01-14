import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Created by brent on 09/01/15.
 */
public class ImageModel extends Model {
    private Point tile;
    private Point offset;
    private Point3D size;
    private Image image;

    public ImageModel(Point tile, Image image) {
        this.tile = tile;
        this.offset = new Point(50, 50); //percentage of tile
        this.image = image;
        this.size = new Point3D(100,100,100); //percentage of tile
    }

    public ImageModel(Point tile, Image image, Point3D size) {
        this.tile = tile;
        this.offset = new Point(50, 50); //percentage of tile
        this.image = image;
        this.size = size; //percentage of tile
    }

    public Point getTile() {
        return tile;
    }
    public void setTile(Point tile) {
        this.tile = tile;
    }

    public Point getPos() {
        int x = tile.x*100+offset.x;
        int y = tile.y*100+offset.y;
        return new Point(x, y);
    }

    public Point getOffset() {return offset;}
    public void setOffset(int x, int y) {
        this.offset = new Point(x, y);
    }

    public void move(int speed, Maze.Direction direction) {
        int x = offset.x;
        int y = offset.y;
        switch (direction) {
            case RIGHT: x += speed;
                if (x >=100) {
                    tile.setLocation(tile.x+1, tile.y);
                    x-=100;
                }
                break;
            case LEFT:  x -= speed;
                if (x <=0) {
                    tile.setLocation(tile.x-1, tile.y);
                    x +=100;
                }
                break;
            case UP: y -= speed;
                if (y <=0) {
                    tile.setLocation(tile.x, tile.y-1);
                    y +=100;
                }
                break;
            case DOWN: y += speed;
                if (y >=100) {
                    tile.setLocation(tile.x, tile.y+1);
                    y -=100;
                }
                break;
        }
        offset.setLocation(x, y);
    }

    public boolean pastCenterOfTile(Maze.Direction direction) {
        switch (direction) {
            case LEFT: return offset.x < 50;
            case RIGHT: return offset.x > 50;
            case UP: return offset.y < 50;
            case DOWN: return offset.y > 50;
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
        Graphics screen = camera.image.getGraphics();
        int x = tile.x*100 + offset.x - camera.target.x;
        int y = tile.y*100 + offset.y - camera.target.y;
        int z = camera.target.z*100 - size.z;
        // compute orthographic projection
        float x1 = camera.cosT*x + camera.sinT*y;
        float y1 = -camera.sinTsinP*x + camera.cosP*z + camera.cosTsinP*y;
        int width = (int)(size.x/camera.distance+.5);

        // the 0.5 is to round off when converting to int
        Point point = new Point(
                (int)(camera.getSize().width/2 +  x1/camera.distance  + 0.5),
                (int)(camera.getSize().height/2 - y1/camera.distance + 0.5)
        );
        // draw 2d image
        if (image==null) {
            if (size.x>100) {
                screen.setColor(Color.yellow);
            } else {
                screen.setColor(Color.white);
            }
            screen.fillOval(point.x-(width/2), point.y - (width/2),width ,width);
        }
    }
}
