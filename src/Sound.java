import javax.sound.sampled.*;
import java.io.File;

/**
 * Created by brent on 15/01/15.
 */
public class Sound {
    private boolean play_audio=false;
    private Clip clip;

    public Sound(File sound_file) {
        initialize(sound_file);
    }

    public void whenFinished(final Runnable action) {
        clip.addLineListener(new LineListener() {
            @Override
            public void update(LineEvent lineEvent) {
                if (lineEvent.getType() == LineEvent.Type.STOP) {
                    action.run();
                }
            }
        });
    }

    public void play() { if (play_audio) clip.start(); }

    private void initialize(File file) {
        try {
            AudioInputStream ais = AudioSystem.getAudioInputStream(file);
            AudioFormat format = ais.getFormat();
            DataLine.Info info = new DataLine.Info(Clip.class, format);
            clip = (Clip)AudioSystem.getLine(info);
            clip.open(ais);
        } catch(Exception e) {
            play_audio =false;
        }
        play_audio=true;
    }
}
