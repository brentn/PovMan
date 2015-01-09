import sun.net.www.content.audio.wav;
import sun.net.www.content.image.gif;

import java.awt.*;

/**
 * Created by brent on 08/01/15.
 */
public class Man {
    private static int INITIAL_LIVES=3;
    private static wav DIE_SOUND;
    private static gif DIE_ANIMATION;

    boolean alive=false;
    long points=0;
    int lives;
    Point pos;
    int orientation; //0=right

    public Man() {
        lives=INITIAL_LIVES;
    }

    public void eat(Consumable item) {
        points += item.consume();
    }

    public void die() {
        alive=false;
    }



}
