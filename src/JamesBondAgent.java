/**
 * Created with IntelliJ IDEA.
 * Creator: Knútur Óli Magnússon
 * Date: 23.2.2013
 * Time: 22:05
 */
import javax.naming.TimeLimitExceededException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
public class JamesBondAgent implements Agent {

    private class StateRecord {
        public State node;
        public int depth;
        public boolean maxplayer;
        public int alpha;
        public int beta;
        public StateRecord(State node, int depth, int alpha, int beta, boolean maxplayer)
        {
            this.node = node;
            this.depth = depth;
            this.alpha = alpha;
            this.beta = beta;
            this.maxplayer = maxplayer;
        }
        @Override
        public boolean equals(Object o)
        {
            StateRecord sr = (StateRecord) o;
            if(beta != sr.beta)
                return false;
            if(alpha != sr.alpha)
                return false;
            if(maxplayer != sr.maxplayer)
                return false;
            if(depth != sr.depth)
                return false;
            if(!node.equals(sr))
                return false;
            return true;
        }
    }
    private String role;
    private int playclock;
    private boolean myTurn;
    private Random random;
    private State state;
    private HashSet<StateRecord> checkedStates;
    long endTime;
    int counter = 0;
    int alphabeta(State node, int depth, int alpha, int beta, boolean maxplayer) throws TimeLimitExceededException {
        StateRecord sr = new StateRecord(node, depth, alpha, beta, maxplayer);
        checkedStates.add(sr);
        if(System.currentTimeMillis() > endTime)
            throw new TimeLimitExceededException("Time limit exceeded");
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
        checkedStates = new HashSet<StateRecord>();
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
            endTime = System.currentTimeMillis()+playclock*1000 - 300;
            List<State> children = new ArrayList<State>();
            int bestScore = Integer.MIN_VALUE;
            int bestColumn = -1;
            for(int i=1;;i++)
            {
                try {
                    int currentBestScore = Integer.MIN_VALUE;
                    int currentBestColumn = -1;
                    for(Integer n : state.legalColumns())
                    {
                        int score = alphabeta(state.successor(n), i, Integer.MIN_VALUE, Integer.MAX_VALUE, myTurn);
                            if(score > bestScore)
                            {
                                currentBestScore = score;
                                currentBestColumn = n;
                            }
                    }
                    bestScore = currentBestScore;
                    bestColumn = currentBestColumn;
                }
                catch(TimeLimitExceededException e)
                {
                    break;
                }
            }
            return "(DROP " + bestColumn + ")";
        } else {
            System.out.println("EXPANSIONS:" + counter);
            return "NOOP";
        }
    }
}
