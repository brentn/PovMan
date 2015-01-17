import javax.sound.sampled.*;
import java.io.File;

/**
 * Created by brent on 15/01/15.
 */
public class Sound {
    private boolean play_audio=false;
    private Clip clip;
    AudioInputStream ais;
    AudioFormat format;
    DataLine.Info info;

    public Sound(File sound_file) {
        try {
            ais = AudioSystem.getAudioInputStream(sound_file);
            format = ais.getFormat();
            info = new DataLine.Info(Clip.class, format);
            clip = (Clip)AudioSystem.getLine(info);
            clip.open(ais);
        } catch(Exception e) {
            play_audio =false;
        }
        play_audio=true;
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

    public void play() {
        if (play_audio) clip.start();
    }

    public void stop() {
        if (play_audio) clip.stop();
    }

    public void loop() {
        if (play_audio) {
            clip.loop(-1);
        }
    }

    public void loop(int times) {
        if (play_audio) {
            clip.loop(times);
        }
    }

    private void initialize(File file) {
    }
}
