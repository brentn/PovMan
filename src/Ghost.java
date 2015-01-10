import java.awt.*;
import java.util.Set;

/**
 * Created by brent on 09/01/15.
 */
public abstract class Ghost extends Consumable implements IModel {

    public static enum Mode {CHASE, SCATTER, FRIGHTENED}
    private static final int POINTS = 200;

    private Point home;
    protected int speed;
    protected Maze.Direction direction;
    protected boolean alive=false;
    protected Point chase_target;
    protected Point scatter_target;
    protected Mode mode = Mode.SCATTER;
    protected ImageModel model;

    public Ghost(Point home) {
        super(POINTS);
        this.home = home;
        createGhostModel();
    }

    public void setMode(Mode mode) {
        if (this.mode==mode) return;
        this.mode=mode;
        direction = reverse();
    }

    public void reset() {
        model.setPos(home);
        alive=true;
    }

    @Override
    public int consume() {
        alive=false;
        return super.consume();
    }

    public void move(Maze maze) {
        if (model.pastCenterOfTile(direction)) {
            Set<Maze.Direction> exits = maze.getExitsFrom(model.getTile());
            exits.remove(reverse());
            if (exits.size() > 1) {
                updateChaseTarget(maze);
                chooseBestRoute(exits);
            } else { //only 1 exit
                direction=exits.iterator().next();
                model.reorient(direction);
            }
        }
        model.move(speed, direction);
        if (maze.manAt(model.getTile())) {
            if (mode==Mode.FRIGHTENED) {
                maze.killGhost(this.consume());
            } else {
                maze.killMan();
            }
        }
    }

    protected abstract void updateChaseTarget(Maze maze);

    private void chooseBestRoute(Set<Maze.Direction> exits) {
        double shortestDistance = 10000;
        int x = model.getTile().x;
        int y = model.getTile().y;
        for (Maze.Direction exit : exits) {
            Point tile=null;
            switch (exit) {
                case LEFT: tile=new Point(x-1, y);
                case RIGHT: tile=new Point(x+1, y);
                case UP: tile=new Point(x, y-1);
                case DOWN: tile=new Point(x, y+1);
            }
            double distance = distanceToTarget(tile);
            if (distance < shortestDistance) {
                shortestDistance = distance;
                direction = exit;
            }
        }
    }

    private double distanceToTarget(Point p1) {
        Point p2=null;
        switch (mode) {
            case SCATTER: p2 = scatter_target;
            case CHASE: p2 = chase_target;
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

    private void createGhostModel() {
        model = new ImageModel(home, null); //TODO:
    }

    @Override
    public Model getModel() {
        return model;
    }
}
