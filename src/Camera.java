import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Created by brent on 08/01/15.
 */
public class Camera extends JFrame {
    public static final float DISTANCE=5;

    public Point3D pos, target;
    private double theta, phi;
    public float sinT, sinP, cosT, cosP;
    public float sinTcosP, cosTcosP, cosTsinP, sinTsinP;
    private Draw viewport;
    public BufferedImage image = null;

    public Camera(Point3D pos, Point3D target) {
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(400, 500);
        this.setVisible(true);
        viewport = new Draw();
        this.add(viewport);
        this.pos = pos;
        this.target = target;
        calculateCameraAngle(pos);
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
        amount = amount/20;
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
        //TODO:sort all objects from far to near
        //TODO:eliminate models outside field of view
        this.image = new BufferedImage(this.getWidth(), this.getHeight(), BufferedImage.TYPE_INT_ARGB);
        this.image.createGraphics();
        Graphics screen = image.getGraphics();
        viewport.paintComponent(screen);
        screen.setColor(Color.black);
        screen.fillRect(0, 0, this.getWidth(), this.getHeight());
        //Draw walls
        for (Wall wall : maze.getWalls()) {
            wall.getModel().drawAsViewedBy(this);
        }
        //Draw dots
        for (Dot dot : maze.getDots()) {
            if (! dot.hasBeenConsumed()) {
                dot.getModel().drawAsViewedBy(this);
            }
        }
        viewport.updateImage(image);
        viewport.repaint(0,0,400,500);
        //Draw ghosts
        for (Ghost ghost : maze.getGhosts()) {
            if (ghost.isAlive()) {
                ghost.getModel().drawAsViewedBy(this);
            }
        }
        //Draw man
        Man man = maze.getMan();
        if (man.isAlive()) {
            man.getModel().drawAsViewedBy(this);
        }
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
}
