import java.awt.*;

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
    public void calculateDistance(Point from) {
        int x = 0, y=0;
        for (Point3D vertex : vertices) {
            x+=vertex.x;
            y+=vertex.y;
        }
        x = x/vertices.length;
        y = y/vertices.length;
        int dx = x-from.x;
        int dy = y-from.y;
        distance = dx*dx+dy*dy;
    }

    @Override
    public void drawAsViewedBy(Camera camera) {

        Graphics screen = camera.image.getGraphics();
        Point[] points = new Point[ vertices.length ];
        int[] xcoords = new int[4];
        int[] ycoords = new int[4];
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
            float SCALE = Camera.NEAR/(z1+Camera.NEAR+Camera.NEARTOOBJ)*Camera.SCALE;
            x1 = x1/SCALE;
            y1 = y1/SCALE;
            // the 0.5 is to round off when converting to int
            xcoords[j] = (int)(camera.getSize().width/2 +  x1/camera.distance  + 0.5);
            ycoords[j] = (int)(camera.getSize().height/2 - y1/camera.distance + 0.5);
            points[j] = new Point(xcoords[j], ycoords[j]);
        }

        // draw 2d image
        screen.setColor(new Color(0, 0, 0, 0.7f));
        screen.fillPolygon(xcoords, ycoords, 4);
        screen.setColor(Color.BLUE);
        for (Edge edge : edges) {
//            System.out.println(points[edges[j].start] + "-" + points[edges[j].end]);
            screen.drawLine(
                    points[edge.start].x, points[edge.start].y,
                    points[edge.end].x, points[edge.end].y
            );
        }
    }

}
