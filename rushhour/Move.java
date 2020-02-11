/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rushhour;

import search.Action;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author steven
 */
public class Move implements Action{
    
    Car car;
    Position newPosition;
    
    public Move (Car car, Position newPosition){
        this.car = car;
        this.newPosition = newPosition;
    }

    public Car getCar(){
        return this.car;
    }
    public Position getPosition(){
        return this.newPosition;
    }
    public int getCost() {
        return 1;
    }
    
    public String toString(){
        String v = this.car.isVertical() == true ? "V" : "H";
        return "Move object: (car: ("+this.car.getRow()+","+this.car.getCol()+") len:"+this.car.getLength()+" "+v+"), "+
                "position: "+this.newPosition+")";
    }    
}
