/**
 * Created by brent on 08/01/15.
 */
public class Consumable {
    private int points;
    private boolean consumed=false;

    public Consumable(int points) {
        this.points = points;
    }

    public int consume() {
        consumed=true;
        return points;
    }

    public boolean hasBeenConsumed() { return consumed; }
}
