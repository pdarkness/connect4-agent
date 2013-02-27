import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class State {
    Grid grid;
    boolean turn;  //True=WHITE,False=RED

    State(boolean turn,Grid grid)
    {
      this.turn = turn;
      this.grid = grid;
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
       newGrid.add(newDisc,column);
       boolean nextTurn = !turn;
        State newState = new State(nextTurn,newGrid);
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

    int GameFinished(int column)
    {
        if(legalColumns().size() == 0)
            return 0;
        else return 0;
    }
}
