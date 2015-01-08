/**
 * Created by brent on 08/01/15.
 */
public class Dot {
    Position pos;
    private boolean consumed = false;

    public Dot(Position pos) {
        this.pos = pos;
    }
    public Dot(int x, int y) {
        this.pos = new Position(x, y);
    }
}
