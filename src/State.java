import sun.plugin2.util.SystemUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class State {
    Grid grid;
    boolean turn;  //True=WHITE,False=RED
    int currentColumn;

    State(boolean turn,Grid grid)
    {
      this.turn = turn;
      this.grid = grid;
      currentColumn = -1;
    }

    public State successor(int column)
    {
       Disc newDisc;

        if(turn)
             newDisc = Disc.WHITE;
        else newDisc = Disc.RED;

       Grid newGrid = new Grid( grid.getWidth(), grid.getHeight());
       for(int i=0;i<grid.getWidth();i++)
       {
           Stack<Disc> current = grid.getStack(i);
           for(int j=0;j<current.size();j++)
              newGrid.add( current.get(j), i );
       }

        State newState = new State(turn,newGrid);
        newState.putDisc(column);
       return newState;
    }

    public void debug() {
        if(turn)
             System.out.println("WHITE's turn!");
        else System.out.println("RED's turn!");
        System.out.print("LEGAL:");
        for(int i=0;i<legalColumns().size();i++)
            System.out.print(legalColumns().get(i) + " ");
        System.out.println();
        System.out.println("Winner: " + gameFinished() );
        System.out.println("Current col: " + currentColumn);
        grid.debug();
    }
    public List<Integer> legalColumns()
    {
        if(gameFinished() != -1 )
            return new ArrayList<Integer>();
        List<Integer> legal = new ArrayList<Integer>();
        for(int i=0;i<grid.getWidth();i++)
            if(grid.getStack(i).size() < grid.getHeight())
                legal.add(i+1);
        return legal;
    }

    int gameFinished()
    {
            if(currentColumn == -1)
                    return -1;
            int legal = 0;
            for(int i=0;i<grid.getWidth();i++)
                if(grid.getStack(i).size() < grid.getHeight())
                    legal++;
            if(legal == 0)
                return 0;

            else
            {
                int adjR = grid.adjecentToRight(currentColumn-1,grid.getStack(currentColumn-1).size()-1);
                int adjL = grid.adjecentToLeft(currentColumn-1,grid.getStack(currentColumn-1).size()-1);

                if((adjR+adjL) >=3)
                {
                    if(turn)
                        return 2;
                    else
                        return 1;

                }
                int adjUR = grid.adjecentToUpRight(currentColumn-1,grid.getStack(currentColumn-1).size()-1);
                int adjDL = grid.adjecentToDownLeft(currentColumn-1,grid.getStack(currentColumn-1).size()-1);

                if((adjUR+adjDL) >=3)
                {
                    if(turn)
                        return 2;
                    else
                        return 1;

                }
                int adjDR = grid.adjecentToDownRight(currentColumn-1,grid.getStack(currentColumn-1).size()-1);
                int adjUL = grid.adjecentToUpLeft(currentColumn-1,grid.getStack(currentColumn-1).size()-1);

                if((adjDR+adjUL) >=3)
                {
                    if(turn)
                        return 2;
                    else
                        return 1;
                }
                int adjDown = grid.adjecentBelow(currentColumn-1,grid.getStack(currentColumn-1).size()-1);

                if(adjDown >=3)
                {
                    if(turn)
                        return 2;
                    else
                        return 1;
                }
            }
        return -1;
    }

    public void putDisc(int column)
    {
        Disc disc;
        if(turn)
            disc = Disc.WHITE;
        else
            disc = Disc.RED;

        currentColumn = column;
        grid.add(disc, column-1);
        turn = !turn;
    }
    public int heuristic_value()
    {
        int sumWhite  = 0;
        int sumRed  = 0;
        for(int i=0;i<grid.getWidth();i++)
        {
            Stack<Disc> current = grid.getStack(i);
            for(int j=0;j<current.size();j++)
            {
                if(grid.getDisc(i,j) == Disc.WHITE)
                {
                    sumWhite = grid.adjacent(i,j);
                }
                else if(grid.getDisc(i,j) == Disc.RED)
                sumRed  = grid.adjacent(i,j);
            }
        }
        return sumWhite-sumRed;
    }

    @Override
    public boolean equals(Object o)
    {
        State compareState = (State) o;
        if(currentColumn != compareState.currentColumn )
           return false;
        if(turn != compareState.turn)
            return false;
        if(!grid.equals(compareState.grid))
            return false;
        return true;
    }
}
