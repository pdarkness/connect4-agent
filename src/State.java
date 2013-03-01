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
        if(gameFinished() == 2)
            return Integer.MIN_VALUE;
        if(gameFinished() == 1)
            return Integer.MAX_VALUE;
        int goodPoints  = 0;
        int badPoints  = 0;
        int longestRed = 0;
        int longestWhite = 0;
        for(int i=0;i<grid.getWidth();i++)
        {
            Stack<Disc> current = grid.getStack(i);
            for(int j=0;j<current.size();j++)
            {
                if(grid.getDisc(i,j) == Disc.WHITE)
                {
                    int vertical = grid.adjecentBelow(i,j);
                    int horizontal = grid.adjecentToRight(i,j)+ grid.adjecentToLeft(i,j);
                    int growing = grid.adjecentToUpRight(i,j) + grid.adjecentToDownLeft(i,j);
                    int reducing = grid.adjecentToDownRight(i,j) + grid.adjecentToUpLeft(i,j);
                    int biggest = Math.max(Math.max(vertical,horizontal),Math.max(growing,reducing));
                    goodPoints += biggest;
                    if(grid.getDisc(i+1,j) == Disc.RED && grid.getDisc(i-1,j) == Disc.RED)
                        goodPoints +=50;
                    if(grid.getDisc(i-1,j+1) == Disc.RED && grid.getDisc(i+1,j-1) == Disc.RED)
                        goodPoints +=50;
                    if(grid.getDisc(i-1,j-1) == Disc.RED && grid.getDisc(i+1,j+1) == Disc.RED)
                        goodPoints +=50;
                }
                else if(grid.getDisc(i,j) == Disc.RED)
                {
                    int vertical = grid.adjecentBelow(i,j);
                    if(vertical>=2 && grid.getDisc(i,j+1) == Disc.WHITE)
                        goodPoints +=100;

                    int to_right = grid.adjecentToRight(i,j) ;
                    int to_left = grid.adjecentToLeft(i,j);
                    int horizontal = to_right + to_left;
                    if( i!= 6 && to_left == 2 && grid.getDisc(i+1,j) == Disc.WHITE)
                        goodPoints +=100;
                    if( i != 0 && to_right == 2 && grid.getDisc(i-1,j) == Disc.WHITE)
                        goodPoints +=100;

                    int to_upper_right = grid.adjecentToUpRight(i,j);
                    int to_lower_left =  grid.adjecentToDownLeft(i,j);
                    if(to_upper_right == 2 && j<4 && i<4 && grid.getStack(i+3).size() >=j+3 && grid.getDisc(i+3,j+3) == Disc.WHITE)
                        goodPoints +=100;
                    if(to_lower_left == 2 && j<5 && i<6 && grid.getStack(i+1).size()>=j+1 && grid.getDisc(i+1,j+1) == Disc.WHITE )
                        goodPoints +=100;

                    int growing = to_lower_left + to_upper_right;

                    int to_lower_right =  grid.adjecentToDownRight(i,j);
                    int to_upper_left = grid.adjecentToUpLeft(i,j);

                    if(to_lower_right == 2 && i>0 && j<5 && grid.getStack(i-1).size()>=j+1 && grid.getDisc(i-1,j+1) == Disc.WHITE)
                        goodPoints += 100;
                    if(to_upper_left == 2 && i<6 && j>0 && grid.getStack(i+1).size()>=j-1 && grid.getDisc(i+1,j-1) == Disc.WHITE)
                        goodPoints += 100;
                    int reducing =  to_lower_left + to_upper_left;
                    int biggest = Math.max(Math.max(vertical,horizontal),Math.max(growing,reducing));
                    badPoints += biggest;
                }
            }
        }
        return goodPoints-badPoints;
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

    @Override
    public int hashCode() {
        int numturn;
        if(turn)
            numturn = 2;
        else numturn = 1;
        int randOrd = 0;

        if(!grid.getStack(0).isEmpty())
            randOrd *= grid.getStack(0).peek().ordinal()*grid.getStack(0).size();
        if(!grid.getStack(1).isEmpty())
            randOrd *= 1+grid.getStack(1).peek().ordinal()*grid.getStack(1).size();
        if(!grid.getStack(2).isEmpty())
            randOrd *= 2+grid.getStack(2).peek().ordinal()*grid.getStack(2).size();
        if(!grid.getStack(3).isEmpty())
            randOrd += 3+grid.getStack(3).peek().ordinal()*grid.getStack(3).size();
        if(!grid.getStack(4).isEmpty())
            randOrd *= 4+grid.getStack(4).peek().ordinal()*grid.getStack(4).size();
        if(!grid.getStack(5).isEmpty())
            randOrd += 5+grid.getStack(5).peek().ordinal()*grid.getStack(5).size();
        if(!grid.getStack(6).isEmpty())
            randOrd *= 6+grid.getStack(6).peek().ordinal()*grid.getStack(6).size();
        //System.out.println(currentColumn*numturn+randOrd);
        return currentColumn+numturn+randOrd+grid.getClass().hashCode()+grid.getStack(5).hashCode()+grid.getStack(6).hashCode()+grid.getStack(1).hashCode()+grid.getStack(2).hashCode()+grid.getStack(3).hashCode();
    }
}
