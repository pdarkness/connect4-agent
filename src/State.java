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

       boolean nextTurn = !turn;
        State newState = new State(nextTurn,newGrid);
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
        System.out.println("Player: " + gameFinished() );
        System.out.println("Current col: " + currentColumn);
        grid.debug();
    }
    public List<Integer> legalColumns()
    {
        List<Integer> legal = new ArrayList<Integer>();
        for(int i=0;i<grid.getWidth();i++)
            if(grid.getStack(i).size() < grid.getHeight())
                legal.add(i);
        return legal;
    }

    int gameFinished()
    {
            if(currentColumn == -1)
                return -1;
            if(legalColumns().size() == 0)
            return 0;

            else
            {
                int adjR = grid.adjecentToRight(currentColumn,grid.getStack(currentColumn).size()-1);
                int adjL = grid.adjecentToLeft(currentColumn,grid.getStack(currentColumn).size()-1);

                System.out.println("adjL: " + adjL );
                System.out.println("AdjR: " + adjR);
                if((adjR+adjL) >=3)
                {
                    if(turn)
                        return 1;
                    else
                        return 2;

                }
                int adjUR = grid.adjecentToUpRight(currentColumn,grid.getStack(currentColumn).size()-1);
                int adjDR = grid.adjecentToDownRight(currentColumn,grid.getStack(currentColumn).size()-1);
                System.out.println("adj Up   Right: " + adjUR);
                System.out.println("Adj Down Right: " + adjDR);

                int adjUL = grid.adjecentToUpLeft(currentColumn,grid.getStack(currentColumn).size()-1);
                int adjDL = grid.adjecentToDownLeft(currentColumn,grid.getStack(currentColumn).size()-1);
                System.out.println("adj Up   Left: " + adjUL);
                System.out.println("Adj Down Left: " + adjDL);
                int adjDown = grid.adjecentBelow(currentColumn,grid.getStack(currentColumn).size()-1);
                System.out.println("Adj down : " + adjDown);

            }
        return -1;
    }

    public void putDisc(int column)
    {
        Disc temp;
        if(turn)
            temp = Disc.WHITE;
        else
            temp = Disc.RED;

        currentColumn = column;
        grid.add(temp, column);
    }
}
