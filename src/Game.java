/**
 * Created by brent on 08/01/15.
 */
public class Game {
    Grid grid;
    Man man;
    int level;

    public Game() {
        level = 1;
        grid = createGrid(level);
        man = new Man();
    }

    public void play() {
        grid.draw(man);
    }

    private Grid  createGrid(int level) {
        Position top_left = new Position(1,1);
        Position top_right = new Position(24, 1);
        Position bottom_left = new Position(1, 24);
        Position bottom_right = new Position(24, 24);
        Grid result = new Grid();
        result.addWall(top_left, top_right);
        result.addWall(top_right, bottom_right);
        result.addWall(bottom_right, bottom_left);
        result.addWall(bottom_left, top_left);
        return result;
    }
}
