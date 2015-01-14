import java.awt.*;

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
        Point[] points = new Point[ vertices.length ];
        for (int j=0; j<vertices.length; j++) {
            // the +50 is to center the wall in the tile
            int x0 = vertices[j].x*100 +50 - camera.target.x;
            int y0 = vertices[j].y*100 +50 - camera.target.y;
            int z0 = vertices[j].z*100 + camera.target.z;
            // compute orthographic projection
            float x1 = camera.cosT*x0 + camera.sinT*y0;
            float y1 = -camera.sinTsinP*x0 + camera.cosP*z0 + camera.cosTsinP*y0;
            float z1 = camera.cosTcosP*y0 - camera.sinTcosP*x0 - camera.sinP*z0;
            // calculate perspective
            //x1 = x1*3f/(z1+3f+1.5f);
            //y1 = y1*3f/(z1+3f+1.5f);

            // the 0.5 is to round off when converting to int
            points[j] = new Point(
                    (int)(camera.getSize().width/2 +  x1/camera.distance  + 0.5),
                    (int)(camera.getSize().height/2 - y1/camera.distance + 0.5)
            );
        }

        // draw 2d image
        screen.setColor(Color.BLUE);
        for (int  j = 0; j < edges.length; ++j ) {
//            System.out.println(points[edges[j].start] + "-" + points[edges[j].end]);
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
        dx = 1*(origin.x-center.x);
        dy = 1*(origin.y-center.y);
        dz = 1*(origin.z-center.z);
        return (float)Math.sqrt(dx*dx + dy*dy + dz*dz);
    }
}
