/**
 * Created by brent on 08/01/15.
 */
public class Wall {
    Position start;
    Position end;

    public Wall(Position start, Position end) {
        this.start = start;
        this.end = end;
    }
    public Wall(int startx, int starty, int endx, int endy) {
        this.start = new Position(startx, starty);
        this.end = new Position(endx, endy);
    }
}
