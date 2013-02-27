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
    public static void main(String[] args)
    {
        State state = new State(true,new Grid(7,6));
        while(state.legalColumns().size() != 0)
        {
            Random rand = new Random();
            int col = rand.nextInt(state.legalColumns().size());
            state.putDisc(state.legalColumns().get(col));
        }
        state.debug();

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
