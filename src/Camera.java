import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Comparator;
import java.util.HashSet;
import java.util.PriorityQueue;
import java.util.Set;

/**
 * Created by brent on 08/01/15.
 */
public class Camera extends JFrame {

    public static enum Style {CLASSIC, FOLLOW}

    public Point3D target;
    protected Style style;
    protected float distance;
    private double theta, phi;
    public float sinT, sinP, cosT, cosP;
    public float sinTcosP, cosTcosP, cosTsinP, sinTsinP;
    private Draw viewport;
    public BufferedImage image = null;

    public Camera(Point3D target, Style style) {
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(400, 500);
        this.setVisible(true);
        viewport = new Draw();
        this.add(viewport);
        this.target = target;
        this.style=style;
        switch (style) {
            case CLASSIC:setClassicOrientation();
                break;
            case FOLLOW:setFollowOrientation();
                break;
        }
    }

    private void setClassicOrientation() {
        phi=-Math.PI/2;
        theta=0;
        distance=10;
        calculateProjectionCoefficinets();
    }

    private void setFollowOrientation() {
        theta=0;
        phi= -.3;
        distance=4;
        calculateProjectionCoefficinets();
    }

    public void follow(Man man) {
        target = new Point3D(man.getPos(), 1);
        double trail = 0;
        switch (man.getDirection()) {
            case LEFT:  trail = (3*Math.PI/2);
                break;
            case RIGHT: trail = (Math.PI/2);
                break;
            case UP: trail = 0;
                break;
            case DOWN: trail = (Math.PI);
                break;
        }
        double amount = Math.abs(theta-trail);
        if (amount >= Math.PI) amount-=Math.PI;
        amount = amount/200;
        if (theta>trail) {
            if ((theta-trail)<Math.PI) {
                theta -= amount;
            } else {
                theta += amount;
            }
        } else {
            if ((trail-theta)<Math.PI) {
                theta += amount;
            } else {
                theta -= amount;
            }
        }
        while (theta<0) {theta+=2*Math.PI;}
        while (theta>(2*Math.PI)) {theta-=2*Math.PI;}
        calculateProjectionCoefficinets();
    }

    public void capture(Maze maze) {
        Point pos = calculate2DCameraPosition();

        // sort all objects from far to near
        Set<IModel> objects = new HashSet<IModel>();
        DistanceComparator comparator = new DistanceComparator();
        PriorityQueue<Model> models = new PriorityQueue<Model>(100, comparator);
        objects.addAll(maze.getWalls());
        objects.addAll(maze.getDots());
        objects.addAll(maze.getGhosts());
        objects.add(maze.getMan());
        for (IModel object : objects) {
            object.getModel().calculateDistance(pos);
            models.add(object.getModel());
        }

        //TODO:eliminate models outside field of view
        this.image = new BufferedImage(this.getWidth(), this.getHeight(), BufferedImage.TYPE_INT_ARGB);
        this.image.createGraphics();
        Graphics screen = image.getGraphics();
        viewport.paintComponent(screen);
        screen.setColor(Color.black);
        screen.fillRect(0, 0, this.getWidth(), this.getHeight());
        for (Model item : models) {
            item.drawAsViewedBy(this);
        }
        viewport.updateImage(image);
        viewport.repaint(0,0,400,500);
    }

    private Point calculate2DCameraPosition() {
        int x = target.x+(int)(distance*10*sinTsinP);
        int y = target.y-(int)(distance*10*cosTsinP);
        return new Point(x,y);
    }

    private void calculateCameraAngle(Point3D pos) {
        double x = target.x-pos.x*100;//TODO:need to include offset
        double y = target.y-pos.y*100;
        double z = target.z-pos.z*100;
        double r = Math.sqrt(x*x + y*y + z*z);
        theta = (x==0?0:Math.atan(y/x));
        phi = (r==0?0:(Math.PI/2)-Math.acos(z/r));
        theta = 0;
        phi = -.3;
    }

    private void calculateProjectionCoefficinets() {
        cosT = (float)Math.cos( theta );
        sinT = (float)Math.sin( theta );
        cosP = (float)Math.cos( phi );
        sinP = (float)Math.sin( phi );
        cosTcosP = cosT*cosP;
        cosTsinP = cosT*sinP;
        sinTcosP = sinT*cosP;
        sinTsinP = sinT*sinP;
    }

    class Draw extends JPanel {
        private BufferedImage buffer = null;

        public void updateImage(BufferedImage image) {
            this.buffer = image;
        }

        public void paintComponent(Graphics g){
            super.paintComponent(g);
            if (buffer!=null) {
                g.drawImage(buffer, 0, 0, this);
            }
        }
    }

    class DistanceComparator implements Comparator<Model> {

        @Override
        public int compare(Model model, Model model2) {
            if (model.getDistance() < model2.getDistance()) return -1;
            if (model.getDistance() > model2.getDistance()) return 1;
            return 0;
        }
    }
}
