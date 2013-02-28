import com.sun.org.apache.bcel.internal.generic.ACONST_NULL;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Stack;

enum Disc {
    RED,
    WHITE
 }
public class Grid {

    private int height;
    List<Stack<Disc>> columns;
    public Grid(int x, int y)
    {
        height = y;
        columns = new ArrayList<Stack<Disc>>();
        for(int i=0;i<x;i++)
            columns.add( new Stack<Disc>() );
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return columns.size();
    }

    public void add(Disc disc,int column) {
        if(column > getWidth() )
           throw new IndexOutOfBoundsException();
        Stack<Disc> current = columns.get(column);
        if(current.size() >= height)
        {
           System.out.println(column);
           throw new IndexOutOfBoundsException();
        }
        current.push(disc);
    }

    public Disc getDisc(int column,int row)
    {
        if(column > getWidth() )
            throw new IndexOutOfBoundsException();
        Stack<Disc> current = columns.get(column);
        if(row >= current.size())
            return null;
        return current.elementAt(row);
    }

    public Stack<Disc> getStack(int column)
    {
        if(column > getWidth() )
            throw new IndexOutOfBoundsException();
        return columns.get(column);
    }

    public void debug() {
        for(int i=getHeight()-1;i>=0;i--)
        {
            for(int j=0;j<getWidth();j++)
            {
                if(getDisc(j,i) == null)
                    System.out.print("- ");
                if(getDisc(j,i) == Disc.RED)
                     System.out.print("R ");
                else if(getDisc(j,i) == Disc.WHITE)
                        System.out.print("W ");
            }
            System.out.println();
        }
    }
    static int alphabeta(State node, int depth, int alpha, int beta, boolean maxplayer)  {
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
    public static void main(String[] args)
    {
        boolean myTurn = true;
        State state = new State(true,new Grid(7,6));
        state.successor(4).debug();
        return;
       /*
        int count = 0;
        while(state.legalColumns().size() != 0)
        {
            Random rand = new Random();
            int col = rand.nextInt(state.legalColumns().size());
            myTurn =   count % 2 != 0;
            if(myTurn)
                state.putDisc(state.legalColumns().get(rand.nextInt(state.legalColumns().size())));
            else {
            int best = -999999;
            int bestMove = -1;
            for(int i=0;i<state.legalColumns().size();i++)
            {
                int score = alphabeta(state.successor(state.legalColumns().get(i)), 6, Integer.MIN_VALUE, Integer.MAX_VALUE, myTurn);
                if(score > best)
                {
                    best = score;
                    bestMove = i;
                }
            }
            state.putDisc(state.legalColumns().get(state.legalColumns().get(bestMove)));
            }
            count++;
        }
        state.debug();
        System.out.println("Points:" + state.heuristic_value());
                          */
    }

    public int adjecentToLeft(int x, int y)
    {
        if(getDisc(x,y) ==null)
            return 0;
        if(x <= 0)
            return 0;
        if(getDisc(x-1,y) == getDisc(x,y))
            return 1+adjecentToLeft(x-1,y);
        return 0;
    }

    public int adjecentToRight(int x, int y)
    {
        if(getDisc(x,y)==null)
            return 0;
        if(x  >= getWidth()-1)
            return 0;
        if(getDisc(x+1,y) == getDisc(x,y))
            return 1+adjecentToRight(x+1,y);
        return 0;
    }

    public int adjecentToUpRight(int x, int y)
    {
        if(getDisc(x,y)==null)
            return 0;
        if(x  >= getWidth()-1 || y >= getHeight())
            return 0;
        if(getDisc(x+1,y+1) == getDisc(x,y))
            return 1+adjecentToUpRight(x+1,y+1);
        return 0;
    }

    public int adjecentToDownRight(int x, int y)
    {
        if(getDisc(x,y)==null)
            return 0;
        if(x  >= getWidth()-1 || y <= 0)
            return 0;
        if(getDisc(x+1,y-1) == getDisc(x,y))
            return 1+adjecentToDownRight(x+1,y-1);
        return 0;
    }

    public int adjecentToDownLeft(int x, int y)
    {
        if(getDisc(x,y)==null)
            return 0;
        if(x  <= 0 || y <=0)
            return 0;
        if(getDisc(x-1,y-1) == getDisc(x,y))
            return 1+adjecentToDownLeft(x-1,y-1);
        return 0;
    }

    public int adjecentToUpLeft(int x, int y)
    {
        if(getDisc(x,y)==null)
            return 0;
        if(x  <= 0 || y >= getHeight())
            return 0;
        if(getDisc(x-1,y+1) == getDisc(x,y))
            return 1+adjecentToUpLeft(x-1,y+1);
        return 0;
    }

    public int adjecentBelow(int x, int y)
    {
        if(getDisc(x,y)==null)
            return 0;
        if(y  <= 0)
            return 0;
        if(getDisc(x,y-1) == getDisc(x,y))
            return 1+adjecentBelow(x,y-1);
        return 0;
    }
    public int adjacent( int x , int y ) {
        int sum = 0;
        if(x-1 >= 0)
        {
           if( getDisc(x-1,y) == getDisc(x,y))
                sum++;
            if(y-1 >= 0)
                if( getDisc(x-1,y-1) == getDisc(x,y))
                     sum++;
            if(y+1 < getHeight() )
                if(getDisc(x-1,y+1) == getDisc(x,y))
                    sum++;
        }
        if(x+1 < getWidth())
        {
            if( getDisc(x+1,y) == getDisc(x,y))
                sum++;
            if(y+1 < getHeight())
                if( getDisc(x+1,y+1) == getDisc(x,y))
                    sum++;
            if(y-1 >=0)
                if( getDisc(x+1,y-1) == getDisc(x,y))
                    sum++;
        }
        if(y+1 < getHeight())
            if(getDisc(x,y+1) == getDisc(x,y))
                sum++;
        if(y-1>=0)
            if(getDisc(x,y-1) == getDisc(x,y))
                sum++;
        return sum;
    }

}
