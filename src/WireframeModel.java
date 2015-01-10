import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by brent on 09/01/15.
 */
public class WireframeModel  extends Model {
    private List<Position_3D> vertices;
    private List<Edge> edges;
    public WireframeModel() {
        vertices = new ArrayList<Position_3D>();
        edges = new ArrayList<Edge>();
    }
    public WireframeModel(List<Position_3D> vertices, List<Edge> edges) {
        this.vertices = vertices;
        this.edges = edges;
    }
    public void addVertex(Position_3D vertex) {
        vertices.add(vertex);
    }
    public void addEdge(Edge edge) {
        edges.add(edge);
    }

    @Override
    public boolean isVisibleFrom(Camera camera) {
        //TODO:
        return true;
    }

    @Override
    public Canvas drawAsViewedBy(Camera camera) {
        //TODO:
        Canvas canvas = new Canvas();
        return canvas;
    }
}
