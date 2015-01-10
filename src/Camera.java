import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Created by brent on 08/01/15.
 */
public class Camera extends JFrame {
    public static final float DIST_TO_VIEWPORT=3;

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
        calculateCameraAngle(pos, target);
        calculateProjectionCoefficinets();
    }

    public void capture(Maze maze) {
        //TODO:sort all objects from far to near
        //TODO:eliminate models outside field of view
        image = new BufferedImage(400, 500, BufferedImage.TYPE_INT_ARGB);
        image.createGraphics();
        image.getGraphics().setColor(Color.BLACK);
        image.getGraphics().fillRect(0,0,400,500);
        viewport.updateImage(image);
        viewport.paintComponent(image.getGraphics());
        //Draw walls
        for (Wall wall : maze.getWalls()) {
            wall.getModel().drawAsViewedBy(this);
        }
        viewport.updateImage(image);
        //Draw dots
        //Draw ghosts
        //Draw man
    }

    private void calculateCameraAngle(Point3D pos, Point3D target) {
        double x = target.x-pos.x;
        double y = target.y-pos.y;
        double z = target.z-pos.z;
        double r = Math.sqrt(x*x + y*y + z*z);
        theta = (x==0?0:Math.atan(y/x));
        phi = (r==0?180:Math.acos(z/r));
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
