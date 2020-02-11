package rushhour;

import java.util.ArrayList;
import java.util.List;

/**
 * This class represents the properties of a car (i.e. its length and orientation), as well as its
 * position on the grid. It is used by the GameState class.
 */
public class Car {
    
    private int row;
    private int col;
    private int length;
    private boolean vertical;
    
    public Car (int row, int col, int length, boolean vertical){
        this.row=row;
        this.col=col;
        this.length=length;
        this.vertical=vertical;
    }
    
    public Car(String s, boolean vertical){
        String [] s0 = s.split("\\s");        
        row = Integer.parseInt(s0[0]);
        col = Integer.parseInt(s0[1]);
        length = Integer.parseInt(s0[2]);
        this.vertical=vertical;        
    }
    
    public Car(String s){
        String [] s0 = s.split("\\s");        
        row = Integer.parseInt(s0[0]);
        col = Integer.parseInt(s0[1]);
        length = Integer.parseInt(s0[2]);
        vertical=s0[3].equals("V");        
    }
    
    public Car(Car c){
        row=c.row;
        col=c.col;
        length=c.length;
        vertical=c.vertical;
    }
    
    public int getRow(){
        return row;
    }
    
    public int getCol(){
        return col;
    }

    public int getLength(){
        return length;
    }        
    
    public boolean isVertical(){
        return vertical;
    }        
    
    public void setRow(int row){
        this.row=row;
    }
    
    public void setCol(int col){
        this.col=col;
    }
           
    public void setLength(int length){
        this.length=length;
    }
    
    public List<Position> getOccupyingPositions(){
        List<Position> res = new ArrayList();
        for(int i=0; i<length; i++){
            if(vertical)
                res.add(new Position(row+i,col));
            else
                res.add(new Position(row,col+i));
        }            
        return res;
    }
    
    public boolean equals (Object o){
        if(!(o instanceof Car))
            return false;
        Car c = (Car)o;
        return row==c.row && col==c.col && length==c.length && vertical==c.vertical;
    }
    
    public int hashCode(){
        return row + 11*col + 23*length  + (vertical?1000:0);
    }

    public String toString(){
        String v = this.vertical == true ? "V" : "H";
        return "(car_"+this.hashCode()+": pos:(" +this.row+","+this.col+") len:"+this.length+" "+v+")";
    }    
}
