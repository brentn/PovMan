import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by brent on 09/01/15.
 */
public class WireframeModel  extends Model {
    private Point3D[] vertices;
    private Edge[] edges;

    public WireframeModel() {
        vertices = new Point3D[0];
        edges = new Edge[0];
    }
    public WireframeModel(Point3D[] vertices, Edge[] edges) {
        this.vertices = vertices;
        this.edges = edges;
    }
    public void addVertex(Point3D vertex) {
        Point3D[] temp = vertices.clone();
        vertices = new Point3D[temp.length + 1];
        System.arraycopy(temp, 0, vertices, 0, temp.length);
        vertices[temp.length] = vertex;
    }
    public void addEdge(Edge edge) {
        Edge[] temp = edges.clone();
        edges = new Edge[temp.length + 1];
        System.arraycopy(temp, 0, edges, 0, temp.length);
        edges[temp.length] = edge;
    }

    @Override
    public boolean isVisibleFrom(Camera camera) {
        //TODO:
        return true;
    }

    @Override
    public void drawAsViewedBy(Camera camera) {
        Graphics screen = camera.image.getGraphics();
        float scaleFactor=50f;
        float distToWall = getDistanceToCenterOfWall(camera.pos);
        Point[] points = new Point[ vertices.length ];
        for (int j=0; j<vertices.length; j++) {
            int x0 = vertices[j].x;
            int y0 = vertices[j].y;
            int z0 = vertices[j].z;
            // compute orthographic projection
            float x1 = camera.cosT*x0 + camera.sinT*z0;
            float y1 = -camera.sinTsinP*x0 + camera.cosP*y0 + camera.cosTsinP*z0;
            // calculate perspective
            float z1 = camera.cosTcosP*z0 - camera.sinTcosP*x0 - camera.sinP*y0;
            x1 = x1*camera.DIST_TO_VIEWPORT/(z1+distToWall);// - camera.target.x;
            y1 = y1*camera.DIST_TO_VIEWPORT/(z1+distToWall);// - camera.target.y;

            // the 0.5 is to round off when converting to int
            points[j] = new Point(
                    (int)(camera.getSize().width/2 + scaleFactor*x1 + 0.5),
                    (int)(camera.getSize().height/2 - scaleFactor*y1 + 0.5)
            );
        }

        // draw 2d image
        screen.setColor(Color.BLUE);
        for (int  j = 0; j < edges.length; ++j ) {
            screen.drawLine(
                    points[edges[j].start].x, points[edges[j].start].y,
                    points[edges[j].end].x, points[edges[j].end].y
            );
        }
    }

    private float getDistanceToCenterOfWall(Point3D origin) {
        int x=0, y=0, z=0; //center of object
        int dx, dy, dz; //distance between object and camera
        int n=vertices.length;
        for (Point3D point : vertices) {
            x+=point.x;
            y+=point.y;
            z+=point.z;
        }
        Point3D center = new Point3D(x/n, y/n, z/n);
        dx = origin.x-center.x;
        dy = origin.y-center.y;
        dz = origin.z-center.z;
        return (float)Math.sqrt(dx*dx + dy*dy + dz*dz);
    }
}
