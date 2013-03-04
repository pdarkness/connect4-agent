import javax.naming.TimeLimitExceededException;
import java.util.*;

public class JamesBondAgent implements Agent {
    final int ALPHA = 0;
    final int BETA = 1;
    final int EXACT = 2;

    private String role;
    private int playclock;
    private boolean myTurn;
    private Random random;
    private State state;
    private HashMap<State,Entry> checkedStates;
    private long endTime;
    private int counter = 0;
    private int state_expansions;
    private Disc me;

    private class Entry {
        public int value;
        public int depth;
        public int type; //0=ALPHA,1=BETA,2=EXACT
        Entry(int value,int depth,int type)
        {
            this.value = value;
            this.depth = depth;
            this.type = type;
        }
    }


    int alphabeta(State node, int depth, int alpha, int beta, boolean maxplayer) throws TimeLimitExceededException {
        if(System.currentTimeMillis() > endTime)
            throw new TimeLimitExceededException("Time limit exceeded");
        Entry lookup = checkedStates.get(node);
        if(lookup != null)
            if(     lookup.depth >= depth &&
                    lookup.type == EXACT ||
                    (lookup.type == ALPHA && lookup.value == alpha) ||
                    (lookup.type == BETA && lookup.value == beta)
                    )
            {
                return lookup.value;
            }
        counter++;
        if( depth == 0 || node.gameFinished() != -1 )
        {
            int s = node.heuristic_value(me);
            checkedStates.put(node,new Entry(s,depth,EXACT));
            return s;
        }
        state_expansions++;
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
            checkedStates.put(node,new Entry(alpha,depth,ALPHA));
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
            if(System.currentTimeMillis() > endTime)
                throw new TimeLimitExceededException("Time limit exceeded");
            checkedStates.put(node,new Entry(alpha,depth,BETA));
            return beta;
        }
    }


    @Override
    public void init(String role, int playclock) {
        state_expansions = 0;
        checkedStates = new HashMap<State, Entry>();
        this.role = role;
        this.playclock = playclock;
        myTurn = !role.equals("WHITE");
        if(!myTurn)
            me = Disc.WHITE;
        else   me = Disc.RED;
        this.state = new State(true,new Grid(7,6));
        // TODO: add your own initialization code here
    }

    int turns = 0;
    List<Integer> exp = new ArrayList<Integer>();

    @Override
    public String nextAction(int lastDrop) {
        state_expansions = 0;
        // update your internal world model according to the action that was just executed
        if(lastDrop != 0)
            state.putDisc(lastDrop);
        myTurn = !myTurn;

        //2. run alpha-beta search to determine the best move
        if (myTurn) {
            turns++;
            counter = 0;
            endTime = System.currentTimeMillis()+playclock*1000 - (playclock*1000)/10;
            List<State> children = new ArrayList<State>();
            int bestColumn = -1;
            int depth = 0;
            for(int i=1;;i++)
            {
                try {

                    int currentBestScore = Integer.MIN_VALUE;
                    int currentBestColumn = 1;
                    for(Integer n : state.legalColumns())
                    {
                        int score = alphabeta(state.successor(n), i, Integer.MIN_VALUE, Integer.MAX_VALUE, myTurn);
                        if(score > currentBestScore)
                        {
                            currentBestScore = score;
                            currentBestColumn = n;
                        }
                    }
                    depth = i;
                    bestColumn = currentBestColumn;
                }
                catch(TimeLimitExceededException e)
                {
                    double per_second = state_expansions / playclock;
                    System.out.println("DEPTH REACHED:" + depth);
                    System.out.println("Expansions:" + state_expansions);
                    exp.add(state_expansions);
                    System.out.println("Expansions per second:" + per_second);
                    int total = 0;
                    for(int t=0;t<exp.size();t++)
                        total += exp.get(t);
                    System.out.println("Average expansions:" + total/exp.size());

                    return "(DROP " + bestColumn + ")";
                }
            }
        } else {
            return "NOOP";
        }
    }
}
