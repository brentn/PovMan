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

    @Override
    public int consume() {
        play_sound();
        return super.consume();
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

    public static Collection<Dot> line(Point a, Point b) {
        Collection<Dot> dots = new HashSet<Dot>();
        if (a.x==b.x) {
            for (int y=a.y; y < b.y; y++) {
                dots.add(new Dot(a.x, y));
            }
        } else {
            for (int x=a.x; x < b.x; x++) {
                dots.add(new Dot(x, a.y));
            }
        }
        return dots;
    }

    private static void play_sound() {
        //if (play_audio) chomp_sound.start();
    }

    private static void initializeAudio() {
        try {
            initialized = true;
            AudioInputStream ais = AudioSystem.getAudioInputStream(CHOMP_SOUND);
            AudioFormat format = ais.getFormat();
            DataLine.Info info = new DataLine.Info(Clip.class, format);
            chomp_sound = (Clip) AudioSystem.getLine(info);
            chomp_sound.open(ais);
        } catch(Exception e) {
            play_audio =false;
        }
    }
}
