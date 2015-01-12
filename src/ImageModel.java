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

    public Point getPos() {
        return tile;
    }

    public void setPos(Point tile) {
        this.tile = tile;
        this.offset = new Point(0, 0);
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
        Graphics screen = camera.image.getGraphics();
        float cameraDistance=10;
        int x = tile.x*100 - camera.target.x*100  + offset.x;
        int y = tile.y*100 - camera.target.y*100 + offset.y;
        int z = camera.target.z*100 + size.z;
        // compute orthographic projection
        float x1 = camera.cosT*x + camera.sinT*y;
        float y1 = -camera.sinTsinP*x + camera.cosP*z + camera.cosTsinP*y;

        // the 0.5 is to round off when converting to int
        Point point = new Point(
                (int)(x1/cameraDistance + 0.5),
                (int)(y1/cameraDistance + 0.5)
        );
        // draw 2d image
        if (image==null) {
        screen.setColor(Color.white);
        screen.fillOval(camera.getSize().width/2 + point.x,
                camera.getSize().height/2 - point.y,
                (int)(size.x/cameraDistance),
                (int)(size.y/cameraDistance));
        }
    }
}
