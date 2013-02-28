/**
 * Created with IntelliJ IDEA.
 * Creator: Knútur Óli Magnússon
 * Date: 23.2.2013
 * Time: 22:05
 */
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
public class JamesBondAgent implements Agent {

    private String role;
    private int playclock;
    private boolean myTurn;
    private Random random;
    private State state;
    int counter = 0;
    int alphabeta(State node, int depth, int alpha, int beta, boolean maxplayer)  {
        counter++;
        if( depth == 0 || node.gameFinished() == 1 )
            return node.heuristic_value();
        List<State> children = new ArrayList<State>();
        for(Integer n : node.legalColumns())
            children.add( node.successor(n));
        if(maxplayer)
        {
            for(State child : children)
            {
                alpha = Math.max(alpha, alphabeta(child, depth-1, alpha, beta, !maxplayer ));
                if(beta <= alpha)
                    break; /* Beta cut-off */
            }
            return alpha;
        }
        else
        {
            for(State child : children)
            {
                beta = Math.min(beta, alphabeta(child, depth-1, alpha, beta, !maxplayer ));
                if(beta <= alpha)
                    break; /* Alpha cut-off */
            }
            return beta;
        }
    }


    @Override
    public void init(String role, int playclock) {
        this.role = role;
        this.playclock = playclock;
        myTurn = !role.equals("WHITE");
        this.state = new State(true,new Grid(7,6));
        // TODO: add your own initialization code here
    }

    @Override
    public String nextAction(int lastDrop) {
        System.out.println(lastDrop);
        // TODO: 1. update your internal world model according to the action that was just executed
        if(lastDrop != 0)
        state.putDisc(lastDrop);
        myTurn = !myTurn;
        // TODO: 2. run alpha-beta search to determine the best move


        if (myTurn) {
            List<State> children = new ArrayList<State>();
            int bestScore = Integer.MIN_VALUE;
            int bestColumn = -1;
            for(Integer n : state.legalColumns())
            {
                int score = alphabeta(state.successor(n), 7, Integer.MIN_VALUE, Integer.MAX_VALUE, myTurn);
                if(score > bestScore)
                {
                    bestScore = score;
                    bestColumn = n;
                }
            }
            return "(DROP " + bestColumn + ")";
        } else {
            System.out.println("EXPANSIONS:" + counter);
            return "NOOP";
        }
    }
}
