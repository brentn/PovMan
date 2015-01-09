import java.awt.*;

/**
 * Created by brent on 08/01/15.
 */
public class Wall implements IModel {
    private static int WALL_HEIGHT=1;

    private Point start;
    private Point end;
    private WireframeModel model;

    public Wall(Point start, Point end) {
        this.start = start;
        this.end = end;
        createWallModel();
    }
    public Wall(int startx, int starty, int endx, int endy) {
        this.start = new Point(startx, starty);
        this.end = new Point(endx, endy);
        createWallModel();
    }

    private void createWallModel(){
        model = new WireframeModel();
        model.addVertex(new Position_3D(start, 0));
        model.addVertex(new Position_3D(end, 0));
        model.addVertex(new Position_3D(end, WALL_HEIGHT));
        model.addVertex(new Position_3D(start, WALL_HEIGHT));
        model.addEdge(new Edge(0, 1));
        model.addEdge(new Edge(1, 2));
        model.addEdge(new Edge(2, 3));
        model.addEdge(new Edge(3, 0));
    }

    @Override
    public Model getModel() {
        return model;
    }
}
