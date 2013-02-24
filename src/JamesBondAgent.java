/**
 * Created with IntelliJ IDEA.
 * Creator: Knútur Óli Magnússon
 * Date: 23.2.2013
 * Time: 22:05
 */
import java.util.Random;
public class JamesBondAgent implements Agent {

    private String role;
    private int playclock;
    private boolean myTurn;
    private Random random;

    @Override
    public void init(String role, int playclock) {
        this.role = role;
        this.playclock = playclock;
        myTurn = !role.equals("WHITE");
        // TODO: add your own initialization code here
    }

    @Override
    public String nextAction(int lastDrop) {
        // TODO: 1. update your internal world model according to the action that was just executed

        myTurn = !myTurn;
        // TODO: 2. run alpha-beta search to determine the best move

        if (myTurn) {
            return "(DROP " + ( random.nextInt(7)+1) + ")";
        } else {
            return "NOOP";
        }
    }
}
