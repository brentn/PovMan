import javax.sound.sampled.*;
import java.awt.*;
import java.io.File;
import java.util.Collection;
import java.util.HashSet;


public class Dot extends Consumable implements IModel {
    private static final File CHOMP_SOUND = new File("resources/sounds/dot.wav");
    private static final int DEFAULT_POINTS=100;
    private static final int HEIGHT = 0;

    private static boolean initialized=false;
    private static boolean play_audio=true;
    private static Clip chomp_sound;

    private Point pos;
    private ImageModel model;

    public Dot(Point pos) {
        super(DEFAULT_POINTS);
        this.pos = pos;
        createDotModel();
        if (! initialized) initializeAudio();
    }
    public Dot(int x, int y) {
        super(DEFAULT_POINTS);
        this.pos = new Point(x, y);
        createDotModel();
        if (! initialized) initializeAudio();
    }

    public Point getPos() { return pos; }

    private void createDotModel() {
        Point3D pos3d = new Point3D(pos, HEIGHT);
        model = new ImageModel(pos3d, null, new Point3D(25,25,25));
    }

    @Override
    public Model getModel() {
        return model;
    }

    public static Collection<Dot> line(int startx, int starty, int endx, int endy) {
        Collection<Dot> dots = new HashSet<Dot>();
        if (startx==endx) {
            for (int y=starty; y <= endy; y++) {
                dots.add(new Dot(startx, y));
            }
        } else {
            for (int x=startx; x <= endx; x++) {
                dots.add(new Dot(x, starty));
            }
        }
        return dots;
    }

    public static Collection<Dot> fill(int startx, int starty, int endx, int endy, boolean[][] wallAt) {
        Collection<Dot> dots = new HashSet<Dot>();
        for (int x=startx; x<=endx; x++) {
            for (int y=starty; y<=endy; y++) {
                if (! wallAt[x][y]) {
                    dots.add(new Dot(x, y));
                }
            }
        }
        return dots;
    }

    private static void initializeAudio() {
        try {
            initialized = true;
            AudioInputStream ais = AudioSystem.getAudioInputStream(CHOMP_SOUND);
            AudioFormat format = ais.getFormat();
            DataLine.Info info = new DataLine.Info(Clip.class, format);
            chomp_sound = (Clip) AudioSystem.getLine(info);
            play_audio=false;
        } catch(Exception e) {
            play_audio =false;
        }
    }
}
