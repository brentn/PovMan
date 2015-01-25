import java.awt.*;

public class ImageModel extends Model {
    private Point tile;
    private Point offset;
    private long last_tick;
    private Point3D size;
    private Image image;

    public ImageModel(Point tile, Image image) {
        this.tile = tile;
        this.offset = new Point(50, 50); //percentage of tile
        this.image = image;
        this.size = new Point3D(100,100,100); //percentage of tile
        this.last_tick=0;
    }

    public ImageModel(Point tile, Image image, Point3D size) {
        this.tile = tile;
        this.offset = new Point(50, 50); //percentage of tile
        this.image = image;
        this.size = size; //percentage of tile
        this.last_tick=0;
    }

    public Point getTile() {
        return tile;
    }
    public void setTile(Point tile) {
        this.tile = tile;
    }
    public void swapImage(Image image) {
        this.image = image;
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
        if (last_tick==0) {
            last_tick=System.currentTimeMillis();
            return;
        }
        long elapsed_time = System.currentTimeMillis()-last_tick;
        last_tick=System.currentTimeMillis();
        int distance = Math.round(elapsed_time*speed / 200f);
        if (distance>50) distance=50;
        int x = offset.x;
        int y = offset.y;
        switch (direction) {
            case RIGHT: x += distance;
                if (x >=100) {
                    tile = new Point(tile.x+1, tile.y);
                    x-=100;
                }
                break;
            case LEFT:  x -= distance;
                if (x <=0) {
                    tile = new Point(tile.x-1, tile.y);
                    x +=100;
                }
                break;
            case UP: y -= distance;
                if (y <=0) {
                    tile = new Point(tile.x, tile.y-1);
                    y +=100;
                }
                break;
            case DOWN: y += distance;
                if (y >=100) {
                    tile = new Point(tile.x, tile.y+1);
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
    public void calculateDistance(Point from) {
        int dx = getPos().x-from.x;
        int dy = getPos().y-from.y;
        distance = dx*dx+dy*dy;
    }

    @Override
    public void drawAsViewedBy(Camera camera) {
        int ELEVATION=50; //percent
        if (image != null) ELEVATION = size.z/2;
        Graphics screen = camera.image.getGraphics();
        int x = tile.x*100 + offset.x - camera.target.x;
        int y = tile.y*100 + offset.y - camera.target.y;
        int z = ELEVATION - size.z/2 + camera.target.z;
        // compute orthographic projection
        float x1 = camera.cosT*x + camera.sinT*y;
        float y1 = -camera.sinTsinP*x + camera.cosP*z + camera.cosTsinP*y;
        float z1 = camera.cosTcosP*y - camera.sinTcosP*x - camera.sinP*z;
        int width = (int)(size.x/camera.distance);
        float SCALE = Camera.NEAR/(z1+Camera.NEAR+Camera.NEARTOOBJ)*Camera.SCALE;
        x1 = x1/SCALE;
        y1 = y1/SCALE;

        // the 0.5 is to round off when converting to int
        Point point = new Point(
                (int)(camera.getSize().width/2 +  x1/camera.distance  + 0.5),
                (int)(camera.getSize().height/2 - y1/camera.distance + 0.5)
        );
        // draw 2d image
        if (image==null) {
            screen.setColor(Color.white);
            int size = Math.round(width/SCALE);
            if (size < 2 ) size=2;
            screen.fillOval(point.x-Math.round((width / 2) / SCALE), point.y - Math.round((width / 2) / SCALE),
                    size, size);
        } else {
            screen.drawImage(image, point.x-Math.round((width/2)/SCALE), point.y - Math.round((width/2)/SCALE),
                    Math.round(width/SCALE) ,Math.round(width/SCALE), null);
        }
    }

}
