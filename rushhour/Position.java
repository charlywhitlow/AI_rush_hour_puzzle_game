package rushhour;

/**
 * This class represents a position on the grid.
 */
public class Position {
    
    private int row;
    private int col;
    
    public Position(int row, int col){
        this.row=row;
        this.col=col;
    }
    
    public int getRow(){
        return row;
    }
    
    public int getCol(){
        return col;
    }

    public boolean equals (Object o){
        if(!(o instanceof Position)) 
            return false;
        Position p = (Position)o;
        return row==p.row && col==p.col;
    }

    public String toString(){
        return "(" +row+","+col+")";
    }
    
}
